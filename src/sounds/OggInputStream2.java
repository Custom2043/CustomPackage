package sounds;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import util.Logger;

import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;
import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.Comment;
import com.jcraft.jorbis.DspState;
import com.jcraft.jorbis.Info;


/**
 * Decompresses an Ogg file.
 * <p>
 * How to use:<br>
 * 1. Create OggInputStream passing in the input stream of the packed ogg file<br>
 * 2. Fetch format and sampling rate using getFormat() and getRate(). Use it to
 *    initalize the sound player.<br>
 * 3. Read the PCM data using one of the read functions, and feed it to your player.
 * <p>
 * OggInputStream provides a read(ByteBuffer, int, int) that can be used to read
 * data directly into a native buffer.
 */
class OggInputStream2 extends FilterInputStream {

	/** The mono 16 bit format */
	public static final int FORMAT_MONO16 = 1;
	
	/** The stereo 16 bit format */
	public static final int FORMAT_STEREO16 = 2;

	// temp vars
	private float[][][] _pcm = new float[1][][];
	private int[] _index;

	// end of stream
	private boolean eos = false;

	// sync and verify incoming physical bitstream
	private SyncState syncState = new SyncState(); 

	// take physical pages, weld into a logical stream of packets
	private StreamState streamState = new StreamState(); 

	// one Ogg bitstream page.  Vorbis packets are inside
	private Page page = new Page(); 

	// one raw packet of data for decode
	private Packet packet = new Packet(); 

	// struct that stores all the static vorbis bitstream settings
	private Info info = new Info(); 

	// struct that stores all the bitstream user comments
	private Comment comment = new Comment(); 

	// central working state for the packet->PCM decoder
	private DspState dspState = new DspState(); 

	// local working space for packet->PCM decode
	private Block block = new Block(this.dspState); 

	/// Conversion buffer size
	private static int convsize = 4096 * 2;
	
	// Conversion buffer
	private static byte[] convbuffer = new byte[convsize];
	
	// where we are in the convbuffer
	private int convbufferOff = 0;

	// bytes ready in convbuffer.
	private int convbufferSize = 0;

	// a dummy used by read() to read 1 byte.
	private byte readDummy[] = new byte[1];
	

	/**
	 * Creates an OggInputStream that decompressed the specified ogg file.
	 */
	public OggInputStream2(InputStream input) {
		super(input);
		try {
			this.initVorbis();
			this._index = new int[this.info.channels];
		} catch (Exception e) {
			Logger.error(e, this.getClass());
			this.eos = true;
		}
	}


	/**
	 * Gets the format of the ogg file. Is either FORMAT_MONO16 or FORMAT_STEREO16
	 */
	public int getFormat() {
		if (this.info.channels == 1) {
			return FORMAT_MONO16;
		} else {
			return FORMAT_STEREO16;
		}
	}


	/**
	 * Gets the rate of the pcm audio.
	 */
	public int getRate() {
		return this.info.rate;
	}


	/**
	 * Reads the next byte of data from this input stream. The value byte is
	 * returned as an int in the range 0 to 255. If no byte is available because
	 * the end of the stream has been reached, the value -1 is returned. This
	 * method blocks until input data is available, the end of the stream is
	 * detected, or an exception is thrown. 
	 * @return the next byte of data, or -1 if the end of the stream is reached.
	 */
	@Override
	public int read() throws IOException {
		int retVal = this.read(this.readDummy, 0, 1);
		return (retVal == -1 ? -1 : this.readDummy[0]);
	}

	
	/**
	 * Reads up to len bytes of data from the input stream into an array of bytes.
	 * @param b the buffer into which the data is read.
	 * @param off the start offset of the data.
	 * @param len the maximum number of bytes read.
	 * @return the total number of bytes read into the buffer, or -1 if there is
	 *         no more data because the end of the stream has been reached. 
	 */
	@Override
	public int read(byte b[], int off, int len) throws IOException {
		if (this.eos) {
			return -1;
		}

		int bytesRead = 0;
		while (!this.eos && (len > 0)) {
			this.fillConvbuffer();
			
			if (!this.eos) {
				int bytesToCopy = Math.min(len, this.convbufferSize-this.convbufferOff);
				System.arraycopy(convbuffer, this.convbufferOff, b, off, bytesToCopy);
				this.convbufferOff += bytesToCopy;
				bytesRead += bytesToCopy;
				len -= bytesToCopy;
				off += bytesToCopy;
			}
		}

		return bytesRead;
	}

	
	/**
	 * Reads up to len bytes of data from the input stream into a ByteBuffer.
	 * @param b the buffer into which the data is read.
	 * @param off the start offset of the data.
	 * @param len the maximum number of bytes read.
	 * @return the total number of bytes read into the buffer, or -1 if there is
	 *         no more data because the end of the stream has been reached. 
	 */
	public int read(ByteBuffer b, int off, int len) throws IOException {
		if (this.eos) {
			return -1;
		}

		b.position(off);
		int bytesRead = 0;
		while (!this.eos && (len > 0)) {
			this.fillConvbuffer();
			
			if (!this.eos) {
				int bytesToCopy = Math.min(len, this.convbufferSize-this.convbufferOff);
				b.put(convbuffer, this.convbufferOff, bytesToCopy);
				this.convbufferOff += bytesToCopy;
				bytesRead += bytesToCopy;
				len -= bytesToCopy;
			}
		}

		return bytesRead;
	}
	public ByteBuffer read(int length) {
		ByteBuffer b = ByteBuffer.allocateDirect(length);

		int bytesRead = 0;
		while (!this.eos && (length > 0)) {
			try{
			this.fillConvbuffer();
			}catch(Exception e){}
			
			if (!this.eos) {
				int bytesToCopy = Math.min(length, this.convbufferSize-this.convbufferOff);
				b.put(convbuffer, this.convbufferOff, bytesToCopy);
				this.convbufferOff += bytesToCopy;
				bytesRead += bytesToCopy;
				length -= bytesToCopy;
			}
		}
		
		b.rewind();
		
		if (this.eos)
		{
			ByteBuffer bb = ByteBuffer.allocateDirect(bytesRead);
			for (int i=0;i<bytesRead;i++)
				bb.put(b.get());
			
			bb.rewind();
			return bb;
		}
		return b;
		//return bytesRead;
	}
	/**
	 * Max Size : 2 M
	 * Can fail cause of out of memory
	 */
	public ByteBuffer readAll()
	{
		return this.read(2097152);
	}
	/**
	 * Helper function. Decodes a packet to the convbuffer if it is empty. 
	 * Updates convbufferSize, convbufferOff, and eos.
	 */
	private void fillConvbuffer() throws IOException {
		if (this.convbufferOff >= this.convbufferSize) {
			this.convbufferSize = this.lazyDecodePacket();
			this.convbufferOff = 0;
			if (this.convbufferSize == -1) {
				this.eos = true;
			}
		}
	}


	/**
	 * Returns 0 after EOF is reached, otherwise always return 1.
	 * <p>
	 * Programs should not count on this method to return the actual number of
	 * bytes that could be read without blocking.
	 * @return 1 before EOF and 0 after EOF is reached. 
	 */
	@Override
	public int available() throws IOException {
		return (this.eos ? 0 : 1);
	}


	/**
	 * OggInputStream does not support mark and reset. This function does nothing.
	 */
	@Override
	public void reset() throws IOException {
	}


	/**
	 * OggInputStream does not support mark and reset.
	 * @return false.
	 */
	@Override
	public boolean markSupported() {
		return false;
	}


	/**
	 * Skips over and discards n bytes of data from the input stream. The skip
	 * method may, for a variety of reasons, end up skipping over some smaller
	 * number of bytes, possibly 0. The actual number of bytes skipped is returned. 
	 * @param n the number of bytes to be skipped. 
	 * @return the actual number of bytes skipped.
	 */
	@Override
	public long skip(long n) throws IOException {
		int bytesRead = 0;
		while (bytesRead < n) {
			int res = this.read();
			if (res == -1) {
				break;
			}

			bytesRead++;
		}
		
		return bytesRead;
	}
	
	
	/**
	 * Initalizes the vorbis stream. Reads the stream until info and comment are read.
	 */
	private void initVorbis() throws Exception {
		// Now we can read pages
		this.syncState.init();

		// grab some data at the head of the stream.  We want the first page
		// (which is guaranteed to be small and only contain the Vorbis
		// stream initial header) We need the first page to get the stream
		// serialno.

		// submit a 4k block to libvorbis' Ogg layer
		int index = this.syncState.buffer(4096);
		byte buffer[] = this.syncState.data;
		int bytes = this.in.read(buffer, index, 4096);
		this.syncState.wrote(bytes);

		// Get the first page.
		if (this.syncState.pageout(this.page) != 1) {
			// have we simply run out of data?  If so, we're done.
			if (bytes < 4096)
				return;//break;

			// error case.  Must not be Vorbis data
			throw new Exception("Input does not appear to be an Ogg bitstream.");
		}

		// Get the serial number and set up the rest of decode.
		// serialno first; use it to set up a logical stream
		this.streamState.init(this.page.serialno());

		// extract the initial header from the first page and verify that the
		// Ogg bitstream is in fact Vorbis data

		// I handle the initial header first instead of just having the code
		// read all three Vorbis headers at once because reading the initial
		// header is an easy way to identify a Vorbis bitstream and it's
		// useful to see that functionality seperated out.

		this.info.init();
		this.comment.init();
		if (this.streamState.pagein(this.page) < 0) {
			// error; stream version mismatch perhaps
			throw new Exception("Error reading first page of Ogg bitstream data.");
		}

		if (this.streamState.packetout(this.packet) != 1) {
			// no page? must not be vorbis
			throw new Exception("Error reading initial header packet.");
		}

		if (this.info.synthesis_headerin(this.comment, this.packet) < 0) {
			// error case; not a vorbis header
			throw new Exception("This Ogg bitstream does not contain Vorbis audio data.");
		}

		// At this point, we're sure we're Vorbis.  We've set up the logical
		// (Ogg) bitstream decoder.  Get the comment and codebook headers and
		// set up the Vorbis decoder

		// The next two packets in order are the comment and codebook headers.
		// They're likely large and may span multiple pages.  Thus we read
		// and submit data until we get our two packets, watching that no
		// pages are missing.  If a page is missing, error out; losing a
		// header page is the only place where missing data is fatal. 

		
		int i = 0;
		while (i < 2) {
			while (i < 2) {

				int result = this.syncState.pageout(this.page);
				if (result == 0)
					break; // Need more data
				// Don't complain about missing or corrupt data yet.  We'll
				// catch it at the packet output phase

				if (result == 1) {
					this.streamState.pagein(this.page); // we can ignore any errors here
					// as they'll also become apparent
					// at packetout
					while (i < 2) {
						result = this.streamState.packetout(this.packet);
						if (result == 0) {
							break;
						}
						
						if (result == -1) {
							// Uh oh; data at some point was corrupted or missing!
							// We can't tolerate that in a header.  Die.
							throw new Exception("Corrupt secondary header. Exiting.");
						}

						this.info.synthesis_headerin(this.comment, this.packet);
						i++;
					}
				}
			}

			// no harm in not checking before adding more
			index = this.syncState.buffer(4096);
			buffer = this.syncState.data;
			bytes = this.in.read(buffer, index, 4096);

			// NOTE: This is a bugfix. read will return -1 which will mess up syncState.
			if (bytes < 0 ) {
				bytes = 0;
			}
			
			if (bytes == 0 && i < 2) {
				throw new Exception("End of file before finding all Vorbis headers!");
			}

			this.syncState.wrote(bytes);
		}

		convsize = 4096 / this.info.channels;

		// OK, got and parsed all three headers. Initialize the Vorbis
		//  packet->PCM decoder.
		this.dspState.synthesis_init(this.info); // central decode state
		this.block.init(this.dspState); // local state for most of the decode
		// so multiple block decodes can
		// proceed in parallel.  We could init
		// multiple vorbis_block structures
		// for vd here
	}


	/**
	 * Decodes a packet.
	 */
	private int decodePacket(Packet packet) {
		// check the endianes of the computer.
		final boolean bigEndian = ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;
		
		if (this.block.synthesis(packet) == 0) { 
			// test for success!
			this.dspState.synthesis_blockin(this.block);
		}

		// **pcm is a multichannel float vector.  In stereo, for
		// example, pcm[0] is left, and pcm[1] is right.  samples is
		// the size of each channel.  Convert the float values
		// (-1.<=range<=1.) to whatever PCM format and write it out
		int convOff = 0;
		int samples;
		while ((samples = this.dspState.synthesis_pcmout(this._pcm, this._index)) > 0) {
			float[][] pcm = this._pcm[0];
			int bout = (samples < convsize ? samples : convsize);

			// convert floats to 16 bit signed ints (host order) and interleave
			for (int i = 0; i < this.info.channels; i++) {
				int ptr = (i << 1) + convOff;


				int mono = this._index[i];

				for (int j = 0; j < bout; j++) {
					int val = (int) (pcm[i][mono + j] * 32767.);

					// might as well guard against clipping
					val = Math.max(-32768, Math.min(32767, val));
					val |= (val < 0 ? 0x8000 : 0);
					
					convbuffer[ptr + 0] = (byte) (bigEndian ? val >>> 8 : val);
					convbuffer[ptr + 1] = (byte) (bigEndian ? val : val >>> 8);
					
					ptr += (this.info.channels) << 1;
				}
			}

			convOff += 2 * this.info.channels * bout;

			// Tell orbis how many samples were consumed
			this.dspState.synthesis_read(bout);
		}
	
		return convOff;
	}

	
	/**
	 * Decodes the next packet.
	 * @return bytes read into convbuffer of -1 if end of file
	 */
	private int lazyDecodePacket() throws IOException {
		int result = this.getNextPacket(this.packet);
		if (result == -1) {
			return -1;
		}

		// we have a packet.  Decode it
		return this.decodePacket(this.packet);
	}


	/**
	 * @param packet where to put the packet.
	 */
	private int getNextPacket(Packet packet) throws IOException {
		// get next packet.
		boolean fetchedPacket = false;
		while (!this.eos && !fetchedPacket) {
			int result1 = this.streamState.packetout(packet);
			if (result1 == 0) {
				// no more packets in page. Fetch new page.
				int result2 = 0;
				while (!this.eos && result2 == 0) {
					result2 = this.syncState.pageout(this.page);
					if (result2 == 0) {
						this.fetchData();
					}
				}

				// return if we have reaced end of file.
				if ((result2 == 0) && (this.page.eos() != 0)) {
					return -1;
				}
				
				if (result2 == 0) {
					// need more data fetching page..
					this.fetchData();
				} else if (result2 == -1) {
					//throw new Exception("syncState.pageout(page) result == -1");
					Logger.warning("syncState.pageout(page) result == -1", this.getClass());
					return -1;
				} else {
					this.streamState.pagein(this.page);
				}
			} else if (result1 == -1) {
				//throw new Exception("streamState.packetout(packet) result == -1");
				Logger.warning("syncState.pageout(page) result == -1", this.getClass());
				return -1;
			} else {
				fetchedPacket = true;
			}
		}

		return 0;
	}


	/**
	 * Copys data from input stream to syncState.
	 */
	private void fetchData() throws IOException {
		if (!this.eos) {
			// copy 4096 bytes from compressed stream to syncState.
			int index = this.syncState.buffer(4096);
			if (index < 0) {
				this.eos = true;
				return;
			}
			int bytes = this.in.read(this.syncState.data, index, 4096);
			this.syncState.wrote(bytes); 
			if (bytes == 0) {
				this.eos = true;
			}
		}
	}


	/**
	 * Gets information on the ogg.
	 */
	@Override
	public String toString() {
		String s = "";
		s = s + "version         " + this.info.version         + "\n";
		s = s + "channels        " + this.info.channels        + "\n";
		s = s + "rate (hz)       " + this.info.rate            ;
		return s;
	}
}