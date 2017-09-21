package value;

public abstract class CustomValue
{
	public int max;
	public int value;
	public CustomValue(int m, int v)
	{
		this.max = m;
		this.value = v;
	}
	public MultipleStateValue getMultiple()
	{
		return (MultipleStateValue)this;
	}
	public SliderValue getSlider()
	{
		return (SliderValue)this;
	}
}
