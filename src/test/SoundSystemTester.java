package test;

import sounds.CodecOgg;
import sounds.SoundSystem;
import util.Logger;

public class SoundSystemTester {

	public static int sourceId;
	public static void main(String[] args) throws InterruptedException
	{
		SoundSystem.init();

		SoundSystem.setDefaultCodec(CodecOgg.class);

		Logger.setLoggerProperties(true, true, true, true);

		sourceId = SoundSystem.newSource(false,  false, "Sans titre.ogg", true);

		SoundSystem.play(sourceId);

		Thread.sleep(9000);

		SoundSystem.quit();
	}
}
