package sounds;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

class SoundBuffer 
{
	final byte[] audioData;
	final int samplerate, audioFormat, untilData;
	
	SoundBuffer(byte[] datas, int until,  int rate, int format)
	{
		audioData = datas;
		samplerate = rate;
		audioFormat = format;
		untilData = until;
	}
	
	ByteBuffer toByteBuffer()
	{
		ByteBuffer buf = ByteBuffer.allocateDirect(untilData);
		buf.order(ByteOrder.nativeOrder());
		buf.put(audioData, 0, untilData);
		buf.flip();
		
		return buf;
	}
}
