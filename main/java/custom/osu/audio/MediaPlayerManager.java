package custom.osu.audio;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.Iterator;

import custom.osu.main.MainActivity;

public class MediaPlayerManager
{
    public static boolean infoEnabled = true;
    private static ArrayList<MediaPlayer> players = new ArrayList<MediaPlayer>();
    private static ArrayList<Fading> fadings = new ArrayList<Fading>();
    public static int create(String filename){return create(filename, false);}
    public static int create(String filename, boolean streaming)
    {
        try
        {
            AssetFileDescriptor afd = MainActivity.getFileDescriptor(filename);
            if (afd == null)
                return -1;
            printInfo("Correctly created music from "+filename);
            MediaPlayer mp = new MediaPlayer();
            mp.setOnInfoListener(null);
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mp.prepare();
            players.add(mp);
            return players.size() - 1;
        } catch (Exception ex) {}
        return -1;
    }
    public static void play(int id)
    {
        if (players.get(id).isPlaying())
            players.get(id).seekTo(0);
        else
            players.get(id).start();
        printInfo("Playing player "+id);
    }
    public static void pause(int id)
    {
        players.get(id).pause();
        printInfo("Pausing player "+id);
    }
    public static void stop(int id)
    {
        players.get(id).stop();
        players.get(id).release();
        printInfo("Stop and releasing player "+id);
    }
    public static boolean isPlaying(int id)
    {
        return players.get(id).isPlaying();
    }
    public static void printInfo(String s)
    {
        if (infoEnabled)
            System.out.println(s);
    }
    public static void addFading(Fading f)
    {
        fadings.add(f);
        players.get(f.player).setVolume(f.baseVolumeL, f.baseVolumeR);
    }
    public static void setPosition(int player, int position)
    {
        players.get(player).seekTo(position);
    }
    public static void update()
    {
        for (Fading f : fadings)
        {
            f.remaining -= MainActivity.getDif();
            if (f.remaining < 0)
                f.remaining = 0;
            players.get(f.player).setVolume(f.endVolumeL + (f.baseVolumeL - f.endVolumeL) * (((float)f.remaining) / f.duration),
                    f.endVolumeR + (f.baseVolumeR - f.endVolumeR) * (((float)f.remaining) / f.duration));
        }
        for (Iterator<Fading> iter = fadings.iterator(); iter.hasNext();)
            if (iter.next().remaining == 0)
                iter.remove();
    }
}