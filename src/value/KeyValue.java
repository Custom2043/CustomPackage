package value;

import org.lwjgl.input.Keyboard;

public class KeyValue
{
	public static final int MOUSE_MIN_VALUE = 250;
	private int value;
	private final int defaut;
	public String nom;
	public KeyValue (int v, String n)
	{
		nom = n;
		value = defaut = v;
	}
	public String getKeyName()
	{
		if (value < MOUSE_MIN_VALUE)
			return Keyboard.getKeyName(value);
		else
			return "Souris "+(this.value-MOUSE_MIN_VALUE);
	}
	public void reset()
	{
		this.value = this.defaut;
	}
	public boolean isMouseValue(){return this.value >= MOUSE_MIN_VALUE;}
	public int getMouseValue(){return this.value - MOUSE_MIN_VALUE;}
	public int getValue()
	{
		return value;
	}
	public void setValue(int value)
	{
		this.value = value;
	}

}
