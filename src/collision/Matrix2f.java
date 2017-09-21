package collision;

import org.lwjgl.util.vector.Vector2f;

public class Matrix2f
{
	private float m00, m01, m10, m11,xCenter=0, yCenter=0;
	public Matrix2f(float un, float deux, float trois, float quatre, Vector2f center)
	{
		this.m00 = un; // mXY
		this.m01 = deux;
		this.m10 = trois;
		this.m11 = quatre;
		center = this.multiply(center);
		this.xCenter = center.x;
		this.yCenter = center.y;
	}
	public Matrix2f(float un, float deux, float trois, float quatre, float det, Vector2f center)
	{
		this.m00=quatre/det;this.m01=-deux/det;this.m10=-trois/det;this.m11=un/det;
		center = this.multiply(center);
		this.xCenter = center.x;
		this.yCenter = center.y;
	}
	public Vector2f multiply(Vector2f v)
	{
		return new Vector2f(this.m00 * v.x + this.m10 * v.y, this.m01 * v.x + this.m11 * v.y);
	}
	public Trait multiply(Trait v)
	{
		return new Trait(this.m00 * v.x + this.m10 * v.y - this.xCenter, this.m01 * v.x + this.m11 * v.y - this.yCenter, this.m00 * v.w + this.m10 * v.h, this.m01 * v.w + this.m11 * v.h);
	}
}
