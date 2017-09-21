package gui;

import org.newdawn.slick.Color;

import util.ScreenCoor;
import value.SliderValue;
import drawer.CustomDrawer;

public class BasicSlider extends CustomSlider
{
	public BasicSlider(String n, int i, ScreenCoor c, SliderValue b)
	{
		super(n, i, c, b);
	}
	@Override
	public void draw()
	{
		CustomDrawer.drawRect(this.coor.addYFlat((int)this.coor.getHeight()*.4f).setH(0, 0, (int)this.coor.getHeight()*.2f), Color.white);
		CustomDrawer.drawRect(this.coor.addXFlat((this.getPos() - 0.05f) * this.coor.getWidth()).setW(0,0,(int)this.coor.getWidth()*.1f), Color.white);
		String nom = this.nom + " " + String.valueOf(this.barre.value);
		BasicBouton.font.drawString((int)(this.coor.getStartX() - 0.05f * this.coor.getWidth() - BasicBouton.font.getWidth(nom) - 5), (int)(this.coor.getMiddleY() - BasicBouton.font.getHeight(nom)/2), nom, Color.white);
	}
}
