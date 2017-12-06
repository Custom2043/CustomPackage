package custom.osu.gui;

import custom.osu.main.CustomTimer;
import custom.osu.util.TimeShift;

import static custom.osu.drawer.CustomDrawer.alphatexturedShader;

public abstract class SlidingBouton extends Bouton
{
    public float endX, endY, endAlpha, baseX, baseY, baseAlpha = 1, baseSize = 1, endSize;

    public TimeShift shift = null;
    public SlidingBouton(int id)
    {
        super(id);
    }
    public void addSlide(int s, int d, float toAlpha, boolean sm)
    {
        addSlide(s, d, getX(), getY(), toAlpha, getSize(), sm);
    }
    public void addSlide(int s, int d, float toX, float toY, float toAlpha, float toSize, boolean sm)
    {
        baseX = getX();
        baseY = getY();
        baseAlpha = getAlpha();
        baseSize = getSize();
        endX = toX; endY = toY; endAlpha = toAlpha; endSize = toSize;
        shift = new TimeShift(s, d, sm);
    }
    public void draw()
    {
        if (shift != null && shift.isOver()) {
            shift = null;
            baseX = endX;
            baseY = endY;
            baseSize = endSize;
            baseAlpha = endAlpha;
        }
        alphatexturedShader.start();
        alphatexturedShader.loadAlpha(getAlpha());
        alphatexturedShader.loadX(getX());
        alphatexturedShader.loadY(getY());
        alphatexturedShader.loadGui(getSize());
    }
    public void touch(int x, int y, int event) {}
    public CustomTimer timer = new CustomTimer();
    public float getX()
    {
        if (shift == null) return baseX;
        return baseX + (endX - baseX) * shift.getRatio();
    }
    public float getY()
    {
        if (shift == null) return baseY;
        return baseY + (endY - baseY) * shift.getRatio();
    }
    public float getAlpha()
    {
        if (shift == null) return baseAlpha;
        return baseAlpha + (endAlpha - baseAlpha) * shift.getRatio();
    }
    public float getSize()
    {
        if (shift == null) return baseSize;
        return baseSize + (endSize - baseSize) * shift.getRatio();
    }
}
