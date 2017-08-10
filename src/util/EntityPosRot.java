package util;

public class EntityPosRot
{
	public TriDouble pos;
	public TriDouble angle;

	public EntityPosRot(){this(0,0,0,0,0,0);}
	public EntityPosRot (double x, double y, double z, double xAngle, double yAngle, double zAngle)
	{
		pos = new TriDouble(x,y,z);
		angle = new TriDouble(xAngle, yAngle, zAngle);
	}
	public EntityPosRot(TriDouble p, TriDouble r)
	{
		pos = p;
		angle = r;
	}
	public void copyFrom(EntityPosRot rp)
	{
		this.pos = rp.pos;
		this.angle = rp.angle;
	}
}
