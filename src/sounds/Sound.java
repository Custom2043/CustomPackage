package sounds;

import org.lwjgl.openal.AL10;

import util.Logger;

class Sound
{
	final int buffer;
	final String path;
	Sound(Codec c, String p)
	{
		this.path = p;

		this.buffer = AL10.alGenBuffers();
		Logger.debug("Load sound "+this.buffer, this.getClass());
		AL10.alBufferData(this.buffer, c.audioFormat, c.readAll(), c.samplerate);
		c.quit();

	}
}
