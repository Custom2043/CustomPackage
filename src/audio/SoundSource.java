package audio;

import org.lwjgl.openal.AL10;

class SoundSource extends Source
{
	audio.Sound sound;
	SoundSource(int s, boolean p, audio.Sound so, boolean loop, float x, float y, float z, float mD, float rD, float roll)
	{
		super(s, p, loop, x, y, z, mD, rD, roll);
		this.sound = so;
	}
	@Override
	void setLooping(boolean loop) {
		AL10.alSourcei(this.sourceId, AL10.AL_LOOPING, loop ? 1 : 0);
	}
	@Override
	boolean isLooping() {
		return AL10.alGetSourcei(this.sourceId, AL10.AL_LOOPING) == 1;
	}
}
