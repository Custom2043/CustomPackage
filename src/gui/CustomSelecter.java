package gui;

import util.ScreenCoor;

public abstract class CustomSelecter extends CustomBouton
{
	public static CustomSelecter selected = null;
	public CustomSelecter(String n, int i, ScreenCoor c)
	{
		super(n, i, c, false);
	}
	@Override
	public void click(CustomBouton boutonOn, boolean appuie, int clicID,int X, int Y)
	{
		if (clicID == 0)
			if (appuie)
				if (boutonOn == this)
					this.activ();
				else if (boutonOn != null && boutonOn.isSelecter())
					this.desactiv();
	}
	public String getName()
	{
		return this.nom;
	}
	@Override
	public void activ()
	{
		selected = this;
		this.isActiv = true;
	}
	@Override
	public void desactiv()
	{
		if (selected == this)
			selected = null;
		this.isActiv = false;
	}
}
