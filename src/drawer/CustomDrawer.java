package drawer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import util.ScreenCoor;
import util.TextureCoor;
import util.TriDouble;

public abstract class CustomDrawer
{
	private static boolean screenGuiModified = true;
	private static float guiSize = 1;
	public static void createDisplay(int width, int height, String name) throws LWJGLException
	{
		Display.setResizable(true);
		Display.setDisplayMode(new DisplayMode(width, height));
		Display.setTitle(name);
		try
		{
			Display.create(new PixelFormat(0, 24, 8, 4));
		}
		catch(Exception e){Display.create();}
		glClearColor(0, 0, 0, 0);
	}
	/*private ByteBuffer readImageToBuffer(InputStream imageStream) throws IOException
    {
        BufferedImage imagebuffer = ImageIO.read(imageStream);
        int[] RGB = imagebuffer.getRGB(0, 0, imagebuffer.getWidth(), imagebuffer.getHeight(), (int[])null, 0, imagebuffer.getWidth());
        ByteBuffer bytBuffer = ByteBuffer.allocate(4 * RGB.length);
        int[] copyRGB = RGB;
        int var6 = RGB.length;
        for (int var7 = 0; var7 < var6; ++var7)
        {
            int var8 = copyRGB[var7];
            bytBuffer.putInt(var8 << 8 | var8 >> 24 & 255);
        }

        bytBuffer.flip();
        return bytBuffer;
    }*/
	public static void setGui(float gui)
	{
		if (guiSize != gui)
			screenGuiModified = true;
		guiSize = gui;
	}
	public static float getGuiSize(){return guiSize;}
	public static void load2D()
	{
		setOrtho();
		glEnable(GL_TEXTURE_2D);
		glDisable(GL_DEPTH_TEST);
		glDisable(GL_CULL_FACE);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
		glColor4f(1, 1, 1, 1);
	}
	public static boolean hasScreenOrGuiBeenModified()
	{
		if(screenGuiModified)
		{
			screenGuiModified = false;
			return true;
		}
		else 
			return false;
		
	}
	public static void load3D(float fov)
	{
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(fov, Display.getWidth()/((float)Display.getHeight()),0.05f, 100f);
		glClear(GL_DEPTH_BUFFER_BIT);
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);
        glEnable(GL_CULL_FACE);
	}
	public static void turnCamera(TriDouble angle)
	{
		glRotated(angle.x, 1, 0, 0);
		glRotated(angle.y, 0, 1, 0);
		glRotated(angle.z, 0, 0, 1);
	}
	public static void translateCamera(TriDouble pos)
	{
		glTranslated(-pos.x, -pos.y, -pos.z);
	}
	public static void setOrtho()
	{
		setOrtho(0,Display.getWidth(),Display.getHeight(),0,1,-1);
	}
	public static void setOrtho(double left, double right, double bottom, double top, double zNear, double zFar)
	{
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glViewport(0,0,Display.getWidth(), Display.getHeight());
		glOrtho(left, right, bottom, top, zNear, zFar);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
	}

	public static void drawCube(double x, double y, double z)
	{
		drawCube(1,1,1,x,y,z,0,0,0);
	}
	public static void drawCube(TriDouble size, TriDouble pos, TriDouble angle)
	{
		drawCube(size.x,size.y,size.z,pos.x,pos.y,pos.z,angle.x,angle.y,angle.z);
	}
	public static void drawCube(double xSize, double ySize, double zSize, double x, double y, double z, double xAngle, double yAngle, double zAngle)
	{
		GL11.glPushMatrix();

		GL11.glRotated(xAngle,1,0,0);
		GL11.glRotated(yAngle,0,1,0);
		GL11.glRotated(zAngle,0,0,1);

		GL11.glTranslated(x, y, z);

		GL11.glScaled(xSize, ySize, zSize);

		GL11.glDisable(GL11.GL_TEXTURE_2D);

		GL11.glBegin(GL11.GL_QUADS);

        GL11.glColor3f(0.0f,1.0f,0.0f);          // Set The Color To Green
        GL11.glVertex3d( 1, 1,0);          // Top Right Of The Quad (Top)
        GL11.glVertex3d(0, 1,0);          // Top Left Of The Quad (Top)
        GL11.glVertex3d(0, 1, 1);          // Bottom Left Of The Quad (Top)
        GL11.glVertex3d( 1, 1, 1);          // Bottom Right Of The Quad (Top)

        GL11.glColor3d(1,.5f,0.0f);          // Set The Color To Orange
        GL11.glVertex3d( 1,0, 1);          // Top Right Of The Quad (Bottom)
        GL11.glVertex3d(0,0, 1);          // Top Left Of The Quad (Bottom)
        GL11.glVertex3d(0,0,0);          // Bottom Left Of The Quad (Bottom)
        GL11.glVertex3d( 1,0,0);  		// Bottom Right Of The Quad (Bottom)

        GL11.glColor3d(1,0.0f,0.0f);          // Set The Color To Red
        GL11.glVertex3d( 1, 1, 1);          // Top Right Of The Quad (Front)
        GL11.glVertex3d(0, 1, 1);          // Top Left Of The Quad (Front)
        GL11.glVertex3d(0,0, 1);          // Bottom Left Of The Quad (Front)
        GL11.glVertex3d( 1,0, 1);          // Bottom Right Of The Quad (Front)

        GL11.glColor3d(1,1,0.0f);          // Set The Color To Yellow
        GL11.glVertex3d( 1,0,0);          // Bottom Left Of The Quad (Back)
        GL11.glVertex3d(0,0,0);          // Bottom Right Of The Quad (Back)
        GL11.glVertex3d(0, 1,0);          // Top Right Of The Quad (Back)
        GL11.glVertex3d( 1, 1,0);          // Top Left Of The Quad (Back)

        GL11.glColor3d(0.0f,0.0f,1);          // Set The Color To Blue
        GL11.glVertex3d(0, 1, 1);          // Top Right Of The Quad (Left)
        GL11.glVertex3d(0, 1,0);          // Top Left Of The Quad (Left)
        GL11.glVertex3d(0,0,0);          // Bottom Left Of The Quad (Left)
        GL11.glVertex3d(0,0, 1);          // Bottom Right Of The Quad (Left)

        GL11.glColor3d(1,0.0f,1);          // Set The Color To Violet
        GL11.glVertex3d( 1, 1,0);          // Top Right Of The Quad (Right)
        GL11.glVertex3d( 1, 1, 1);          // Top Left Of The Quad (Right)
        GL11.glVertex3d( 1,0, 1);          // Bottom Left Of The Quad (Right)
        GL11.glVertex3d( 1,0,0);          // Bottom Right Of The Quad (Right)
        GL11.glEnd();

        GL11.glEnable(GL11.GL_TEXTURE_2D);

		GL11.glPopMatrix();
	}
	public static void blit (Texture text, ScreenCoor coor, TextureCoor textureCoor)
	{
		blitWithColor(text, coor, textureCoor, 255,255,255,255);
	}
	public static void blitWithColor(Texture text, ScreenCoor coor, TextureCoor textureCoor, Color c)
	{
		blitWithColor(text,coor,textureCoor,c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
	}
	public static void blitWithColor(Texture text, ScreenCoor coor, TextureCoor textureCoor, int r, int v, int b, int a)
	{
		if (text == null)
		{
			drawRect(coor,r, v, b, a);
		}
		else
		{
			glColor4ub((byte)r, (byte)v, (byte)b, (byte)a);
			text.bind();
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glBegin(GL_QUADS);
			glTexCoord2f(textureCoor.getXStart(text), textureCoor.getYStart(text));
			glVertex2f((int)coor.getStartX(),(int)coor.getStartY());
			glTexCoord2f(textureCoor.getXEnd(text), textureCoor.getYStart(text));
			glVertex2f((int)coor.getEndX(),(int)coor.getStartY());
			glTexCoord2f(textureCoor.getXEnd(text), textureCoor.getYEnd(text));
			glVertex2f((int)coor.getEndX(),(int)coor.getEndY());
			glTexCoord2f(textureCoor.getXStart(text),textureCoor.getYEnd(text));
			glVertex2f((int)coor.getStartX(),(int)coor.getEndY());
			glEnd();
		}
	}
	public static void drawLine(float x1, float y1, float x2, float y2, Color c)
	{
		drawLine(x1,y1,x2,y2,c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
	}
	public static void drawLine(float x1, float y1, float x2, float y2, int r, int v, int b, int a)
	{
		glColor4ub((byte)r,(byte)v,(byte)b,(byte)a);
		glDisable(GL_TEXTURE_2D);
		glBegin(GL_LINES);
		glVertex2f(x1,y1);
		glVertex2f(x2,y2);
		glEnd();
		glEnable(GL_TEXTURE_2D);
	}
	public static void drawRect(ScreenCoor coor, Color c)
	{
		drawRect(coor,c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
	}
	public static void drawRect(ScreenCoor coor, int r, int v, int b, int a)
	{
		glColor4ub((byte)r,(byte)v,(byte)b,(byte)a);
		glDisable(GL_TEXTURE_2D);
		glBegin(GL_QUADS);
		glVertex2f(coor.getStartX(),coor.getStartY());
		glVertex2f(coor.getEndX(),coor.getStartY());
		glVertex2f(coor.getEndX(),coor.getEndY());
		glVertex2f(coor.getStartX(),coor.getEndY());
		glEnd();
		glEnable(GL_TEXTURE_2D);
	}
	public static void updateScreenSize()
	{
		setOrtho();
		screenGuiModified = true;
	}
	public static void update()
	{
		Display.update();
		glClear(GL_COLOR_BUFFER_BIT);
		glLoadIdentity(); // Clear ModelView
	}
	public static void drawModel(Model model)
	{
		//glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		//glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		glColor4f(1,1,1,1);
		
		if (model.texture != null)
			model.texture.bind();
		
		GL30.glBindVertexArray(model.vaoId);
		
		for (int i=0;i<16;i++)
			if (model.isVertexArrayEnabled(i))
				GL20.glEnableVertexAttribArray(i);
		GL11.glDrawArrays(GL11.GL_QUADS, 0, model.vertexNumber);
		for (int i=0;i<16;i++)
			if (model.isVertexArrayEnabled(i))
				GL20.glEnableVertexAttribArray(i);
		
		GL30.glBindVertexArray(0);
	}
	public static void drawString(float x, float y, boolean centeredX, boolean centeredY, String text, AngelCodeFont font, Color c)
	{
		font.drawString(x - (centeredX ? font.getWidth(text)/2 : 0), y - (centeredY ? font.getHeight(text)/2 : 0), text, c);
	}
}
