package gui;

import java.util.ArrayList;
import java.util.Iterator;

import util.ScreenCoor;

public abstract class CustomBoutonContainer extends CustomBouton
{
	public ArrayList<CustomBouton> boutons;
	public CustomBoutonContainer(String n, int i,ScreenCoor c, ArrayList<CustomBouton> l, boolean a)
	{
		super(n, i, c, a);
		this.boutons = l;
	}
	public CustomBoutonContainer(String n, int i,ScreenCoor c, boolean a)
	{
		super(n, i, c, a);
		this.boutons = new ArrayList<CustomBouton>();
	}
	@Override
	public CustomBouton getBoutonWithCoor(int X, int Y)
	{
		if (this.coor.isIn(X, Y))
			return this;
		for (Iterator<CustomBouton> iter = this.boutons.iterator();iter.hasNext();)
		{
			CustomBouton bouton = iter.next().getBoutonWithCoor(X, Y);
			if (bouton != null)
				return bouton;
		}
		return null;
	}
	@Override
	public CustomBouton getBoutonWithID(int id)
	{
		if (this.id == id)
			return this;
		for (Iterator<CustomBouton> iter = this.boutons.iterator();iter.hasNext();)
		{
			CustomBouton bouton = iter.next().getBoutonWithID(id);
			if (bouton != null)
				return bouton;
		}
		return null;
	}
}
