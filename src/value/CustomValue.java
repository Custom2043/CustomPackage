package value;

public abstract class CustomValue
{
	public int max;
	public int value;
	public CustomValue(int m, int v)
	{
		max = m;
		value = v;
	}
	public MultipleStateValue getMultiple()
	{
		return (MultipleStateValue)this;
	}
}
