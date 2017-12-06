package audio;

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
		while (this.continu)
		{
			for (int i = 0; i< AudioSystem.NB_NONSTREAMING_SOURCES; i++)
				if (AudioSystem.nonStreamingSourcesPlaying[i] != null)
					if (AL10.alGetSourcei(AudioSystem.nonStreamingSourcesPlaying[i].sourceId, AL10.AL_SOURCE_STATE) == AL10.AL_STOPPED)
					{
						Logger.debug("Found stopped source : "+AudioSystem.nonStreamingSourcesPlaying[i].sourceId);
						new Command.CommandStop(AudioSystem.nonStreamingSourcesPlaying[i].sourceId).handle(); // Son fini
					}
			for (int i=0;i<AudioSystem.NB_STREAMING_SOURCES;i++)
				if (AudioSystem.streamingSourcesPlaying[i] != null)
					if (AL10.alGetSourcei(AudioSystem.streamingSourcesPlaying[i].sourceId, AL10.AL_SOURCE_STATE) == AL10.AL_STOPPED && !AudioSystem.streamingSourcesPlaying[i].loop)
					{
						Logger.debug("Found stopped source : "+AudioSystem.streamingSourcesPlaying[i].sourceId);
						new Command.CommandStop(AudioSystem.streamingSourcesPlaying[i].sourceId).handle(); // Music finie
					}
					else if (AudioSystem.streamingSourcesPlaying[i].loop || AL10.alGetSourcei(AudioSystem.streamingSourcesPlaying[i].sourceId, AL10.AL_SOURCE_STATE) != AL10.AL_PAUSED)
					{
						new Command.CommandPreloadStream(AudioSystem.streamingSourcesPlaying[i].sourceId).handle();
						if (AL10.alGetSourcei(AudioSystem.streamingSourcesPlaying[i].sourceId, AL10.AL_SOURCE_STATE) == AL10.AL_STOPPED)
							AL10.alSourcePlay(AudioSystem.streamingSourcesPlaying[i].sourceId);
					}
			for (Iterator<Command> iter = this.list.getList().iterator();iter.hasNext();)
			{
				Command c = iter.next();
				if (c.ready) // the command has been thrown
				{
					c.handle();
					iter.remove();
				}
				else
				{
					c.ready = true;
					Logger.debug("Execute Command : "+c.getClass().getName());
					while (!c.ended) // Wait end of the command
						try {Thread.sleep(0, 10);}
						catch (InterruptedException e) {}
					iter.remove();
				}
			}
			if (this.continu && this.list.getList().size() == 0)
				try {Thread.sleep(1);}
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
		catch (InterruptedException e) {Logger.error(e);}
	}
	public void addCommand(Command c)
	{
		this.list.add(c);
		this.interrupt();
	}
}
