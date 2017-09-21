package gui;

import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.input.Mouse;

import util.ScreenCoor;
import drawer.CustomDrawer;

public abstract class CustomBarre extends CustomBoutonContainer
{
	private int max = 0, value = 0, oldValue = 0;
	public CustomBarre(String n, int i,ScreenCoor c, ArrayList<CustomBouton> l, int m)
	{
		super(n, i, c, false);
		this.boutons = l;
		this.max = m;
	}
	public CustomBarre(String n, int i,ScreenCoor c)
	{
		super(n, i, c, false);
		this.boutons = new ArrayList<CustomBouton>();
	}
	public void addBoutonAndHeight(CustomBouton b, int h)
	{
		this.boutons.add(b);
		this.max += h;
	}
	@Override
	public void click(CustomBouton boutonOn, boolean appuie, int clicID, int X, int Y)
	{
		if (boutonOn == this)
			if (clicID == 0)
				if (appuie)
					this.activ();
		if (clicID == 0)
			if (!appuie)
				this.desactiv();
		if (Mouse.isButtonDown(0))
			if (this.isActiv())
				this.setPosition(Y);
		if (boutonOn != this && Y >= 0 && Y <= this.coor.getHeight())
			for (Iterator<CustomBouton> iter = this.boutons.iterator(); iter.hasNext();)
				iter.next().click(boutonOn, appuie, clicID, X, Y);
	}
	@Override
	public CustomBouton getBoutonWithCoor(int X, int Y)
	{
		if (Y >= this.coor.getStartY() && Y <= this.coor.getEndY())
		{
			if (this.coor.isIn(X, Y))
				return this;
			for (Iterator<CustomBouton> iter = this.boutons.iterator();iter.hasNext();)
			{
				CustomBouton bouton = iter.next();
				if (bouton.getBoutonWithCoor(X, Y) != null)
					return bouton;
			}
		}
		return null;
	}
	@Override
	public void keyTyped(char carac, int keyCode)
	{
		Iterator<CustomBouton> iter = this.boutons.iterator();
		for (CustomBouton cb; iter.hasNext();)
		{
			cb = iter.next();
			cb.keyTyped(carac, keyCode);
		}
	}
	@Override
	public void draw()
	{
		this.setValue(this.value - Mouse.getDWheel());
		this.drawBarre();

		for (Iterator<CustomBouton> iter = this.boutons.iterator(); iter.hasNext();)
		{
			CustomBouton cb = iter.next();
			cb.coor = cb.coor.addYFlat(this.oldValue-this.value);
			cb.draw();
		}
		this.oldValue = this.value;
	}
	public abstract void drawBarre();
	public void setPosition(float mouseY)
	{
		this.setValue(Math.round((mouseY - this.getHalfBarreHeight()) * this.barreToRealRatio()));
	}
	/**
	 * Value * realToBarreRatio() = Y
	 */
	public float realToBarreRatio(){return (this.coor.getHeight()) / this.getMax();}
	public float barreToRealRatio(){return (this.getMax()) / this.coor.getHeight();}
	public int getBarreHeight(){return Math.round(this.coor.getHeight() * this.realToBarreRatio());}
	public int getHalfBarreHeight(){return this.getBarreHeight() / 2;}
	public int getPosY(){return Math.round(this.value * this.realToBarreRatio());}
	public int getMaxNoScale(){return this.max;}
	public int getMax(){return (int)(this.max * CustomDrawer.getGuiSize());}
	public void setMax(int m){this.max = m;}
	public void addMax(int m){this.max += m;}
	public int getValue(){return this.value;}
	public void setValue(int v)
	{
		if (v < 0)
			v = 0;
		if (this.getMax() > this.coor.getHeight())
			if (v > this.getMax() - this.coor.getHeight())
				v = (int) (this.getMax() - this.coor.getHeight());
		this.value = v;
	}
}
