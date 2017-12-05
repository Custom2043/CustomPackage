package sounds;

import java.io.InputStream;

abstract class Codec
{
	public InputStream stream;
	public Codec(InputStream is)
	{
		stream = is;
	}
	// Codecs must implements an constructor that takes an input stream
	// and needs to write stream, samplerate and audioFormat
	/**
	 * Used by streaming source
	 */
	public abstract SoundBuffer readChunk(int chunkSize);
	/**
	 * Used by sounds
	 */
	public abstract SoundBuffer readAll();
	public abstract boolean isStreamOver();
	/**
	 * Stream is automatically closed
	 */
	public abstract void quit();
}
