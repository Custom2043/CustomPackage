package sounds;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;

import org.lwjgl.openal.AL10;

import util.Logger;

public class CodecWav extends Codec
{
	public int bitsPerSample, channels, fileSize;
	final FileChannel file;
	public CodecWav(String s)
	{
		this.file = this.openFile(s);
		try
		{
			this.file.position(22);
			this.channels = this.getValue(2);
			this.samplerate = this.getValue(4);
			this.file.position(34);
			this.bitsPerSample = this.getValue(2);
			this.file.position(40);
			this.fileSize = this.getValue(4);
			this.file.position(44);
			} catch(IOException e){Logger.error(e, this.getClass());}
			if (this.bitsPerSample == 8)
				this.audioFormat = this.channels == 1 ? AL10.AL_FORMAT_MONO8 : AL10.AL_FORMAT_STEREO8;
			else if (this.bitsPerSample == 16)
				this.audioFormat = this.channels == 1 ? AL10.AL_FORMAT_MONO16 : AL10.AL_FORMAT_STEREO16;

			Logger.debug("samplerate : " + this.samplerate, this.getClass());
			Logger.debug("channels : " + this.channels, this.getClass());
			Logger.debug("audioFormat : " + this.audioFormat, this.getClass());
			this.bufferSize = this.samplerate*(this.bitsPerSample/8)*this.channels;
	}

	@Override
	public ByteBuffer read(int length) {
		try
		{
			ByteBuffer bb = ByteBuffer.allocateDirect(Math.min(length, this.fileSize - (int)(this.file.position()-44)));

			//Position - entete
			this.file.read(bb);

			bb.clear();

			return bb;
		}
		catch (IOException e) {Logger.error(e, this.getClass());}
		return null;
	}

	FileChannel openFile(String s)
	{
		try {return FileChannel.open(Paths.get(s));}
		catch (IOException e) {Logger.error(e, this.getClass());return null;}
	}

	@Override
	public void quit()
	{
		try {
			this.file.close();
		} catch (IOException e) {
			Logger.error(e, this.getClass());
		}
	}
	int getValue(int number) throws IOException
	{
		ByteBuffer b = ByteBuffer.allocateDirect(number);
		this.file.read(b);
		b.clear();
		int value = 0;
		for (int i=0;i<number;i++)
		{
			int by = b.get();
			if (by < 0)
				by = 256 + by;
			value += by << (8*i);
		}
		return value;
	}

	@Override
	public void reset() {
		try {this.file.position(0);}
		catch (IOException e) {Logger.error(e, this.getClass());}
	}
}
