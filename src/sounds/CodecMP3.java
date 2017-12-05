package sounds;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.lwjgl.openal.AL10;

import javazoom.mp3spi.DecodedMpegAudioInputStream;

public class CodecMP3 extends Codec
{
	private boolean endOfStream = false;
	private DecodedMpegAudioInputStream myAudioInputStream = null;
	private int channels, sampleSize, frameLength, samplerate, alFormat;

	public CodecMP3(InputStream is)
	{
		super(is);
		myAudioInputStream = DecodedMpegAudioInputStream.toFileFormat(new BufferedInputStream(is));
		channels = myAudioInputStream.getHeader().mode() < 3 ? 2 : 1;
		frameLength = (int) myAudioInputStream.getFrameLength();
		sampleSize = 16;
		samplerate = myAudioInputStream.getHeader().frequency();
		alFormat = channels == 2 ? AL10.AL_FORMAT_STEREO16 : AL10.AL_FORMAT_MONO16;
	}

	public SoundBuffer readChunk(int length)
	{
		int bytesRead = 0, cnt = 0;

		byte[] streamBuffer = new byte[length];

		try
		{
			while (!endOfStream && bytesRead < streamBuffer.length)
			{
				myAudioInputStream.execute();
				if ((cnt = myAudioInputStream.read(streamBuffer, bytesRead, streamBuffer.length - bytesRead)) <= 0)
				{
					endOfStream = true;
					break;
				}
				bytesRead += cnt;
			}
		}
		catch (Exception ioe)
		{
			endOfStream = true;
			return null;
		}

		if (bytesRead <= 0)
		{
			endOfStream = true;
			return null;
		}

		return new SoundBuffer(streamBuffer, streamBuffer.length, samplerate, alFormat);
	}

	public SoundBuffer readAll()
	{
		byte[] fullBuffer = null;
		int bytesRead = 0, cnt = 0, fileSize = channels * frameLength * sampleSize / 8;
		if (fileSize > 0)
		{
			fullBuffer = new byte[Math.min(fileSize, SoundSystem.defaultSoundBufferSize)];
			try
			{
				while ((cnt = myAudioInputStream.read(fullBuffer, bytesRead, fullBuffer.length - bytesRead)) != -1 && bytesRead < fullBuffer.length)
					bytesRead += cnt;
			}
			catch (IOException e)
			{
				return null;
			}
		}
		else
		{
			fullBuffer = new byte[SoundSystem.defaultSoundBufferSize];
			while (!endOfStream && bytesRead < fullBuffer.length)
			{
				cnt = 0;
				try
				{
					myAudioInputStream.execute();
					if ((cnt = myAudioInputStream.read(fullBuffer, bytesRead, fullBuffer.length - bytesRead)) <= 0)
					{
						endOfStream = true;
						break;
					}
					bytesRead += cnt;
				}
				catch (IOException e)
				{
					return null;
				}
			}
		}
		return new SoundBuffer(fullBuffer, bytesRead, samplerate, alFormat);
	}

	public boolean isStreamOver()
	{
		return endOfStream;
	}

	public void quit()
	{
		try
		{
			myAudioInputStream.close();
		}
		catch (Exception e){}
	}
}