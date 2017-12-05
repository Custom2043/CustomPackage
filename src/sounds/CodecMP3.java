package sounds;

import java.io.File;
import java.net.URL;
import java.nio.ByteBuffer;

import org.lwjgl.openal.AL10;

import de.cuina.fireandfuel.CodecJLayerMP3;
import de.cuina.fireandfuel.SoundBuffer;

public class CodecMP3 extends Codec
{
	CodecJLayerMP3 gne = new CodecJLayerMP3();
	URL u;
	public CodecMP3(String s)
	{
		try
		{
			u = new File(s).toURI().toURL();
			gne.initialize(u);
		}catch(Exception e){e.printStackTrace();}
		this.audioFormat = AL10.AL_FORMAT_STEREO16;
		this.samplerate = 44100;
	}
	public ByteBuffer read(int length) 
	{
		SoundBuffer b = gne.read();
		if (b == null)
			return ByteBuffer.allocateDirect(0);
		ByteBuffer bb = ByteBuffer.allocateDirect(b.audioData.length);
		bb.put(b.audioData);
		bb.flip();
		return bb;
	}
	
	public ByteBuffer readAll() 
	{
		SoundBuffer b = gne.readAll();
		if (b == null)
			return ByteBuffer.allocateDirect(0);
		ByteBuffer bb = ByteBuffer.allocateDirect(b.audioData.length);
		bb.put(b.audioData);
		bb.flip();
		return bb;
	}
	
	public void quit() 
	{
		gne.cleanup();
	}

	public void reset()
	{
		gne.initialize(u);
	}

}
