package custom.osu.gui;

import java.util.ArrayList;

import custom.osu.main.CustomTimer;

public abstract class Gui
{
    public ArrayList<Bouton> boutons = new ArrayList<>();

    public int lastX, lastY, firstX, firstY;
    public boolean touching = false;
    public boolean moved;
    private CustomTimer clickTimer = new CustomTimer();
    public abstract void drawBeforeButtons();
    public void draw()
    {
        drawBeforeButtons();
        drawButtons();
    }
    public void drawButtons()
    {
        for (Bouton b : boutons)
            b.draw();
    }
    // Event : 0 = touch down, 1 = up, 2 = move
    public void touchScreen(int x, int y, int event)
    {
        if (event == 0) {
            touching = true; moved = false;clickTimer.set0();
            firstX = x; firstY = y;
        }
        if (event == 1)
            touching = false;
        if (event == 2)
            moved = true;
        lastX = x;
        lastY = y;
        for (Bouton b : boutons)
            b.touch(x, y, event);
        touch(x, y, event);
    }
    public abstract void touch(int x, int y, int event);
    public void quit(){}
    public <T extends Bouton> T addBouton(T b){boutons.add(b);return b;}
    public boolean isShortClick(){return firstX == lastX && firstY == lastY && clickTimer.getDifference() < 200;}
}
