package custom.osu.gui;

/**
 * Created by utilisateur on 23/11/2017.
 */

public abstract class Bouton
{
    public final int id;
    public Bouton(int id)
    {
        this.id = id;
    }
    public abstract boolean isIn(int x, int y);
    public abstract void draw();
    public abstract void touch(int x, int y, int event);
}
