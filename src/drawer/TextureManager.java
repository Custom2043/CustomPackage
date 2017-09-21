package drawer;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class TextureManager
{
	private static String textFolder = "", textMode = "";
	private ArrayList<Texture> list = new ArrayList<Texture>();
	public TextureManager(String folder, String mode)
	{
		textFolder = folder; textMode = mode;
	}
	public Texture loadTexture(String name){return this.loadTexture(name, textMode);}

	public Texture loadTexture(String name, String mod)
	{
		//glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		//glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		File f = new File(textFolder + name);
		if (!f.exists())
		{
			System.out.println("TextureManager ; File doesn't exist : "+f.getAbsolutePath());
			return null;
		}
		try
		{
			System.out.println("TextureManager ; Charge texture : "+f.getAbsolutePath());
			Texture t = TextureLoader.getTexture(mod, new FileInputStream(f));
			this.list.add(t);
			return t;
		}
		catch(Exception e){System.out.println("TextureManager ; Fail in charge of : "+f.getAbsolutePath());e.printStackTrace();return null;}
	}
	public void quit()
	{
		for (Texture t : this.list)
			if (t != null)
				t.release();
	}
	public void setTextureFolder(String folder){textFolder = folder;}
	public void setDefaultMode(String mode){textMode = mode;}
}
