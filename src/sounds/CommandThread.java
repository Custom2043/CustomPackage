package sounds;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Iterator;

import org.lwjgl.openal.AL10;

import util.Logger;
import util.UpdateList;

class CommandThread extends Thread
{
	private boolean continu;
	public UpdateList<Command> list = new UpdateList<Command>();
	public CommandThread()
	{
		this.continu = true;
	}
	@Override
	public void run()
	{
		while (this.continu || this.list.getList().size() != 0)
		{
			for (int i=0;i<SoundSystem.NB_NONSTREAMING_SOURCES;i++)
				if (SoundSystem.nonStreamingSourcesPlaying[i] != null)
					if (AL10.alGetSourcei(SoundSystem.nonStreamingSourcesPlaying[i].sourceId, AL10.AL_SOURCE_STATE) == AL10.AL_STOPPED)
					{							
						Logger.debug("Found stopped source : "+SoundSystem.nonStreamingSourcesPlaying[i].sourceId, CommandThread.class);
						new Command.CommandStop(SoundSystem.nonStreamingSourcesPlaying[i].sourceId).handle(); // Son fini
					}
			for (int i=0;i<SoundSystem.NB_STREAMING_SOURCES;i++)
				if (SoundSystem.streamingSourcesPlaying[i] != null)
				{
					if (AL10.alGetSourcei(SoundSystem.streamingSourcesPlaying[i].sourceId, AL10.AL_SOURCE_STATE) == AL10.AL_STOPPED && !SoundSystem.streamingSourcesPlaying[i].loop)
					{
						Logger.debug("Found stopped source : "+SoundSystem.streamingSourcesPlaying[i].sourceId, CommandThread.class);
						new Command.CommandStop(SoundSystem.streamingSourcesPlaying[i].sourceId).handle(); // Music finie
					}
					else if (SoundSystem.streamingSourcesPlaying[i].loop || AL10.alGetSourcei(SoundSystem.streamingSourcesPlaying[i].sourceId, AL10.AL_SOURCE_STATE) != AL10.AL_PAUSED)
					{
						int toLoad = AL10.alGetSourcei(SoundSystem.streamingSourcesPlaying[i].sourceId, AL10.AL_BUFFERS_PROCESSED);
						if (toLoad > 1)
							Logger.debug("Load "+toLoad+" buffers for source "+SoundSystem.streamingSourcesPlaying[i].sourceId, CommandThread.class);
						for(int j=0;j<toLoad;j++)
						{
							StreamingSource source = SoundSystem.streamingSourcesPlaying[i];
							ByteBuffer bb = source.codec.read();
							AL10.alSourceUnqueueBuffers(SoundSystem.streamingSourcesPlaying[i].sourceId, createIntBuffer(1));
							AL10.alDeleteBuffers(source.buffers[0]);
							source.buffers[0] = source.buffers[1];
							source.buffers[1] = source.buffers[2];
							if (bb.capacity() == 0 && source.loop)
							{
								source.codec.reset();
								bb = source.codec.read();
							}
							if (bb.capacity() != 0) // Encore des données
							{
								source.buffers[2] = AL10.alGenBuffers();
								AL10.alBufferData(source.buffers[2], source.codec.audioFormat, bb, source.codec.samplerate);
								AL10.alSourceQueueBuffers(SoundSystem.streamingSourcesPlaying[i].sourceId, source.buffers[2]);
							}
						}
						if (AL10.alGetSourcei(SoundSystem.streamingSourcesPlaying[i].sourceId, AL10.AL_SOURCE_STATE) == AL10.AL_STOPPED)
							AL10.alSourcePlay(SoundSystem.streamingSourcesPlaying[i].sourceId);
					}
				}

			for (Iterator<Command> iter = this.list.getList().iterator();iter.hasNext();)
			{
				Command c = iter.next();
				c.ready = true;
				Logger.debug("Execute Command : "+c.getClass().getName(), CommandThread.class);
				while (!c.ended)
					try {Thread.sleep(0, 10);} 
					catch (InterruptedException e) {}
				iter.remove();
			}
			if (this.continu && this.list.getList().size() == 0)
				try {Thread.sleep(10);}
				catch (InterruptedException e) {}
			
		}
	}
	protected static IntBuffer createIntBuffer(int size) {
		ByteBuffer temp = ByteBuffer.allocateDirect(4 * size);
		temp.order(ByteOrder.nativeOrder());
		return temp.asIntBuffer();
	}
	protected static FloatBuffer createFloatBuffer(int size) {
		ByteBuffer temp = ByteBuffer.allocateDirect(4 * size);
		temp.order(ByteOrder.nativeOrder());
		return temp.asFloatBuffer();
	}
	public void quit()
	{
		this.continu = false;
		try {this.join();}
		catch (InterruptedException e) {Logger.error(e, this.getClass());}
	}
	public void addCommand(Command c)
	{
		this.list.add(c);
		this.interrupt();
	}
}
