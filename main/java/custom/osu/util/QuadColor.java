package custom.osu.util;

public class QuadColor
{
	Color u, d, t, q;
	public QuadColor(Color hg, Color hd, Color bg, Color bd)
	{
		this.u = hg;
		this.d = hd;
		this.t = bg;
		this.q = bd;
	}
	public QuadColor(Color color)
	{
		this.u = this.d = this.t = this.q = color;
	}
	public QuadColor()
	{
		this.u = this.d = this.t = this.q = Color.WHITE;
	}
	public Color[] getAsColorArray()
	{
		return new Color[]{this.u, this.d, this.t, this.q};
	}
}
