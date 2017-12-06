package custom.osu.gui;

import custom.osu.drawer.CustomDrawer;
import custom.osu.drawer.TexturedSCModel;
import custom.osu.drawer.ShaderProgram;
import custom.osu.drawer.Texture;
import custom.osu.util.QuadColor;
import custom.osu.util.ScreenCoor;
import custom.osu.util.TextureCoor;

import static custom.osu.drawer.CustomDrawer.cuttexturedShader;

public class RectSlidingBouton extends SlidingBouton
{
    public static boolean draw = false;
    public final ScreenCoor coor;
    public final TexturedSCModel model;
    public RectSlidingBouton(int id, ScreenCoor c, TextureCoor tc, Texture t)
    {
        super(id);
        coor = c;
        model = new TexturedSCModel(c, new QuadColor(), tc, t);
        this.baseAlpha = 0;
    }

    @Override
    public boolean isIn(int x, int y)
    {
        float xx = getX(), yy = getY();
        return coor.getStartX() + xx < x &&
               coor.getEndX() + xx > x &&
               coor.getStartY() + yy < y &&
               coor.getEndY() + yy > y;
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
        if (draw){
        cuttexturedShader.start();
        cuttexturedShader.loadAlpha(getAlpha());
        cuttexturedShader.loadX(getX());
        cuttexturedShader.loadY(getY());//MainActivity.getWidth() / 2);
        cuttexturedShader.loadGui(getSize());
        CustomDrawer.drawModel(model);
        ShaderProgram.stop();}
    }
}
