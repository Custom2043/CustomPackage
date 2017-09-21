package boot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

public class CommandList
{
	final ArrayList<Command> commands = new ArrayList<Command>();
	long totalSize = 0;
	public CommandList(ServerAncrer ancrer)
	{
		for (ServerLine line : ancrer.lines)
		{
			File f = new File(line.file);
			if (!f.exists() || f.lastModified() != line.timestamp || f.length() != line.size)
			{
				boolean added = false;
				for (Command c : this.commands)
					if (!added && c.packNumber == line.packNumber && line.offset == c.offset + c.totalSize)
					{
						c.totalSize += line.size;
						c.lines.add(line);
						added = true;
					}
				if (!added)
					this.commands.add(new Command(line));
				this.totalSize += line.size;
			}
		}
	}
	public boolean downloadCommands(CustomFTP ftp, String reper) throws IOException
	{
		byte[] b;
		ByteInputStream bis;
		for (Command c : this.commands)
		{
			System.out.println(c.toString());
			b = ftp.download("/"+reper+"/Pack_"+c.packNumber+".btp", c.offset, (int)c.totalSize);

			if (b == null)
			{
				ftp.close();
				return false;
			}
			bis = new ByteInputStream();
			bis.setBuf(b);
			for (int i=0;i<c.lines.size();i++)
				try
				{
					String file = c.lines.get(i).file;
					if (file.contains("/"))
						if (!new File(CustomFTP.nerf(file)).exists())
							new File(CustomFTP.nerf(file)).mkdirs();
					FileOutputStream fos = new FileOutputStream(new File(file));
					byte[] bb = new byte[(int)c.lines.get(i).size];
					bis.read(bb);
					fos.write(bb);
					fos.close();
				}catch(IOException e){e.printStackTrace();}
			bis.close();
		}
		return true;
	}
}
class Command
{
	final ArrayList<ServerLine> lines = new ArrayList<ServerLine>();
	long totalSize;
	final int packNumber;
	final long offset;

	public Command(ServerLine line)
	{
		this.lines.add(line);
		this.packNumber = line.packNumber;
		this.offset = line.offset;
		this.totalSize += line.size;
	}
	@Override
	public String toString()
	{
		String s = "Pack : "+this.packNumber+", offset : "+this.offset+" totalSize : "+this.totalSize;
		for (int i=0;i<this.lines.size();i++)
			s += "\n\tFile : "+this.lines.get(i).file+", size : "+this.lines.get(i).size;
		return s;
	}
}
