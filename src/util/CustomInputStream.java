package util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class CustomInputStream extends DataInputStream
{
	public CustomInputStream(InputStream arg0)
	{
		super(arg0);
	}
	public CustomInputStream(File arg0) throws FileNotFoundException
	{
		this(new BufferedInputStream(new FileInputStream(arg0)));
	}
	public String readString() throws IOException
	{
		int l = this.readInt();
		byte[] b = new byte[l];
		this.read(b);
		return new String(b);
	}
	public EntityPosRot readEntityPosRot() throws IOException
	{
		return new EntityPosRot(this.readTriDouble(), this.readTriDouble());
	}
	public TriDouble readTriDouble() throws IOException
	{
		return new TriDouble(this.readDouble(), this.readDouble(), this.readDouble());
	}
}
