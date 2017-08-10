package gui;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;

import util.Logger;
import util.ScreenCoor;
import drawer.CustomDrawer;

public class BasicBouton extends CustomBouton
{
	public static AngelCodeFont font;
	static
	{
		try {
			font = new AngelCodeFont("Tahoma_18.fnt","Tahoma_18.png");} 
		catch (SlickException e) {Logger.error(e, BasicBouton.class);}
	}
	public BasicBouton(String n, int i, ScreenCoor c, boolean a) 
	{
		super(n, i, c, a);
	}

	@Override
	protected void draw() 
	{
		CustomDrawer.drawRect(this.coor, isActiv() ? Color.white : Color.gray);
		font.drawString((int)(this.coor.getMiddleX() - font.getWidth(this.nom)/2), (int)(this.coor.getMiddleY() - font.getHeight(this.nom)/2), this.nom, Color.black);
	}

	@Override
	protected void click(CustomBouton boutonOn, boolean appuie, int clicID,
			int X, int Y) {
		
	}

	@Override
	protected void keyTyped(char carac, int keyCode) {
		
	}

}
