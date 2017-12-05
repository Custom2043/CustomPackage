package test;

import sounds.*;
import util.Logger;

public class SoundSystemTester {

	public static int sourceId, soundId;
	public static void main(String[] args) throws InterruptedException
	{
		SoundSystem.init();
		
		SoundSystem.setDefaultCodec(CodecMP3.class);

		Logger.setLoggerProperties(true, true, true, true);

		soundId = SoundSystem.loadSound("welcome.mp3");
		sourceId = SoundSystem.newSoundSource(soundId, false, true);
		
		//sourceId = SoundSystem.newStreamingSource(new FileInputStreamSource("welcome.mp3"), false, true);

		SoundSystem.play(sourceId);

		Thread.sleep(9000);

		SoundSystem.quit();
	}
}
