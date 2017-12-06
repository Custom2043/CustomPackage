package custom.osu.audio;

public class Fading
{
    public final int duration, player;
    public final float baseVolumeL, baseVolumeR, endVolumeL, endVolumeR;
    public int remaining;

    public Fading(int p, int d, float b, float e)
    {
        this(p, d, b, b, e, e);
    }
    public Fading(int p, int d, float bL, float bR, float eL, float eR)
    {
        player = p;
        remaining = duration = d;
        baseVolumeL = bL;
        baseVolumeR = bR;
        endVolumeL = eL;
        endVolumeR = eR;
    }
}
