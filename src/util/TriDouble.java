package util;

public class TriDouble
{
	public double x, y, z;
	public TriDouble(){this(0,0,0);}
	public TriDouble (double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public TriDouble add(TriDouble de)
	{
		return new TriDouble (this.x+de.x, this.y+de.y, this.z+de.z);
	}
}
