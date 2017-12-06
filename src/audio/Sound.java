package audio;

import org.lwjgl.openal.AL10;

import util.Logger;

class Sound
{
	final int buffer;
	Sound(Codec c)
	{
		this.buffer = AL10.alGenBuffers();
		Logger.debug("Load sound "+this.buffer);
		AudioBuffer sb = c.readAll();
		AL10.alBufferData(this.buffer, sb.audioFormat, sb.toByteBuffer(), sb.samplerate);
		c.quit();
	}
}
