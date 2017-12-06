package test;

import org.lwjgl.opengl.Display;

import drawer.*;
import util.*;

public class TextureTest
{
	public static void main(String[] args) throws Exception
	{
		Logger.setLoggerProperties(true, true, true, true);
		
		CustomDrawer.createDisplay(1280, 720, "GNEEEEAY");

		TextureManager loader = new TextureManager("");
		
		Texture texture = loader.loadTexture("test.jpg");
		
		TexturedScreenCoorShader shader = new TexturedScreenCoorShader("vertex.txt", "fragment.txt");
		
		ScreenCoorModel model = new ScreenCoorModel(ScreenCoor.AllScreen, new QuadColor(), TextureCoor.allPicture, texture);
		
		texture.bind();
		
		Matrix2DShader.setMatrix(Matrix.createOrthographicMatrix());
		
		Matrix2DShader.setScreenData(Display.getWidth(), Display.getHeight(), 1);
		
		while (!Display.isCloseRequested())
		{
			if (Display.wasResized())
			{
				Matrix2DShader.setMatrix(Matrix.createOrthographicMatrix());
				
				Matrix2DShader.setScreenData(Display.getWidth(), Display.getHeight(), 1);
				
				CustomDrawer.setOrtho();
			}
			
			shader.start();
			
			CustomDrawer.drawModel(model);
			
			ShaderProgram.stop();
			
			CustomDrawer.update();
		}
		Display.destroy();
	}
}
