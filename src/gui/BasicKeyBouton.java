package gui;

import org.newdawn.slick.Color;

import util.ScreenCoor;
import util.Translator;
import value.KeyValue;
import drawer.CustomDrawer;

public class BasicKeyBouton extends CustomKeyBouton
{
	public BasicKeyBouton(String n, int i, ScreenCoor c, KeyValue k)
	{
		super(n, i, c, k);
	}
	@Override
	public void draw()
	{
		CustomDrawer.drawRect(this.coor, this.isActiv() ? Color.white : Color.gray);
		CustomDrawer.drawString(this.coor.getMiddleX(), this.coor.getMiddleY(), true, true, Translator.translate(this.key.nom)+" : "+this.key.getKeyName(), BasicBouton.font, Color.black);
	}
}
