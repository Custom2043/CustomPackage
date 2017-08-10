package gui;

import util.ScreenCoor;
import value.MultipleStateValue;

public abstract class CustomMultipleStateBouton extends CustomBouton
{
	public MultipleStateValue options;
	public CustomMultipleStateBouton(String n, int i, ScreenCoor c, MultipleStateValue o)
	{
		super(n, i, c, true);
		this.options = o;
	}
	private void changeState()
	{
		this.options.value ++;
		if (this.options.value >= this.options.max)
			this.options.value = 0;
		clickOn();
	}
	public abstract void clickOn();
	@Override
	public void click(CustomBouton boutonOn, boolean appuie, int clicID, int X, int Y)
	{
		if (clicID == 0)
		{
			if (appuie)
			{
				if (boutonOn == this)
				{
					this.changeState();
				}
			}
		}
	}
}
