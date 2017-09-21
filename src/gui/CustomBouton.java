package gui;

import util.ScreenCoor;

public abstract class CustomBouton
{
	static CustomBouton boutonOn = null;
	protected boolean isActiv = true;
	public String nom;
	public final int id;
	public ScreenCoor coor;

	public CustomBouton(String n, int i, ScreenCoor c, boolean a)
	{
		this.nom = n;
		this.isActiv = a;
		this.id = i;
		this.coor = c;
	}
	public CustomSelecter getSelect()
	{
		return (CustomSelecter)this;
	}
	public CustomSlider getSlider()
	{
		return (CustomSlider)this;
	}
	public CustomMultipleStateBouton getMultiple()
	{
		return (CustomMultipleStateBouton)this;
	}
	public CustomZoneTexte getTexte()
	{
		return (CustomZoneTexte)this;
	}
	public CustomKeyBouton getKey()
	{
		return (CustomKeyBouton)this;
	}
	public CustomBarre getBarre()
	{
		return (CustomBarre)this;
	}
	public CustomBoutonContainer getContainer()
	{
		return (CustomBoutonContainer)this;
	}

	public boolean isSelecter()
	{
		return this instanceof CustomSelecter;
	}
	public boolean isContainer()
	{
		return this instanceof CustomBoutonContainer;
	}
	public boolean isSlider()
	{
		return this instanceof CustomSlider;
	}
	public boolean isMultiple()
	{
		return this instanceof CustomMultipleStateBouton;
	}
	public boolean isTexte()
	{
		return this instanceof CustomZoneTexte;
	}
	public boolean isKey()
	{
		return this instanceof CustomKeyBouton;
	}
	public boolean isBarre()
	{
		return this instanceof CustomBarre;
	}

	public void activ(){this.isActiv = true;}
	public void desactiv(){this.isActiv = false;}
	public boolean isActiv(){return this.isActiv;}

	public boolean isHovered()
	{
		return boutonOn == this;
	}

	CustomBouton getBoutonWithCoor(int X, int Y)
	{
		if (this.coor.isIn(X, Y))
			return this;
		return null;
	}

	CustomBouton getBoutonWithID(int id)
	{
		if (this.id == id)
			return this;
		return null;
	}
	protected abstract void click(CustomBouton boutonOn, boolean appuie, int clicID, int X, int Y);
	protected abstract void keyTyped(char carac, int keyCode);
	public abstract void draw();
}
