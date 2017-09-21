package util;

public class EntityPosRot
{
	public TriDouble pos;
	public TriDouble angle;

	public EntityPosRot(){this(0,0,0,0,0,0);}
	public EntityPosRot (double x, double y, double z, double xAngle, double yAngle, double zAngle)
	{
		this.pos = new TriDouble(x,y,z);
		this.angle = new TriDouble(xAngle, yAngle, zAngle);
	}
	public EntityPosRot(TriDouble p, TriDouble r)
	{
		this.pos = p;
		this.angle = r;
	}
	public void copyFrom(EntityPosRot rp)
	{
		this.pos = rp.pos;
		this.angle = rp.angle;
	}
}
