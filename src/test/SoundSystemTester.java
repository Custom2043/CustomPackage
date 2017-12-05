package test;

import sounds.CodecMP3;
import sounds.SoundSystem;
import util.Logger;

public class SoundSystemTester {

	public static int sourceId;
	public static void main(String[] args) throws InterruptedException
	{
		SoundSystem.init();
		
		SoundSystem.setDefaultCodec(CodecMP3.class);

		Logger.setLoggerProperties(true, true, true, true);

		sourceId = SoundSystem.newSource(false,  true, "welcome.mp3", true);

		SoundSystem.play(sourceId);

		Thread.sleep(9000);

		SoundSystem.quit();
	}
}
