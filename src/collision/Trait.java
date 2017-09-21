package collision;

public class Trait
{
	public float x, y, w, h;

	public Trait(float x2, float y2, float w2, float h2) {
		this.x = x2;
		this.y = y2;
		this.w = w2;
		this.h = h2;
	}
	public float getLowX(){return Math.min(this.x, this.x+this.w);}
	public float getLowY(){return Math.min(this.y, this.y+this.h);}
	public float getHighX(){return Math.max(this.x, this.x+this.w);}
	public float getHighY(){return Math.max(this.y, this.y+this.h);}
	@Override
	public String toString()
	{
		return "x : "+this.x+", y : "+this.y+", w : "+this.w+", h : "+this.h;
	}
	public float getLength()
	{
		return (float)Math.sqrt(this.w * this.w + this.h * this.h);
	}
	public boolean touch(Trait t)
	{
		float det = this.w*t.h - this.h * t.w;
		if (det == 0) // Parallèle
		{
			if ((this.x - t.x) * t.h == (this.y - t.y) * t.w)
				if (t.h == 0) // Parallèle à x
					return this.getLowX() <= t.getHighX() && this.getHighX() >= t.getLowX();
				return this.getLowY() <= t.getHighY() && this.getHighY() >= t.getLowY();
		}
		float m = -(-this.w*this.y+this.w*t.y+this.h*this.x-this.h*t.x)/det;
		float k = -(t.h*this.x-t.x*t.h-t.w*this.y+t.w*t.y)/det;
		if (m >= 0 && m <= 1 && k >= 0 && k <= 1)
			return true;
		return false;
	}
	public float getXPosAtY(float y)
	{
		if (this.h != 0)
			return this.x + (y - this.y)/this.h * this.w;
		else if (y == this.y)
			return Float.POSITIVE_INFINITY;
		else
			return Float.NaN;
	}
	public float getYPosAtX(float x)
	{
		if (this.w != 0)
			return this.y + (x-this.x)/this.w * this.h;
		else if (x == this.x)
			return Float.POSITIVE_INFINITY;
		else
			return Float.NaN;
	}
	/**
	 * Angle entre ]-Pi;Pi]
	 */
	public double getAngle()
	{
		return Math.atan2(this.h, this.w);
	}
	//Différence entre ]-Pi;Pi[ à Pi près
	public double getDifAngle(Trait t)
	{
		double dif = this.getAngle() - t.getAngle();
		if (dif > Math.PI)
			dif -= Math.PI * 2;
		if (dif <= -Math.PI)
			dif += Math.PI * 2;
		return dif;
	}
	@Override
	public boolean equals(Object deu)
	{
		if (deu == null) return false;
		if (!(deu instanceof Trait)) return false;
		Trait deux = (Trait)deu;
		return deux.x == this.x && deux.y == this.y && deux.w == this.w && deux.h == this.h;
	}
}
