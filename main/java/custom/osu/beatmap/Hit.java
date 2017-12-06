package custom.osu.beatmap;

public class Hit
{
    public static int width, height, size;
    private int x, y;
    public int remainingTime, color, startTime, endTime, duration;
    public boolean touched = false;
    public Hit(int xx, int yy, int d, int sT, int c)
    {
        x = xx; y = yy; duration = d; startTime = sT; color = c;
        endTime = startTime + duration;
        remainingTime = duration;
    }

    public int getX()
    {
        return (int)(y / 384d * (width - size)) + size/2;
    }
    public int getY()
    {
        return (int)(height / 2 + ((x - 256) / 384d * (width - size) + size / 4));
    }
}
