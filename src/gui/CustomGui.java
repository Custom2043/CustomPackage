package gui;

import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public abstract class CustomGui
{
	public ArrayList<CustomBouton> boutons = new ArrayList<CustomBouton>();
	public abstract void draw();
	protected abstract void mouseEvent(int clicID, int X, int Y, boolean press, CustomBouton boutonOn);
	protected abstract void keyboardEvent(char carac, int keyCode);
	public CustomBouton getBoutonWithCoor(int X, int Y)
	{
		for (Iterator<CustomBouton> iter = this.boutons.iterator();iter.hasNext();)
		{
			CustomBouton bouton = iter.next().getBoutonWithCoor(X, Y);
			if (bouton != null)
				return bouton;
		}
		return null;
	}
	public CustomBouton getBoutonWithID(int id)
	{
		for (Iterator<CustomBouton> iter = this.boutons.iterator();iter.hasNext();)
		{
			CustomBouton bouton = iter.next().getBoutonWithID(id);
			if (bouton != null)
				return bouton;
		}
		return null;
	}
	/**
	 * Has to be called at each keyboard event
	 * Update buttons and call keyboardEvent
	 */
	public void type()
	{
		for (Iterator<CustomBouton> iter = this.boutons.iterator();iter.hasNext();)
		{
			iter.next().keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
		}
		this.keyboardEvent(Keyboard.getEventCharacter(), Keyboard.getEventKey());
	}
	/**
	 * Has to be called at each mouse event
	 * Update buttons and call mouseEvent
	 */
	public void click()
	{
		int Y = Display.getHeight() - Mouse.getEventY() - 1;
		CustomBouton.boutonOn = this.getBoutonWithCoor(Mouse.getEventX(), Y);
		for (CustomBouton b : this.boutons)
			b.click(CustomBouton.boutonOn, Mouse.getEventButtonState(), Mouse.getEventButton(), (int)(Mouse.getEventX() - b.coor.getStartX()), (int) (Y - b.coor.getStartY()));

		this.mouseEvent(Mouse.getEventButton(), Mouse.getEventX(),Y, Mouse.getEventButtonState(), CustomBouton.boutonOn);
	}
	public void drawButtons()
	{
		for (Iterator<CustomBouton> iter = this.boutons.iterator();iter.hasNext();)
		{
			iter.next().draw();
		}
	}
}
