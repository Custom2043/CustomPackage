package custom.osu.gui;

import custom.osu.drawer.TexturedSCModel;
import custom.osu.drawer.ShaderProgram;
import custom.osu.main.MainActivity;
import custom.osu.util.QuadColor;
import custom.osu.util.ScreenCoor;
import custom.osu.util.TextureCoor;

import static custom.osu.drawer.CustomDrawer.drawModel;
import static custom.osu.drawer.CustomDrawer.logo;
import static custom.osu.main.MainActivity.getHeight;
import static custom.osu.main.MainActivity.getWidth;

public class LogoBouton extends SlidingBouton
{
    private static TexturedSCModel logoModel = new TexturedSCModel(ScreenCoor.flat(.5f * getWidth() - .375f * getHeight(), .125f * getHeight(), .75f* getHeight(), .75f * getHeight()), new QuadColor(), TextureCoor.allPicture, logo);
    public LogoBouton(int id)
    {
        super(id);
    }
    public void draw()
    {
        super.draw();
        drawModel(logoModel);
        ShaderProgram.stop();
    }
    public boolean isIn(int x, int y)
    {
        float xx = x - .5f * MainActivity.getWidth() - getX();
        float yy = y - .5f * MainActivity.getHeight() - getY();
        return xx * xx + yy * yy < 0.37f * .37f * MainActivity.getHeight() * MainActivity.getHeight();
    }
}
