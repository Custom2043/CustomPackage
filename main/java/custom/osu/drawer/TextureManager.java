package custom.osu.drawer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import custom.osu.main.MainActivity;

import static android.opengl.GLES20.*;

public class TextureManager
{
	private ArrayList<Texture> list = new ArrayList<Texture>();
	public Texture loadTexture(String name)
	{
		try
		{
            InputStream is = MainActivity.getAsset("textures/"+name);
            Bitmap imageBuffer = BitmapFactory.decodeStream(is);
			is.close();

			int[] text = new int[1];
            glGenTextures(1, text, 0);
			Texture t = new Texture(text[0], imageBuffer.getWidth(), imageBuffer.getHeight());
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, imageBuffer, 0);
			this.list.add(t);
			System.out.println("TextureManager ; Charge texture correctly : "+name);
			return t;
		}
		catch(Exception e){System.out.println("TextureManager ; Fail in charge of : "+name);e.printStackTrace();return null;}
	}
}
