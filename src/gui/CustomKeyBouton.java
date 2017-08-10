package gui;

import util.ScreenCoor;
import value.KeyValue;

public abstract class CustomKeyBouton extends CustomBouton
{
	public static CustomKeyBouton changing = null;
	public KeyValue key;
	public CustomKeyBouton(String n, int i, ScreenCoor c, KeyValue k)
	{
		super(n, i, c, false);
		this.key = k;
	}
	@Override
	public void activ()
	{
		this.isActiv = true;
		changing = this;
	}
	@Override
	public void desactiv()
	{
		this.isActiv = false;
		if (changing == this)
			changing = null;
	}
	private void setClic(int clicID)
	{
		this.key.setValue(clicID+KeyValue.MOUSE_MIN_VALUE);
		this.desactiv();
	}
	@Override
	public void click(CustomBouton boutonOn, boolean appuie, int clicID, int X, int Y)
	{
		if (appuie)
		{
			if (this.isActiv)
				if (boutonOn == this)
					this.setClic(clicID);
				else
					this.desactiv();
			else if (clicID == 0)
					if (boutonOn == this)
						this.activ();
		}
	}
	@Override
	public void keyTyped(char carac, int keyCode)
	{
		if (this.isActiv)
		{
			this.getKey().key.setValue(keyCode);
			this.desactiv();
		}
	}

}
