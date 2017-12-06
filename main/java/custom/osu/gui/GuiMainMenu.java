package custom.osu.gui;

import custom.osu.audio.Fading;
import custom.osu.audio.MediaPlayerManager;
import custom.osu.drawer.LogoShader;
import custom.osu.drawer.ShaderProgram;
import custom.osu.drawer.TexturedSCModel;
import custom.osu.main.CustomTimer;
import custom.osu.main.MainActivity;
import custom.osu.util.Color;
import custom.osu.util.QuadColor;
import custom.osu.util.ScreenCoor;
import custom.osu.util.TextureCoor;
import custom.osu.util.TimeShift;

import static custom.osu.drawer.CustomDrawer.*;

public class GuiMainMenu extends Gui
{
    private int player =  MediaPlayerManager.create("sounds/demo.mp3");
    private int playerWelcome =  MediaPlayerManager.create("sounds/welcome.mp3");
    private int tong =  MediaPlayerManager.create("sounds/menuhit.mp3");

    private boolean firstStart;
    private CustomTimer timer = new CustomTimer(), lastTouch = new CustomTimer();
    private TimeShift bgShift = new TimeShift(2100, 500, false);
    private static TexturedSCModel background = new TexturedSCModel(ScreenCoor.AllScreen, new QuadColor(), TextureCoor.scaleToScreen(bg), bg),
                                   welcome = new TexturedSCModel(ScreenCoor.screenGui(.5f, .5f, 0, 0, -376f/2, -78f/2, 375, 78), new QuadColor(), TextureCoor.allPicture, welcomeText);
    private RectSlidingBouton playBouton = this.addBouton(new RectSlidingBouton(1, ScreenCoor.screenGui(.375f, .5f, 0, 0, -583f/2, -390f/2, 583f, 89f), TextureCoor.allPicture, play)),
                              editBouton = this.addBouton(new RectSlidingBouton(1, ScreenCoor.screenGui(.375f, .5f, 0, 0, -583f/2, -190f/2, 583f, 89f), TextureCoor.allPicture, edit)),
                              optionsBouton = this.addBouton(new RectSlidingBouton(1, ScreenCoor.screenGui(.375f, .5f, 0, 0, -583f/2, 10f/2, 583f, 89f), TextureCoor.allPicture, options)),
                              exitBouton = this.addBouton(new RectSlidingBouton(1, ScreenCoor.screenGui(.375f, .5f, 0, 0, -583f/2, 210f/2, 583f, 89f), TextureCoor.allPicture, exit));
    private LogoBouton logo = this.addBouton(new LogoBouton(0));

    private static TexturedSCModel[] textModel = fnt.createModel(.5f*MainActivity.getWidth(), .05f*MainActivity.getHeight(), -.5f, -.5f, Color.WHITE, "WESH ALORS");

    int state = 0;
    public void drawBeforeButtons()
    {
        RectSlidingBouton.draw = state >= 0;
        if (MainActivity.firstFrame()) {
            firstStart = true;
            MediaPlayerManager.play(playerWelcome);
            logo.baseAlpha = 0;
            logo.addSlide(2100, 200, 1, false);
            state = -1;
        }
        if (firstStart && timer.getDifference() > 2100)
        {
            MediaPlayerManager.setPosition(player, 6788);
            MediaPlayerManager.addFading(new Fading(player, 200, 0, 1));
            MediaPlayerManager.play(player);
            firstStart = false;
        }
        if (state == -1 && timer.getDifference() > 2300)
            state = 0;
        alphatexturedShader.start();
        alphatexturedShader.loadX(0);
        alphatexturedShader.loadY(0);
        if (firstStart && timer.getDifference() < 2100) // Draw welcome
        {
            if (timer.getDifference() < 500)
                alphatexturedShader.loadAlpha(timer.getDifference() / 500f);
            else if (timer.getDifference() < 1800)
                alphatexturedShader.loadAlpha(1);
            else if (timer.getDifference() < 2100)
                alphatexturedShader.loadAlpha((2100 - timer.getDifference()) / 300f);

            alphatexturedShader.loadGui(timer.getDifference() / 8000f + 1);
            drawModel(welcome);
        }
        alphatexturedShader.loadGui(1);
        if (timer.getDifference() < 2100)
            alphatexturedShader.loadAlpha(0);
        else if (timer.getDifference() < 2600)
            alphatexturedShader.loadAlpha(bgShift.getRatio());
        else
            alphatexturedShader.loadAlpha(1);

        if (state == 1 && lastTouch.getDifference() > 5000) {
            logo.addSlide(0, 1800, 0, 0, 1, 1, true);
            playBouton.addSlide(0, 1800, 0, 0, 0, 1, true);
            editBouton.addSlide(0, 1800, 0, 0, 0, 1, true);
            optionsBouton.addSlide(0, 1800, 0, 0, 0, 1, true);
            exitBouton.addSlide(0, 1800, 0, 0, 0, 1, true);
            state = 0;
        }


        drawModel(background);
        ShaderProgram.stop();

        texturedShader.start();
        for (TexturedSCModel m : textModel)
            if (m != null)
                drawModel(m);
        ShaderProgram.stop();
        logoShader.start();
        drawModel(logoModel);
        logoShader.stop();
    }
    public void touch(int x, int y, int event)
    {
        lastTouch.set0();
        if (event == 1 && !firstStart && logo.isIn(x, y) && this.isShortClick())
        {
            MediaPlayerManager.play(tong);
            if (state == 0) {
                logo.addSlide(0, 350, -.25f * MainActivity.getHeight(), 0, 1, 1, true);
                playBouton.addSlide(0, 500, .45f * MainActivity.getHeight(), 0, 1, 1, true);
                editBouton.addSlide(0, 500, .45f * MainActivity.getHeight(), 0, 1, 1, true);
                optionsBouton.addSlide(0, 500, .45f * MainActivity.getHeight(), 0, 1, 1, true);
                exitBouton.addSlide(0, 500, .45f * MainActivity.getHeight(), 0, 1, 1, true);
                state = 1;
            }
        }
        /*if (event == 1 && !firstStart && editBouton.isIn(x, y) && this.isShortClick())
        {
            MediaPlayerManager.play(tong);
            CustomDrawer.setScreen(new GuiLoad());
        }*/
    }
    public void quit()
    {
        MediaPlayerManager.stop(playerWelcome);
        MediaPlayerManager.stop(player);
        MediaPlayerManager.stop(tong);
    }
}
