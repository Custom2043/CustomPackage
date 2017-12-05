package sounds;

import java.io.IOException;
import java.io.InputStream;

import org.lwjgl.openal.AL10;
import org.newdawn.slick.openal.OggInputStream;

import util.Logger;

public class CodecOgg extends Codec
{
	protected OggInputStream oggStream;
	public int channels, samplerate, audioFormat;
	public CodecOgg(InputStream is) throws IOException
	{
		super(is);
		this.oggStream = new OggInputStream(is);
		this.samplerate = this.oggStream.getRate();
		this.audioFormat = this.oggStream.getChannels() == 1 ? AL10.AL_FORMAT_MONO16 : AL10.AL_FORMAT_STEREO16;
		this.channels = oggStream.getChannels();
	}
	@Override
	public SoundBuffer readChunk(int length) {
		try 
		{
			length -= length % (16 * channels); // We take an entire sample
			byte[] by = new byte[length];
			int red = this.oggStream.read(by);
			return new SoundBuffer(by, red, samplerate, audioFormat);
		}
		catch (IOException e) {Logger.error(e, this.getClass());}
		return null;
	}

	@Override
	public void quit() 
	{
		try 
		{
			this.oggStream.close();
		} catch (IOException e) {Logger.error(e, this.getClass());}
	}
	@Override
	public SoundBuffer readAll() {
		return readChunk(oggStream.getLength());
	}
	@Override
	public boolean isStreamOver() {
		return oggStream.atEnd();
	}
}