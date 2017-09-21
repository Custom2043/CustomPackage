package boot;

import java.io.IOException;
import java.util.ArrayList;

import util.CustomInputStream;

public class ServerAncrer
{
	public ArrayList<ServerLine> lines = new ArrayList<ServerLine>();
	public ServerAncrer(CustomInputStream cis) throws IOException
	{
		while (cis.available() > 0)
			this.lines.add(new ServerLine(cis.readString(), cis.readInt(), cis.readLong(), cis.readLong(), cis.readLong()));
	}
}
