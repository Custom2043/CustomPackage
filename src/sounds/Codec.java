package sounds;

import java.nio.ByteBuffer;

public abstract class Codec 
{
	public int samplerate,audioFormat, bufferSize;
	public ByteBuffer read() {
		return this.read(this.bufferSize);
	}
	public abstract ByteBuffer read(int length);
	public abstract void quit();
	/**
	 * Reads up to 2M
	 */
	public ByteBuffer readAll() {
		return this.read(20097152);
	}
	public abstract void reset();
}
