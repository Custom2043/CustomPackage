package sounds;

class StreamingSource extends Source
{
	int[] buffers = new int[3];
	Codec codec;
	boolean loop;
	StreamingSource(Codec c, int s, boolean p, boolean l, float x, float y,float z,float mD, float rD, float roll)
	{
		super(s, p, l, x, y, z, mD, rD, roll);
		this.codec = c;
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
