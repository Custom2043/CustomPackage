package custom.osu.audio;

import org.lwjgl.openal.AL10;

import util.Logger;

abstract class Source
{
	final int sourceId;
	boolean priority;
	Source(int s, boolean p, boolean loop, float x, float y, float z, float mD, float rD, float roll)
	{
		this.sourceId = s;
		this.priority = p;
		this.setLooping(loop);

		Logger.debug("Load source "+this.sourceId, this.getClass());

		AL10.alSourcef(this.sourceId, AL10.AL_GAIN, 1f);
		AL10.alSourcef(this.sourceId, AL10.AL_PITCH, 1);
		AL10.alSource3f(this.sourceId, AL10.AL_POSITION, x,y,z);

		AL10.alSourcef(this.sourceId, AL10.AL_MAX_DISTANCE, mD);
		AL10.alSourcef(this.sourceId, AL10.AL_REFERENCE_DISTANCE, rD);
		AL10.alSourcef(this.sourceId, AL10.AL_ROLLOFF_FACTOR, roll);
	}
	public boolean streaming()
	{
		return this instanceof StreamingSource;
	}
	public StreamingSource getStreamingSource()
	{
		return (StreamingSource)this;
	}
	public SoundSource getSoundSource()
	{
		return (SoundSource)this;
	}
	abstract void setLooping(boolean loop);
	abstract boolean isLooping();
}
