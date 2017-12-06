package test;

import audio.AudioSystem;
import audio.CodecMP3;
import util.FileInputStreamSource;
import util.Logger;

public class SoundSystemTester {

	public static int sourceId, soundId;
	public static void main(String[] args) throws InterruptedException
	{
		AudioSystem.init();
		
		AudioSystem.setDefaultCodec(CodecMP3.class);

		Logger.setLoggerProperties(true, true, true, true);

		//soundId = AudioSystem.loadSound("welcome.mp3");
		//sourceId = AudioSystem.newSoundSource(soundId, false, true);
		
		AudioSystem.setDefaultBuffers(65336, 655360, 2);
		sourceId = AudioSystem.newStreamingSource(new FileInputStreamSource("welcome.mp3"), false, true);
		
		AudioSystem.play(sourceId);

		Thread.sleep(9000);

		AudioSystem.quit();
	}
}
