package custom.osu.util;

/**
 * Created by utilisateur on 16/11/2017.
 */

public class Color
{
    public static final Color WHITE = new Color(255, 255, 255),
                              RED = new Color(255, 0, 0),
                              GREEN = new Color(0, 255, 0),
                              BLUE = new Color(0, 0, 255),
                              NOALPHA = new Color(0, 0, 0, 0);


    public int color;
    public Color(int c)
    {
        color = c;
    }
    public Color(int r, int g, int b)
    {
        this(r, g, b, 255);
    }
    public Color(int r, int g, int b, int a)
    {
        color = (r & 255) << 24;
        color += (g & 255) << 16;
        color += (b & 255) << 8;
        color += a & 255;
    }
    public byte getRed() {return (byte)(color >> 24);}
    public byte getGreen() {return (byte)((color >> 16) & 255);}
    public byte getBlue() {return (byte)((color >> 8) & 255);}
    public byte getAlpha() {return (byte)(color & 255);}
}
