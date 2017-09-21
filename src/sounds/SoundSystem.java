package sounds;

import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.vector.Vector3f;

import util.Logger;

public class SoundSystem
{
	public static int noAtte = 0, linearAtte = 1, linearAtteClamped = 2, expoAtte = 3,
					  expoAtteClamped = 4, inverseAtte = 5, inverseAtteClamped = 6,
					  byteOffset = 7, sampleOffset = 8, secondOffset = 9;

	static float masterVolume = 1;

	static CommandThread commands;

	static final int NB_STREAMING_SOURCES = 4;
	static final int NB_NONSTREAMING_SOURCES = 28;

	static StreamingSource[] streamingSourcesPlaying = new StreamingSource[NB_STREAMING_SOURCES];
	static SoundSource[] nonStreamingSourcesPlaying = new SoundSource[NB_NONSTREAMING_SOURCES];

	static ArrayList<Source> sources= new ArrayList<Source>();
	static ArrayList<Sound> sounds= new ArrayList<Sound>();

	static Class<? extends Codec> defaultCodec = CodecWav.class;

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
	static Sound getSound(String soundPath)
	{
		for (Sound s : sounds)
			if (s.path.equals(soundPath))
				return s;
		return null;
	}
	static Source getSource(int sourceId)
	{
		for (Source s : sources)
			if (s.sourceId == sourceId)
				return s;
		Logger.warning("Unexistent source", SoundSystem.class);
		return null;
	}
	static Source[] getSourcesPlaying(boolean stream)
	{
		return stream ? streamingSourcesPlaying : nonStreamingSourcesPlaying;
	}
	static void deleteFromChannels(int sourceId)
	{
		for (int i=0;i<SoundSystem.NB_NONSTREAMING_SOURCES;i++)
			if (SoundSystem.nonStreamingSourcesPlaying[i] != null)
				if (SoundSystem.nonStreamingSourcesPlaying[i].sourceId == sourceId)
					SoundSystem.nonStreamingSourcesPlaying[i] = null;

		for (int i=0;i<SoundSystem.NB_STREAMING_SOURCES;i++)
			if (SoundSystem.streamingSourcesPlaying[i] != null)
				if (SoundSystem.streamingSourcesPlaying[i].sourceId == sourceId)
					SoundSystem.streamingSourcesPlaying[i] = null;

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
		catch (LWJGLException e) {Logger.error(e, SoundSystem.class);return false;}
	}
	public static int newSource(boolean prior, boolean streaming, String soundPath, boolean loop, float x, float y, float z, float maxDistance, float referenceDistance, float rollofFactor)
	{
		return Command.executeInThread(new Command.CommandNewSource(prior, streaming, soundPath, loop,x,y,z,maxDistance, referenceDistance, rollofFactor, defaultCodec)).sourceId;
	}
	public static int newSource(boolean prior, boolean streaming, String soundPath, boolean loop)
	{
		return Command.executeInThread(new Command.CommandNewSource(prior, streaming, soundPath, loop,0,0,0,0,0,0,defaultCodec)).sourceId;
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
	public static void unloadSound(String soundPath)
	{
		Command.executeInThread(new Command.CommandUnloadSound(soundPath));
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
}
