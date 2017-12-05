package sounds;

import java.io.InputStream;
import java.nio.ByteBuffer;

public abstract class Codec
{
	public InputStream stream;
	public int samplerate,audioFormat;
	// Codecs must implements an constructor that takes an input stream
	// and needs to write stream, samplerate and audioFormat
	/**
	 * Used when streaming
	 */
	public abstract ByteBuffer readChunk(int chunkSize);
	/**
	 * Used by sounds
	 */
	public abstract ByteBuffer readAll();
	public abstract boolean isStreamOver();
	public abstract void quit();
}
