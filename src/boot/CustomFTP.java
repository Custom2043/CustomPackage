package boot;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.io.CopyStreamAdapter;

public abstract class CustomFTP
{
	FTPClient ftp = new FTPClient();
	private boolean continu = true;

	public CustomFTP()
	{
		this.ftp.setConnectTimeout(this.getConnectTimeout());
		this.ftp.setDataTimeout(this.getDataTimeout());
		this.ftp.setReceieveDataSocketBufferSize(10500000);
	}

	public boolean shouldContinu() {return this.continu;}
	public void stop() {this.continu = false;}
	public void restart() {this.continu = true;}

	public abstract void tryToConnectOnce(int attempt);
	public abstract void failToConnectOnce(int attempt);

	public boolean connect()
	{
		for (int i = 0; i < 3 && this.continu; i++)
		{
			this.tryToConnectOnce(i);
			try
			{
				this.ftp.connect(this.getServerAdress());
				if (!this.ftp.login(this.getUsername(), this.getPassword()))
				{
					this.closeProperly();
					return false;
				}
				this.ftp.enterLocalPassiveMode();
				this.ftp.setFileTransferMode(FTP.BINARY_FILE_TYPE);
				this.ftp.setFileType(FTP.BINARY_FILE_TYPE);
				return true;
			} catch (Exception e) {this.failToConnectOnce(i);}
		}
		return false;
	}

	public abstract void successfullyDisconnect();

	public void closeProperly()
	{
		try
		{
			if (this.ftp.isConnected())
			{
				this.ftp.logout();
				this.ftp.disconnect();
				this.successfullyDisconnect();
			}
		}
		catch (Exception e) {this.close();}
	}
	public abstract void emergencyDisconnect();
	public void close()
	{
		this.emergencyDisconnect();
		try
		{
			this.ftp.disconnect();
		}
		catch (Exception e) {}
	}

	public abstract int getConnectTimeout();
	public abstract int getDataTimeout();
	public abstract String getServerAdress();
	public abstract String getUsername();
	public abstract String getPassword();

	public boolean downloadAt(String clientFile, String serverFile, long offset,long size)
	{
		if (clientFile.contains("/"))
			if (!new File(nerf(clientFile)).exists())
				new File(nerf(clientFile)).mkdirs();
		for (int i = 0; i < 3 && this.continu; i++)
		{
			this.tryToDownloadOnce(i);
			this.ftp.setRestartOffset(offset);
			byte[] b = this.download(serverFile, size);
			if (b != null)
			{
				try
				{
					FileOutputStream fos = new FileOutputStream(clientFile);
					fos.write(b);
					fos.close();
					return true;
				}
				catch (IOException e) {}
				return false;
			}
			else if (this.continu)
			{
				this.failToDownloadOnce(i);
				this.close();
				if (!this.connect())
					i = 3;
			}
		}
		return false;
	}

	public byte[] download(String serverFile, long offset,long size)
	{
		for (int i = 0; i < 3 && this.continu; i++)
		{
			this.tryToDownloadOnce(i);
			this.ftp.setRestartOffset(offset);
			byte[] b = this.download(serverFile, size);
			if (b != null)
				return b;
			else if (this.continu)
			{
				this.failToDownloadOnce(i);
				this.close();
				if (!this.connect())
					i = 3;
			}
		}
		return null;
	}

	public abstract void transferredBytes(long totalBytesTransferred,int bytesTransferred, long streamSize);
	public abstract void tryToDownloadOnce(int attempt);
	public abstract void failToDownloadOnce(int attempt);

	public byte[] download(String serverFile, long size)
	{
		CopyStreamAdapter streamListener = new CopyStreamAdapter()
		{
			@Override
			public void bytesTransferred(long totalBytes, int transferred, long streamSize)
			{
				CustomFTP.this.transferredBytes(totalBytes, transferred,streamSize);
			}
		};
		this.ftp.setCopyStreamListener(streamListener);

		FileOutputStream oos = null;
		InputStream ois = null;
		try
		{
			if (size == -1)//Important : obtenir la taille AVANT le stream, sinon �a rame
				size = (int) this.ftp.listFiles(stringToURI(serverFile))[0].getSize();
			ois = this.ftp.retrieveFileStream(stringToURI(serverFile));
			byte[] buf = new byte[(int)size];
			DataInputStream dis = new DataInputStream(ois);
			dis.readFully(buf);
			ois.close();
			this.ftp.completePendingCommand();
			return buf;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (oos != null)
				IOUtils.closeQuietly(oos);
			if (ois != null)
				IOUtils.closeQuietly(ois);
		}
		return null;
	}

	public static String stringToURI(String string)
	{
		return string.replace(" ", "%20");
	}
	public static String URIToString(String uri)
	{
		return uri.replace("%20", " ");
	}
	public static String nerf(String file)
	{
		int index = file.lastIndexOf("/");
		if (index == -1)
			return new String(file);
		return file.substring(0, file.lastIndexOf("/"));
	}
}
