package audio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.vector.Vector3f;

import util.InputStreamSource;
import util.Logger;
import util.TimeShift;

public class AudioSystem
{
	public static int NO_ATTE = 0, LINEAR_ATTE = 1, LINEAR_ATTE_CLAMPED = 2, EXPO_ATTE = 3,
					  EXPO_ATTE_CLAMPED = 4, INVERSE_ATTE = 5, INVERSE_ATTE_CLAMPED = 6,
					  BYTE_OFFSET = 7, SAMPLE_OFFSET = 8, SECOND_OFFSET = 9;

	static float masterVolume = 1;

	static CommandThread commands;

	static final int NB_STREAMING_SOURCES = 64;
	static final int NB_NONSTREAMING_SOURCES = 64;

	static StreamingSource[] streamingSourcesPlaying = new StreamingSource[NB_STREAMING_SOURCES];
	static SoundSource[] nonStreamingSourcesPlaying = new SoundSource[NB_NONSTREAMING_SOURCES];

	static ArrayList<Source> sources= new ArrayList<Source>();
	static ArrayList<Sound> sounds= new ArrayList<Sound>();

	static Class<? extends Codec> defaultCodec = CodecWav.class;
	
	static int defaultStreamingBufferSize = 262144, defaultSoundBufferSize = 2097152;
	static int defaultNumberOfStreamingBuffers = 3;

	static int getFreePos(Source[] s)
	{
		for (int i = 0;i<s.length;i++)
			if (s[i] == null)
				return i;
		return -1;
	}
	static int getNonPrioPos(Source[] s)
	{
		for (int i = 0;i<s.length;i++)
			if (s[i].priority == false)
				return i;
		return -1;
	}
	static Sound getSound(int id)
	{
		for (Sound s : sounds)
			if (s.buffer == id)
				return s;
		return null;
	}
	static Source getSource(int sourceId)
	{
		for (Source s : sources)
			if (s.sourceId == sourceId)
				return s;
		Logger.warning("Unexistent source");
		return null;
	}
	static Source[] getSourcesPlaying(boolean stream)
	{
		return stream ? streamingSourcesPlaying : nonStreamingSourcesPlaying;
	}
	static void deleteFromChannels(int sourceId)
	{
		for (int i=0;i<AudioSystem.NB_NONSTREAMING_SOURCES;i++)
			if (AudioSystem.nonStreamingSourcesPlaying[i] != null)
				if (AudioSystem.nonStreamingSourcesPlaying[i].sourceId == sourceId)
					AudioSystem.nonStreamingSourcesPlaying[i] = null;

		for (int i=0;i<AudioSystem.NB_STREAMING_SOURCES;i++)
			if (AudioSystem.streamingSourcesPlaying[i] != null)
				if (AudioSystem.streamingSourcesPlaying[i].sourceId == sourceId)
					AudioSystem.streamingSourcesPlaying[i] = null;

	}
	public static void deleteSource(int sourceId)
	{
		Command.executeInThread(new Command.CommandDeleteSource(sourceId));
	}
	public static boolean init()
	{
		if (AL.isCreated())
			return true;
		try
		{
			AL.create();
			AL10.alListener3f(AL10.AL_POSITION, 0,0,0);
			AL10.alListener3f(AL10.AL_VELOCITY, 0,0,0);
			commands = new CommandThread();
			commands.start();
			return true;
		}
		catch (LWJGLException e) {Logger.error(e);return false;}
	}
	
	public static int newStreamingSource(InputStream stream, int bufferSize, int bufferNumber, Class<? extends Codec> codec, boolean prior, boolean loop, float x, float y, float z, float maxDistance, float referenceDistance, float rollofFactor)
	{
		return Command.executeInThread(new Command.CommandNewSource(bufferSize, bufferNumber, prior, stream, loop,x,y,z,maxDistance, referenceDistance, rollofFactor, codec)).sourceId;
	}
	public static int newStreamingSource(InputStreamSource stream, int bufferSize, int bufferNumber, Class<? extends Codec> codec, boolean prior, boolean loop, float x, float y, float z, float maxDistance, float referenceDistance, float rollofFactor)
	{
		return Command.executeInThread(new Command.CommandNewSource(bufferSize, bufferNumber, prior, stream, loop,x,y,z,maxDistance, referenceDistance, rollofFactor, codec)).sourceId;
	}
	public static int newStreamingSource(InputStream stream, boolean prior, boolean loop, float x, float y, float z, float maxDistance, float referenceDistance, float rollofFactor)
	{
		return Command.executeInThread(new Command.CommandNewSource(defaultStreamingBufferSize, defaultNumberOfStreamingBuffers, prior, stream, loop,x,y,z,maxDistance, referenceDistance, rollofFactor, defaultCodec)).sourceId;
	}
	public static int newStreamingSource(InputStreamSource stream, boolean prior, boolean loop, float x, float y, float z, float maxDistance, float referenceDistance, float rollofFactor)
	{
		return Command.executeInThread(new Command.CommandNewSource(defaultStreamingBufferSize, defaultNumberOfStreamingBuffers, prior, stream, loop,x,y,z,maxDistance, referenceDistance, rollofFactor, defaultCodec)).sourceId;
	}
	public static int newStreamingSource(InputStream stream, boolean prior, boolean loop)
	{
		return Command.executeInThread(new Command.CommandNewSource(defaultStreamingBufferSize, defaultNumberOfStreamingBuffers, prior, stream, loop,0,0,0,0,0,0,defaultCodec)).sourceId;
	}
	public static int newStreamingSource(InputStreamSource stream, boolean prior, boolean loop)
	{
		return Command.executeInThread(new Command.CommandNewSource(defaultStreamingBufferSize, defaultNumberOfStreamingBuffers, prior, stream, loop,0,0,0,0,0,0,defaultCodec)).sourceId;
	}
	
	public static int newSoundSource(int sound, int bS, boolean prior, boolean loop, float x, float y, float z, float maxDistance, float referenceDistance, float rollofFactor)
	{
		return Command.executeInThread(new Command.CommandNewSource(bS, prior, sound, loop,x,y,z,maxDistance, referenceDistance, rollofFactor, defaultCodec)).sourceId;
	}
	public static int newSoundSource(int sound, boolean prior, boolean loop, float x, float y, float z, float maxDistance, float referenceDistance, float rollofFactor)
	{
		return Command.executeInThread(new Command.CommandNewSource(defaultSoundBufferSize, prior, sound, loop,x,y,z,maxDistance, referenceDistance, rollofFactor, defaultCodec)).sourceId;
	}
	public static int newSoundSource(int sound, boolean prior, boolean loop)
	{
		return Command.executeInThread(new Command.CommandNewSource(defaultSoundBufferSize, prior, sound, loop,0,0,0,0,0,0,defaultCodec)).sourceId;
	}
	
	public static void pause(int sourceId)
	{
		Command.executeInThread(new Command.CommandPause(sourceId));
	}
	public static void play(int sourceId)
	{
		Command.executeInThread(new Command.CommandPlay(sourceId));
	}
	public static boolean playing(int sourceId)
	{
		return Command.executeInThread(new Command.CommandPlaying(sourceId)).playing;
	}
	public static void quit()
	{
		Command.executeInThread(new Command.CommandCleanUp());
		commands.quit();
		AL.destroy();
	}
	public static void setVolume(int sourceId, float volume)
	{
		Command.executeInThread(new Command.CommandFloat(sourceId, AL10.AL_GAIN, volume * masterVolume));
	}
	public static float getVolume(int sourceId)
	{
		return Command.executeInThread(new Command.CommandFloat(sourceId, AL10.AL_GAIN)).value * masterVolume;
	}
	public static void setMasterVolume(float volume)
	{
		Command.executeInThread(new Command.CommandMasterVolume(volume));
	}
	public static float getMasterVolume()
	{
		return Command.executeInThread(new Command.CommandMasterVolume()).volume;
	}
	public static int getDistanceModel()
	{
		return Command.executeInThread(new Command.CommandAttenuationMod()).atte;
	}
	public static void setPitch(int sourceId, float pitch)
	{
		Command.executeInThread(new Command.CommandFloat(sourceId, AL10.AL_PITCH, pitch));
	}
	public static float getPitch(int sourceId)
	{
		return Command.executeInThread(new Command.CommandFloat(sourceId, AL10.AL_PITCH)).value;
	}
	public static void setSourcePosition(int sourceId, Vector3f vec)
	{
		Command.executeInThread(new Command.CommandVector(sourceId, AL10.AL_POSITION, vec));
	}
	public static Vector3f getSourcePosition(int sourceId)
	{
		return Command.executeInThread(new Command.CommandVector(sourceId, AL10.AL_POSITION)).vec;
	}
	public static void setSourceVelocity(int sourceId, Vector3f vec)
	{
		Command.executeInThread(new Command.CommandVector(sourceId, AL10.AL_VELOCITY, vec));
	}
	public static void setAttenationMod(int atteMod)
	{
		Command.executeInThread(new Command.CommandAttenuationMod(atteMod));
	}
	public static int getAttenationMod()
	{
		return Command.executeInThread(new Command.CommandAttenuationMod()).atte;
	}
	public static void setOffset(int sourceId, int offsetMod, int value)
	{
		Command.executeInThread(new Command.CommandPosition(sourceId, offsetMod, value));
	}
	public static int getOffset(int sourceId, int offsetMod)
	{
		return Command.executeInThread(new Command.CommandPosition(sourceId, offsetMod)).value;
	}
	public static Vector3f getSourceVelocity(int sourceId)
	{
		return Command.executeInThread(new Command.CommandVector(sourceId, AL10.AL_VELOCITY)).vec;
	}
	public static void setSourceRoll(int sourceId, float roll)
	{
		Command.executeInThread(new Command.CommandFloat(sourceId, AL10.AL_ROLLOFF_FACTOR, roll));
	}
	public static float getSourceRoll(int sourceId)
	{
		return Command.executeInThread(new Command.CommandFloat(sourceId, AL10.AL_ROLLOFF_FACTOR)).value;
	}
	public static void setSourceReferenceDistance(int sourceId, float referenceDistance)
	{
		Command.executeInThread(new Command.CommandFloat(sourceId, AL10.AL_REFERENCE_DISTANCE, referenceDistance));
	}
	public static float getSourceReferenceDistance(int sourceId)
	{
		return Command.executeInThread(new Command.CommandFloat(sourceId, AL10.AL_REFERENCE_DISTANCE)).value;
	}
	public static void setSourceMaxDistance(int sourceId, float maxDistance)
	{
		Command.executeInThread(new Command.CommandFloat(sourceId, AL10.AL_MAX_DISTANCE, maxDistance));
	}
	public static float getSourceMaxDistance(int sourceId)
	{
		return Command.executeInThread(new Command.CommandFloat(sourceId, AL10.AL_MAX_DISTANCE)).value;
	}
	public static void stop(int sourceId)
	{
		Command.executeInThread(new Command.CommandStop(sourceId));
	}
	public static int loadSound(String sourceFile)
	{
		return loadSound(getStream(sourceFile));
	}
	public static int loadSound(String sourceFile, Class<? extends Codec> c)
	{
		return loadSound(getStream(sourceFile), c);
	}
	public static int loadSound(InputStream is)
	{
		return loadSound(is, defaultCodec);
	}
	public static int loadSound(InputStream is, Class<? extends Codec> c)
	{
		return Command.executeInThread(new Command.CommandLoadSound(is, c)).buffer;
	}
	public static void preloadStreamingSource(int streamingSource)
	{
		Command.executeInThread(new Command.CommandPreloadStream(streamingSource));
	}
	public static void unloadSound(int id)
	{
		Command.executeInThread(new Command.CommandUnloadSound(id));
	}
	public static void setDefaultCodec(Class<? extends Codec> c)
	{
		defaultCodec = c;
	}
	public static void setLooping(int sourceId, boolean looping)
	{
		Command.executeInThread(new Command.CommandLoop(sourceId, looping));
	}
	public static boolean isLooping(int sourceId)
	{
		return Command.executeInThread(new Command.CommandLoop(sourceId)).value;
	}
	public static void setDefaultBuffers(int maxStreamingBufferSize, int maxSoundBufferSize, int streamingBufferNumber)
	{
		defaultStreamingBufferSize = maxStreamingBufferSize;
		defaultSoundBufferSize = maxSoundBufferSize;
		defaultNumberOfStreamingBuffers = streamingBufferNumber;
	}
	public static int getDefaultStreamingBufferSize()
	{
		return defaultStreamingBufferSize;
	}
	public static int getDefaultSoundBufferSize()
	{
		return defaultSoundBufferSize;
	}
	public static int getDefaultNumberOfBuffers()
	{
		return defaultNumberOfStreamingBuffers;
	}
	public static Class<? extends Codec> getDefaultCodec()
	{
		return defaultCodec;
	}
	//We can't change the buffer size of a sound, since it is already red
	public static void setStreamingBuffersData(int streamingSourceId, int maxStreamingBufferSize, int streamingBufferNumber)
	{
		Source s = getSource(streamingSourceId);
		if (!(s instanceof StreamingSource))
		{	
			Logger.warning("The source "+streamingSourceId+" is not a streaming source");
			return;
		}
		((StreamingSource)s).bufferSize = maxStreamingBufferSize;
		((StreamingSource)s).bufferNumbers = streamingBufferNumber;
	}
	private static FileInputStream getStream(String source)
	{
		try {
			return new FileInputStream(new File(source));
		}catch(IOException e) {Logger.error("The file "+source+" doesn't exist");}
		return null;
	}
	public static void fade(int source, TimeShift shift, float baseVolume, float endVolume)
	{
		commands.fadings.add(new Command.VolumeFading(source, baseVolume, endVolume, shift));
	}
}
