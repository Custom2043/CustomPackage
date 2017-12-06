package audio;

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
	 * MUST RETURN A SAMPLESIZE - LENGTH BUFFER
	 */
	public abstract AudioBuffer readChunk(int chunkSize);
	/**
	 * Used by sounds
	 * MUST RETURN A SAMPLESIZE - LENGTH BUFFER
	 */
	public abstract AudioBuffer readAll();
	public abstract boolean isStreamOver();
	/**
	 * Stream is automatically closed
	 */
	public abstract void quit();
}
