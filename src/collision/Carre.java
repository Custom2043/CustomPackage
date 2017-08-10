package collision;

public class Carre
{
	private float x, y, w, h;
	public Carre(float x2, float y2, float w2, float h2)
	{
		this.x = x2;
		this.y = y2;
		this.w = w2;
		this.h = h2;
	}
	public float getLowX(){return Math.min(this.x, this.x+this.w);}
	public float getLowY(){return Math.min(this.y, this.y+this.h);}
	public float getHighX(){return Math.max(this.x, this.x+this.w);}
	public float getHighY(){return Math.max(this.y, this.y+this.h);}

	public boolean touch(Trait t)
	{
		return this.isIn(t.x, t.y) || this.isIn(t.x+t.w, t.y+t.h) || t.touch(new Trait(this.x,this.y,0,this.h)) || t.touch(new Trait(this.x,this.y,this.w,0)) || t.touch(new Trait(this.x+this.w,this.y,0,this.h)) || t.touch(new Trait(this.x,this.y+this.h,this.w,0));
	}
	public boolean isIn(float x, float y)
	{
		return x >= this.getLowX() && x <= this.getHighX() && y >= this.getLowY() && y <= this.getHighY();
	}
	@Override
	public String toString()
	{
		return "x : "+this.x+", y : "+this.y+", w : "+this.w+", h : "+this.h;
	}
}
