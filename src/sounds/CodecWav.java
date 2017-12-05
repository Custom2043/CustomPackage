package sounds;

import java.io.IOException;
import java.io.InputStream;

import org.lwjgl.openal.AL10;

import util.Logger;

public class CodecWav extends Codec
{
	boolean over = false;
	int bitsPerSample, channels, fileSize, samplerate, audioFormat;
	public CodecWav(InputStream is)
	{
		super(is);
		try
		{
			stream.skip(22);
			this.channels = this.getValue(2);
			this.samplerate = this.getValue(4);
			stream.skip(6);
			this.bitsPerSample = this.getValue(2);
			stream.skip(4);
			this.fileSize = this.getValue(4);
		} catch(IOException e){Logger.error(e, this.getClass());}
		if (this.bitsPerSample == 8)
			this.audioFormat = this.channels == 1 ? AL10.AL_FORMAT_MONO8 : AL10.AL_FORMAT_STEREO8;
		else if (this.bitsPerSample == 16)
			this.audioFormat = this.channels == 1 ? AL10.AL_FORMAT_MONO16 : AL10.AL_FORMAT_STEREO16;

		Logger.debug("samplerate : " + this.samplerate, this.getClass());
		Logger.debug("channels : " + this.channels, this.getClass());
		Logger.debug("audioFormat : " + this.audioFormat, this.getClass());
	}

	@Override
	public SoundBuffer readChunk(int length) {
		try
		{
			length -= length % (bitsPerSample * channels); // We take an entire sample
			byte[] b = new byte[length];
			int read = stream.read(b);
			if (stream.available() <= 0)
				over = true;
			return new SoundBuffer(b, read, samplerate, audioFormat);
		}
		catch (IOException e) {Logger.error(e, this.getClass());}
		return null;
	}
	@Override
	public void quit(){}
	
	int getValue(int numberLength) throws IOException
	{
		byte[] b = new byte[numberLength];
		stream.read(b);
		int value = 0;
		for (int i=0;i<b.length;i++)
		{
			int bb = b[i];
			if (bb < 0)
				bb = 256 + bb;
			value += bb << (8*i);
		}
		return value;
	}

	@Override
	public SoundBuffer readAll() 
	{
		return readChunk(fileSize);
	}

	@Override
	public boolean isStreamOver() {
		return over;
	}
}
