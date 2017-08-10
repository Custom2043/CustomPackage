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
	public CryptInputStream(InputStream is) throws FileNotFoundException 
	{
		super();
		stream = is;
	}
	public int read() throws IOException
	{
		return decrypt(new byte[stream.read()])[0];
	}
	public int read(byte b[], int off, int len) throws IOException 
	{
		int i = stream.read(b, off, len);
		b = decrypt(b);
		return i;
	}
	public byte[] decrypt(byte[] b)
	{
		for (int i = 0;i<b.length;i++)
		{
			b[i] += cle1.charAt(avance) + last + cle2.charAt(Math.abs(last) % 13);
			last = b[i];
			avance ++;
			if (avance > 15)
				avance -= 14;
		}
		return b;
	}
}
