package sounds;

import util.InputStreamSource;

class StreamingSource extends Source
{
	int[] buffers = new int[3];
	final Codec codec;
	boolean loop;
	int bufferNumbers, bufferSize;
	InputStreamSource source;
	StreamingSource(Codec c, InputStreamSource so,  int bS, int bN, int s, boolean p, boolean l, float x, float y,float z,float mD, float rD, float roll)
	{
		super(s, p, l, x, y, z, mD, rD, roll);
		source = so;
		this.codec = c;
		this.bufferNumbers = bN;
		this.bufferSize = bS;
	}
	@Override
	void setLooping(boolean l) {
		this.loop = l;
	}
	@Override
	boolean isLooping() {
		return this.loop;
	}
}
