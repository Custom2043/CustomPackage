package util;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CustomOutputStream extends DataOutputStream
{
	public CustomOutputStream(OutputStream os)
	{
		super(os);
	}
	public CustomOutputStream(File arg0) throws FileNotFoundException 
	{
		this(new BufferedOutputStream(new FileOutputStream(arg0)));
	}
	public void writeString(String s) throws IOException
	{
		this.writeInt(s.length());
		this.write(s.getBytes());
	}
	public void writeEntityPosRot(EntityPosRot rp) throws IOException
	{
		this.writeTriDouble(rp.pos);
		this.writeTriDouble(rp.angle);
	}
	public void writeTriDouble(TriDouble td) throws IOException
	{
		this.writeDouble(td.x);
		this.writeDouble(td.y);
		this.writeDouble(td.z);
	}
}
