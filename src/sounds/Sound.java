package sounds;

import java.io.InputStream;

import org.lwjgl.openal.AL10;

import util.Logger;

class Sound
{
	final int buffer;
	final InputStream stream;
	Sound(Codec c, InputStream is)
	{
		stream = is;
		this.buffer = AL10.alGenBuffers();
		Logger.debug("Load sound "+this.buffer, this.getClass());
		AL10.alBufferData(this.buffer, c.audioFormat, c.readAll(), c.samplerate);
		c.quit();
	}
}
