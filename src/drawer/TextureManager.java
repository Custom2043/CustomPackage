package drawer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

import util.Logger;

import static org.lwjgl.opengl.GL11.*;

public class TextureManager
{
	private static String textFolder = "";
	private ArrayList<Texture> list = new ArrayList<Texture>();
	public TextureManager(String folder)
	{
		textFolder = folder;
	}
	public Texture loadTexture(String name)
	{
		File f = new File(textFolder + name);
		if (!f.exists())
		{
			Logger.error("Fail in charge of : "+f.getAbsolutePath());
			return null;
		}
		else
			try
			{
				return loadTexture(new FileInputStream(f));
			}
			catch(Exception e){Logger.error("Fail in charge of : "+f.getAbsolutePath());Logger.error(e);return null;}
	}
	public Texture loadTexture(InputStream is)
	{
		try
		{
			BufferedImage image = ImageIO.read(is);
			is.close();
			
			Texture t = new Texture(glGenTextures(), image.getWidth(),  image.getHeight());
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			
			int[] pixels = new int[image.getWidth() * image.getHeight()];
		    image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

		    ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);
		    
		    for (int pixel : pixels)
		    {
		    	buffer.put((byte) ((pixel >> 16) & 0xFF));
	            buffer.put((byte) ((pixel >> 8) & 0xFF));
	            buffer.put((byte) (pixel & 0xFF));
	            buffer.put((byte) ((pixel >> 24) & 0xFF));
		    }
		    buffer.flip();
		    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
			this.list.add(t);
			Logger.debug("Texture loaded correctly");
			return t;
		}
		catch(Exception e){System.out.println("Fail in charge of a texture");Logger.error(e);return null;}
	}
	public void quit()
	{
		for (Texture t : this.list)
			if (t != null)
				t.release();
	}
	public void setTextureFolder(String folder){textFolder = folder;}
}
