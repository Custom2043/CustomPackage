package sounds;

import static sounds.SoundSystem.deleteFromChannels;
import static sounds.SoundSystem.getFreePos;
import static sounds.SoundSystem.getNonPrioPos;
import static sounds.SoundSystem.getSound;
import static sounds.SoundSystem.getSource;
import static sounds.SoundSystem.getSourcesPlaying;
import static sounds.SoundSystem.sounds;
import static sounds.SoundSystem.sources;

import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.Iterator;

import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.util.vector.Vector3f;

import util.InputStreamSource;
import util.Logger;

abstract class Command
{
	private static boolean GET = true,
						   SET = false;
	boolean ready = false, ended = false;

	Command()
	{
	}
	static <Type extends Command> Type executeInThread(Type t)
	{
		SoundSystem.commands.addCommand(t);
		while (!t.ready)
			try {Thread.sleep(0, 10);}
			catch (InterruptedException e) {}
		t.handle();
		t.ended = true;
		return t;
	}
	abstract void handle();
	abstract static class SourceCommand extends Command
	{
		final Source source;
		SourceCommand(int sid)
		{
			this.source = getSource(sid);
		}
	}
	abstract static class GetSetCommand extends SourceCommand
	{
		final boolean get;
		GetSetCommand(boolean g, int sourceId)
		{
			super(sourceId);
			this.get = g;
		}
	}
	static class CommandPlay extends SourceCommand
	{
		CommandPlay(int sid) {super(sid);}
		@Override
		void handle()
		{
			int i = getFreePos(getSourcesPlaying(this.source.streaming()));
			if (i == -1) // Si il n'y a pas de place
				i = getNonPrioPos(getSourcesPlaying(this.source.streaming()));
			if (i != -1) // Si il y a de la place
			{
				if (this.source.streaming() && AL10.alGetSourcei(this.source.sourceId, AL10.AL_BUFFERS_QUEUED) == 0)
				{
					this.source.getStreamingSource().codec.reset();
					StreamingSource s = this.source.getStreamingSource();
					// On charge le début de la musique
					for (int j=0;j<3;j++)
					{
						s.buffers[j] = AL10.alGenBuffers();
						AL10.alBufferData(s.buffers[j] , s.codec.audioFormat, s.codec.read(), s.codec.samplerate);
						AL10.alSourceQueueBuffers(this.source.sourceId, s.buffers[j]);
					}
					Logger.debug("Load sound in stream", this.getClass());
				}
				getSourcesPlaying(this.source.streaming())[i] = this.source;
				AL10.alSourcePlay(this.source.sourceId);
				Logger.debug("Play source "+this.source.sourceId+" in channel "+(this.source.streaming() ?"":"non ")+"streaming channel "+i, this.getClass());
			}
		}
	}
	static class CommandStop extends SourceCommand
	{
		CommandStop(int sid) {super(sid);}
		@Override
		void handle()
		{
			stop(this.source);
		}
		private static void stop(Source s)
		{
			Logger.debug("Stop source "+s.sourceId, Command.CommandStop.class);
			deleteFromChannels(s.sourceId);
			AL10.alSourceStop(s.sourceId);
			if (s.streaming())
			{
				AL10.alSourceUnqueueBuffers(s.sourceId, CommandThread.createIntBuffer(3));
				AL10.alDeleteBuffers(CommandThread.createIntBuffer(3)
											.put(s.getStreamingSource().buffers[0])
											.put(s.getStreamingSource().buffers[1])
											 .put(s.getStreamingSource().buffers[2]));
			}
		}
	}
	static class CommandPause extends SourceCommand
	{
		CommandPause(int sid) {super(sid);}
		@Override
		void handle()
		{
			Logger.debug("Pause source "+this.source.sourceId, this.getClass());
			deleteFromChannels(this.source.sourceId);
			AL10.alSourcePause(this.source.sourceId);

		}
	}
	static class CommandDeleteSource extends SourceCommand
	{
		CommandDeleteSource(int sid) {super(sid);}
		@Override
		void handle()
		{
			deleteSource(this.source, true);
		}
		private static void deleteSource(Source source, boolean remove)
		{
			if (CommandPlaying.playing(source))
				CommandStop.stop(source);

			AL10.alDeleteSources(source.sourceId);//NE SUPPRIMER PAS LES BUFFERS !

			if (remove)
				for (Iterator<Source> iter = SoundSystem.sources.iterator();iter.hasNext();)
					if (iter.next().sourceId == source.sourceId)
						iter.remove();
		}
	}
	static class CommandLoadSound extends Command
	{
		InputStream stream;Class<? extends Codec> codec;
		CommandLoadSound(InputStream is, Class<? extends Codec> c) {this.stream=is;this.codec=c;}
		@Override
		void handle()
		{
			loadSound(stream, codec);
		}
		private static void loadSound(InputStream is, Class<? extends Codec> c)
		{
			try
			{
				Sound s = new Sound(c.getConstructor(String.class).newInstance(is), is);
				sounds.add(s);
			}
			catch (Exception e) {Logger.error(e, CommandLoadSound.class);}
		}
	}
	static class CommandUnloadSound extends Command
	{
		int id;
		CommandUnloadSound(int i) {this.id = i;}
		@Override
		void handle()
		{
			unloadSound(this.id, true);
		}
		private static void unloadSound(int soundId, boolean remove)
		{
			for (Iterator<Source> iter = SoundSystem.sources.iterator();iter.hasNext();)
			{
				Source s = iter.next();
				if (!s.streaming())
					if (s.getSoundSource().sound.buffer == soundId)
					{
						CommandDeleteSource.deleteSource(s, remove);
						if (remove)
							iter.remove();
					}
			}
			for (Iterator<Sound> iter = SoundSystem.sounds.iterator();iter.hasNext();)
			{
				Sound s = iter.next();
				AL10.alDeleteBuffers(s.buffer);
				Logger.debug("Unload sound "+s.buffer, CommandUnloadSound.class);
				if (remove)
					iter.remove();
			}
		}
	}
	static class CommandNewSource extends Command
	{
		int sourceId;
		InputStream stream;boolean prior, loop, streaming;Class<? extends Codec> codec;
		InputStreamSource streamSource;
		float x, y, z, maxD, referD, rolloff;
		int bufferSize = SoundSystem.defaultSoundBufferSize, bufferNumber = SoundSystem.defaultStreamingBufferSize;
		CommandNewSource(boolean p, boolean str, InputStream so, boolean l, float x2, float y2, float z2, float mD, float rD, float rol, Class<? extends Codec> c) {this.stream = so;
		this.prior=p;this.streaming = str;this.codec = c;this.loop = l;this.x=x2;this.y=y2;this.z=z2;this.maxD = mD; this.referD = rD;this.rolloff = rol;}
		
		CommandNewSource(int bS, int bN, boolean p, InputStream so, boolean l, float x2, float y2, float z2, float mD, float rD, float rol, Class<? extends Codec> c) 
		{
			this(p, true, so, l, x2, y2, z2, mD, rD, rol, c);
			bufferSize = bS;
			bufferNumber = bN;
		}
		
		CommandNewSource(boolean p, boolean str, InputStreamSource so, boolean l, float x2, float y2, float z2, float mD, float rD, float rol, Class<? extends Codec> c) {this.streamSource = so;
		this.prior=p;this.streaming = str;this.codec = c;this.loop = l;this.x=x2;this.y=y2;this.z=z2;this.maxD = mD; this.referD = rD;this.rolloff = rol;}
		
		CommandNewSource(int bS, int bN, boolean p, InputStreamSource so, boolean l, float x2, float y2, float z2, float mD, float rD, float rol, Class<? extends Codec> c) 
		{
			this(p, true, so, l, x2, y2, z2, mD, rD, rol, c);
			bufferSize = bS;
			bufferNumber = bN;
		}
		
		@Override
		void handle()
		{
			int sourceId = AL10.alGenSources();
			if ((this.sourceId = AL10.alGetError()) != AL10.AL_NO_ERROR)
				return;
			Source source = null;
			if (this.streaming)
				try
				{
					source = new StreamingSource(this.codec.getConstructor(String.class).newInstance(this.stream), bufferSize, bufferNumber, sourceId, this.prior, this.loop, this.x,this.y,this.z, this.maxD, this.referD, this.rolloff);
				}
				catch (Exception e) {Logger.error(e, this.getClass());}
			else
			{ // Load Sound
				Sound s = getSound(this.stream);

				if (s == null)
					try
					{
						s = new Sound(this.codec.getConstructor(String.class).newInstance(this.stream), this.stream);
						sounds.add(s);
					}
					catch (Exception e) {Logger.error(e, this.getClass());}
				AL10.alSourcei(sourceId, AL10.AL_BUFFER, s.buffer);
				//Logger.debug("End of loading of sound "+s.path+", number of buffer : "+, CommandNewSource.class);
				source = new SoundSource(sourceId, this.prior, s, this.loop, this.x, this.y, this.z, this.maxD, this.referD, this.rolloff);
			}
			sources.add(source);
			this.sourceId = sourceId;
		}
	}
	static class CommandPlaying extends SourceCommand
	{
		boolean playing;
		CommandPlaying(int sid) {super(sid);}
		@Override
		void handle()
		{
			this.playing = playing(this.source);
		}
		private static boolean playing(Source source)
		{
			return AL10.alGetSourcei(source.sourceId, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
		}
	}
	static class CommandCleanUp extends Command
	{
		CommandCleanUp(){}
		@Override
		void handle()
		{
			for (Sound s : sounds) // Unloads sources too
				CommandUnloadSound.unloadSound(s.buffer, false);
		}
	}
	static class CommandFloat extends GetSetCommand
	{
		float value;int dataType;
		CommandFloat(int sourceId, int data, float v){super(SET, sourceId);this.value = v;this.dataType = data;}
		CommandFloat(int sourceId, int data){super(GET, sourceId);this.dataType = data;}
		@Override
		void handle()
		{
			if (this.get)
				this.value = AL10.alGetSourcef(this.source.sourceId, this.dataType);
			else
				AL10.alSourcef(this.source.sourceId, this.dataType, this.value);
		}
	}
	static class CommandLoop extends GetSetCommand
	{
		boolean value;
		CommandLoop(int sourceId, boolean v){super(SET, sourceId);this.value = v;}
		CommandLoop(int sourceId){super(GET, sourceId);}
		@Override
		void handle()
		{
			if (this.get)
				this.value = this.source.isLooping();
			else
				this.source.setLooping(this.value);
		}
	}
	static class CommandVector extends GetSetCommand
	{
		Vector3f vec; int dataType;
		CommandVector(int sourceId, int data, Vector3f p){super(SET, sourceId);this.vec = p; this.dataType = data;}
		CommandVector(int sourceId, int data){super(GET, sourceId);this.dataType = data;}
		@Override
		void handle()
		{
			if (this.get)
			{
				FloatBuffer buf = CommandThread.createFloatBuffer(3);
				AL10.alGetSource(this.source.sourceId, this.dataType, buf);
				this.vec = new Vector3f(buf.get(), buf.get(), buf.get());
			}
			else
				AL10.alSource3f(this.source.sourceId, this.dataType, this.vec.x, this.vec.y, this.vec.z);
		}
	}
	static class CommandMasterVolume extends Command
	{
		float volume;boolean act;
		CommandMasterVolume(float v){this.act = SET;this.volume = v;}
		CommandMasterVolume(){this.act = GET;}
		@Override
		void handle()
		{
			if (this.act == GET)
				this.volume = SoundSystem.masterVolume;
			else
			{
				for (Source s : SoundSystem.sources)
					AL10.alSourcef(s.sourceId, AL10.AL_GAIN, AL10.alGetSourcef(s.sourceId, AL10.AL_GAIN) / SoundSystem.masterVolume * volume);
				SoundSystem.masterVolume = this.volume;
			}
		}
	}
	static class CommandAttenuationMod extends Command
	{
		int atte; boolean act;
		CommandAttenuationMod(){this.act = GET;}
		CommandAttenuationMod(int a){this.act = SET;this.atte = a;}
		@Override
		void handle()
		{
			if (this.act == GET)
				this.atte = AL10.alGetInteger(AL10.AL_DISTANCE_MODEL);
			else
			{
				if (this.atte == SoundSystem.NO_ATTE)
					AL10.alDistanceModel(AL10.AL_NONE);
				if (this.atte == SoundSystem.LINEAR_ATTE)
					AL10.alDistanceModel(AL11.AL_LINEAR_DISTANCE);
				if (this.atte == SoundSystem.LINEAR_ATTE_CLAMPED)
					AL10.alDistanceModel(AL11.AL_LINEAR_DISTANCE_CLAMPED);
				if (this.atte == SoundSystem.EXPO_ATTE)
					AL10.alDistanceModel(AL11.AL_EXPONENT_DISTANCE);
				if (this.atte == SoundSystem.EXPO_ATTE_CLAMPED)
					AL10.alDistanceModel(AL11.AL_EXPONENT_DISTANCE_CLAMPED);
				if (this.atte == SoundSystem.INVERSE_ATTE)
					AL10.alDistanceModel(AL10.AL_INVERSE_DISTANCE);
				if (this.atte == SoundSystem.INVERSE_ATTE_CLAMPED)
					AL10.alDistanceModel(AL10.AL_INVERSE_DISTANCE_CLAMPED);
			}
		}
	}
	static class CommandPosition extends GetSetCommand
	{
		int offsetMod, value;
		CommandPosition(int sourceId, int oM) {super(GET, sourceId);this.offsetMod = oM;}
		CommandPosition(int sourceId, int oM, int v) {super(SET, sourceId);this.offsetMod = oM;this.value=v;}
		@Override
		void handle()
		{
			if (this.get)
			{
				if (this.offsetMod == SoundSystem.BYTE_OFFSET)
					this.value = AL10.alGetSourcei(this.source.sourceId, AL11.AL_BYTE_OFFSET);
				if (this.offsetMod == SoundSystem.SAMPLE_OFFSET)
					this.value = AL10.alGetSourcei(this.source.sourceId, AL11.AL_SAMPLE_OFFSET);
				if (this.offsetMod == SoundSystem.SECOND_OFFSET)
					this.value = AL10.alGetSourcei(this.source.sourceId, AL11.AL_SEC_OFFSET);
			}
			else
			{
				if (this.offsetMod == SoundSystem.BYTE_OFFSET)
					AL10.alSourcei(this.source.sourceId, AL11.AL_BYTE_OFFSET, this.value);
				if (this.offsetMod == SoundSystem.SAMPLE_OFFSET)
					AL10.alSourcei(this.source.sourceId, AL11.AL_SAMPLE_OFFSET, this.value);
				if (this.offsetMod == SoundSystem.SECOND_OFFSET)
					AL10.alSourcei(this.source.sourceId, AL11.AL_SEC_OFFSET, this.value);
			}
		}
	}
}
