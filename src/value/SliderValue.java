package value;

public class SliderValue extends CustomValue
{
	public int min;

	public SliderValue(int mi, int ma, int va)
	{
		super(ma, va);
		this.max = ma; this.min = mi; this.value = va;
	}

	public float getPourcent()
	{
		return this.value / 100f;
	}
}
