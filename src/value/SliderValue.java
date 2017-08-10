package value;

public class SliderValue
{
	public int max, min, value;
	
	public SliderValue(int mi, int ma, int va)
	{
		max = ma; min = mi; value = va;
	}
	
	public float getPourcent()
	{
		return value / 100f;
	}
}
