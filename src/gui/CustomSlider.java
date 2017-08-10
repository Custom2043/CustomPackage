package gui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import util.MouseHelper;
import util.ScreenCoor;
import value.SliderValue;

public abstract class CustomSlider extends CustomBouton
{
	public SliderValue barre;
	public static final byte LARGEUR = 5;
	public CustomSlider(String n, int i,ScreenCoor c, SliderValue b)
	{
		super(n, i ,c, false);
		this.barre = b;
	}
	public float getPos()
	{
		return ((float)(this.barre.value - this.barre.min)) / ((float)(this.barre.max - this.barre.min));
	}
	public void setPosition(float X)
	{
		if (X < LARGEUR)
			X = LARGEUR;
		if (X > this.coor.getWidth() - LARGEUR)
			X = this.coor.getWidth() - LARGEUR;
		this.barre.value = (int)(((X-LARGEUR)/(this.coor.getWidth() - 2*LARGEUR)) * (this.barre.max - this.barre.min))+this.barre.min;
	}
	@Override
	public void click(CustomBouton boutonOn, boolean appuie, int clicID, int X, int Y)
	{
		if (clicID == 0)
		{
			if (appuie)
			{
				if (boutonOn == this)
				{
					this.activ();
				}
			}
			else
			{
				if (this.isActiv)
				{
					this.desactiv();
				}
			}
		}
		if (Mouse.isButtonDown(0))
		{
				if (this.isActiv)
					this.setPosition(X);
		}
	}
	@Override
	public void keyTyped(char carac, int keyCode)
	{
		if (this.coor.isIn(MouseHelper.getXMouse(), MouseHelper.getYMouse()) && Keyboard.getEventKeyState())
		{
			if (keyCode == Keyboard.KEY_RIGHT)
				if (this.getSlider().barre.value < this.getSlider().barre.max)
					this.getSlider().barre.value++;
			if (keyCode == Keyboard.KEY_LEFT)
				if (this.getSlider().barre.value > this.getSlider().barre.min)
					this.getSlider().barre.value--;
		}
	}
}
