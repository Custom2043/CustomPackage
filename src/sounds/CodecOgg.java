package sounds;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.openal.AL10;
import org.newdawn.slick.openal.OggInputStream;

import util.Logger;

public class CodecOgg extends Codec
{
	final String path;
	protected OggInputStream oggStream;
	public CodecOgg(String s) throws IOException
	{
		this.path = s;
		this.oggStream = new OggInputStream(this.openFile(this.path));
		this.samplerate = this.oggStream.getRate();
		this.audioFormat = this.oggStream.getChannels() == 1 ? AL10.AL_FORMAT_MONO16 : AL10.AL_FORMAT_STEREO16;
		this.bufferSize = this.samplerate * 2 * (this.audioFormat == AL10.AL_FORMAT_MONO16 ? 1 : 2);
	}
	@Override
	public ByteBuffer read(int length) {
		try {
			byte[] by = new byte[length];

			int red = this.oggStream.read(by);

			ByteBuffer b;

			if (red > -1)
			{
				b = ByteBuffer.allocateDirect(red);
				b.put(by, 0, red);
				b.flip();
			}
			else
				b = ByteBuffer.allocateDirect(0);
			return b;
		}
		catch (IOException e) {Logger.error(e, this.getClass());}
		return null;
	}

	@Override
	public void quit() {
		try {
			this.oggStream.close();
		} catch (IOException e) {
			Logger.error(e, this.getClass());
		}
	}
	FileInputStream openFile(String s)
	{
		try {return new FileInputStream(s);}
		catch (IOException e) {Logger.error(e, this.getClass());return null;}
	}
	@Override
	public void reset() {
		try {this.oggStream.close();
		this.oggStream = new OggInputStream(this.openFile(this.path));}
		catch (IOException e) {Logger.error(e, this.getClass());}
	}

}