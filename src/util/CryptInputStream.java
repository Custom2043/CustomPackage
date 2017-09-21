package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class CryptInputStream extends InputStream
{
	public static final String cle1 = "AJACCIOFUTMUETTE", cle2 = "MILLENALMAHOM";
	public int last = 0, avance = 0;
	public InputStream stream;
	public CryptInputStream(File arg0) throws FileNotFoundException
	{
		this(new FileInputStream(arg0));
	}
	public CryptInputStream(InputStream is)
	{
		super();
		this.stream = is;
	}
	@Override
	public int read() throws IOException
	{
		return this.decrypt(new byte[this.stream.read()])[0];
	}
	@Override
	public int read(byte b[], int off, int len) throws IOException
	{
		int i = this.stream.read(b, off, len);
		b = this.decrypt(b);
		return i;
	}
	public byte[] decrypt(byte[] b)
	{
		for (int i = 0;i<b.length;i++)
		{
			b[i] += cle1.charAt(this.avance) + this.last + cle2.charAt(Math.abs(this.last) % 13);
			this.last = b[i];
			this.avance ++;
			if (this.avance > 15)
				this.avance -= 14;
		}
		return b;
	}
}
