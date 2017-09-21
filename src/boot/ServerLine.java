package boot;

import java.io.IOException;

import util.CustomOutputStream;

public class ServerLine
{
	public String file;
	public int packNumber;
	public long offset, size, timestamp;

	public ServerLine(String f, int pN, long o, long s, long ts)
	{
		this.file = f;
		this.packNumber = pN;
		this.offset = o;
		this.size = s;
		this.timestamp = ts;
	}
	public void write(CustomOutputStream cos) throws IOException
	{
		cos.writeString(this.file);
		cos.writeInt(this.packNumber);
		cos.writeLong(this.offset);
		cos.writeLong(this.size);
		cos.writeLong(this.timestamp);
	}

	public void writeCasually(CustomOutputStream cos) throws IOException
	{
		cos.write((this.file+", Pack number : "+this.packNumber+", Offset : "+this.offset+", Size : "+this.size+", Timestamp : "+this.timestamp+"\r\n").getBytes());
	}
}
