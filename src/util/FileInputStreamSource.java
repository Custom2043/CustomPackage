package util;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.InputStream;

public class FileInputStreamSource implements InputStreamSource
{
	private File file;
	public FileInputStreamSource(String s)
	{
		this(new File(s));
	}
	public FileInputStreamSource(File f)
	{
		file = f;
		getStreamBack();
	}
	public boolean canStreamBeRetrieved() 
	{
		return file.exists();
	}

	public InputStream getStreamBack() 
	{
		try
		{
			return new FileInputStream(file);
		}catch(Exception e){Logger.error("Can't create stream : "+file.toString(), this.getClass());Logger.error(e, getClass());}
		return null;
	}
}
