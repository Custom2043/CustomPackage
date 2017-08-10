package util;

import org.lwjgl.opengl.Display;

import drawer.CustomDrawer;

public class ScreenCoor
{
	public static ScreenCoor AllScreen = new ScreenCoor(0,0,1,1,0,0,0,0,0,0,0,0);
	public float xScreen,yScreen,wScreen,hScreen, xGui, yGui, wGui, hGui, xFlat, yFlat, wFlat, hFlat;
	
	public static ScreenCoor screen(float x, float y, float w, float h){return new ScreenCoor(x,y,w,h,0,0,0,0,0,0,0,0);}
	public static ScreenCoor screenGui(float x, float y, float w, float h, float xG, float yG, float wG, float hG){return new ScreenCoor(x,y,w,h,xG,yG,wG,hG,0,0,0,0);}
	public static ScreenCoor screenFlat(float x, float y, float w, float h, float xF, float yF, float wF, float hF){return new ScreenCoor(x,y,w,h,0,0,0,0,xF,yF,wF,hF);}
	public static ScreenCoor screenGuiFlat(float x, float y, float w, float h, float xG, float yG, float wG, float hG, float xF, float yF, float wF, float hF){return new ScreenCoor(x,y,w,h,xG,yG,wG,hG,xF,yF,wF,hF);}
	
	public static ScreenCoor gui(float xG, float yG, float wG, float hG){return new ScreenCoor(0,0,0,0,xG,yG,wG,hG,0,0,0,0);}
	public static ScreenCoor guiFlat(float xG, float yG, float wG, float hG, float xF, float yF, float wF, float hF){return new ScreenCoor(0,0,0,0,xG,yG,wG,hG,xF,yF,wF,hF);}

	public static ScreenCoor flat(float xF, float yF, float wF, float hF){return new ScreenCoor(0,0,0,0,0,0,0,0,xF,yF,wF,hF);}

	public static ScreenCoor nul(){return new ScreenCoor(0,0,0,0,0,0,0,0,0,0,0,0);}
	
	private ScreenCoor(float x,float y,float w,float h, float xG, float yG, float wG, float hG, float xF, float yF, float wF, float hF)
	{
		this.xScreen = x; this.xGui = xG; this.xFlat = xF;
		this.yScreen = y; this.yGui = yG; this.yFlat = yF;
		this.wScreen = w; this.wGui = wG; this.wFlat = wF;
		this.hScreen = h; this.hGui = hG; this.hFlat = hF;
	}
	public float[] inFloatArray()
	{
		return new float[]{this.getStartX(), this.getStartY(), this.getEndX(), this.getStartY(), this.getEndX(), this.getEndY(), this.getStartX(), this.getEndY()};
	}
	@Override
	public String toString()
	{
		return "Screen ; X : "+this.xScreen+", Y : "+this.yScreen+",W : "+this.wScreen+",H : "+this.hScreen+
			   "\nGui ; X : "+this.xGui+", Y : "+this.yGui+",W : "+this.wGui+",H : "+this.hGui+
			   "\nFlat ; X : "+this.xFlat+", Y : "+this.yFlat+",W : "+this.wFlat+",H : "+this.hFlat;
	}
	public boolean isIn(float X, float Y)
	{
		return (X >= this.getStartX() && X <= this.getEndX() && Y >= this.getStartY() && Y <= this.getEndY());
	}
	public float getStartX()
	{
		return this.xScreen*this.getScreenWidth() + this.xGui * this.getGuiSize() + this.xFlat;
	}
	public float getStartY()
	{
		return this.yScreen*this.getScreenHeight() + this.yGui * this.getGuiSize() + this.yFlat;
	}
	public float getWidth()
	{
		return this.wScreen*this.getScreenWidth() + this.wGui * this.getGuiSize() + this.wFlat;
	}
	public float getHeight()
	{
		return this.hScreen*this.getScreenHeight() + this.hGui * this.getGuiSize() + this.hFlat;
	}
	public float getEndX()
	{
		return this.getStartX() + this.getWidth();
	}
	public float getEndY()
	{
		return this.getStartY() + this.getHeight();
	}
	public float getMiddleX()
	{
		return this.getStartX() + this.getWidth() / 2;
	}
	public float getMiddleY()
	{
		return this.getStartY() + this.getHeight() / 2;
	}
	private int getScreenHeight()
	{
		return Display.getHeight();
	}
	private int getScreenWidth()
	{
		return Display.getWidth();
	}
	public float getGuiSize()
	{
		return CustomDrawer.getGuiSize();
	}
	public ScreenCoor addScreen(float xAdd, float yAdd, float wAdd, float hAdd){return new ScreenCoor(this.xScreen + xAdd,this.yScreen + yAdd,this.wScreen + wAdd,this.hScreen + hAdd, this.xGui, this.yGui, this.wGui, this.hGui, this.xFlat, this.yFlat, this.wFlat, this.hFlat);}
	public ScreenCoor addGui(float xAdd, float yAdd, float wAdd, float hAdd){return new ScreenCoor(this.xScreen,this.yScreen,this.wScreen,this.hScreen, this.xGui + xAdd, this.yGui + yAdd, this.wGui + wAdd, this.hGui + hAdd, this.xFlat, this.yFlat, this.wFlat, this.hFlat);}
	public ScreenCoor addFlat(float xAdd, float yAdd, float wAdd, float hAdd){return new ScreenCoor(this.xScreen,this.yScreen,this.wScreen,this.hScreen, this.xGui, this.yGui, this.wGui, this.hGui, this.xFlat + xAdd, this.yFlat + yAdd, this.wFlat + wAdd, this.hFlat + hAdd);}

	public ScreenCoor setScreen(float xSet, float ySet, float wSet, float hSet){return new ScreenCoor(xSet,ySet,wSet,hSet, this.xGui, this.yGui, this.wGui, this.hGui, this.xFlat, this.yFlat, this.wFlat, this.hFlat);}
	public ScreenCoor setGui(float xSet, float ySet, float wSet, float hSet){return new ScreenCoor(this.xScreen,this.yScreen,this.wScreen,this.hScreen,xSet,ySet,wSet,hSet, this.xFlat, this.yFlat, this.wFlat, this.hFlat);}
	public ScreenCoor setFlat(float xSet, float ySet, float wSet, float hSet){return new ScreenCoor(this.xScreen,this.yScreen,this.wScreen,this.hScreen, this.xGui, this.yGui, this.wGui, this.hGui,xSet,ySet,wSet,hSet);}

	public ScreenCoor addX(float screen, float gui, float flat){return new ScreenCoor(this.xScreen + screen,this.yScreen,this.wScreen,this.hScreen, this.xGui + gui, this.yGui, this.wGui, this.hGui, this.xFlat + flat, this.yFlat, this.wFlat, this.hFlat);}
	public ScreenCoor addY(float screen, float gui, float flat){return new ScreenCoor(this.xScreen,this.yScreen + screen,this.wScreen,this.hScreen, this.xGui, this.yGui + gui, this.wGui, this.hGui, this.xFlat, this.yFlat + flat, this.wFlat, this.hFlat);}
	public ScreenCoor addW(float screen, float gui, float flat){return new ScreenCoor(this.xScreen,this.yScreen,this.wScreen + screen,this.hScreen, this.xGui, this.yGui, this.wGui + gui, this.hGui, this.xFlat, this.yFlat, this.wFlat + flat, this.hFlat);}
	public ScreenCoor addH(float screen, float gui, float flat){return new ScreenCoor(this.xScreen,this.yScreen,this.wScreen,this.hScreen + screen, this.xGui, this.yGui, this.wGui, this.hGui + gui, this.xFlat, this.yFlat, this.wFlat, this.hFlat + flat);}

	public ScreenCoor setX(float screen, float gui, float flat){return new ScreenCoor(screen,this.yScreen,this.wScreen,this.hScreen, gui, this.yGui, this.wGui, this.hGui, flat, this.yFlat, this.wFlat, this.hFlat);}
	public ScreenCoor setY(float screen, float gui, float flat){return new ScreenCoor(this.xScreen,screen,this.wScreen,this.hScreen, this.xGui, gui, this.wGui, this.hGui, this.xFlat, flat, this.wFlat, this.hFlat);}
	public ScreenCoor setW(float screen, float gui, float flat){return new ScreenCoor(this.xScreen,this.yScreen,screen,this.hScreen, this.xGui, this.yGui, gui, this.hGui, this.xFlat, this.yFlat, flat, this.hFlat);}
	public ScreenCoor setH(float screen, float gui, float flat){return new ScreenCoor(this.xScreen,this.yScreen,this.wScreen,screen, this.xGui, this.yGui, this.wGui, gui, this.xFlat, this.yFlat, this.wFlat, flat);}

	public ScreenCoor setXScreen(float xScreen){return new ScreenCoor(xScreen,this.yScreen,this.wScreen,this.hScreen, this.xGui, this.yGui, this.wGui, this.hGui, this.xFlat, this.yFlat, this.wFlat, this.hFlat);}
	public ScreenCoor setYScreen(float yScreen){return new ScreenCoor(this.xScreen,yScreen,this.wScreen,this.hScreen, this.xGui, this.yGui, this.wGui, this.hGui, this.xFlat, this.yFlat, this.wFlat, this.hFlat);}
	public ScreenCoor setWScreen(float wScreen){return new ScreenCoor(this.xScreen,this.yScreen,wScreen,this.hScreen, this.xGui, this.yGui, this.wGui, this.hGui, this.xFlat, this.yFlat, this.wFlat, this.hFlat);}
	public ScreenCoor setHScreen(float hScreen){return new ScreenCoor(this.xScreen,this.yScreen,this.wScreen,hScreen, this.xGui, this.yGui, this.wGui, this.hGui, this.xFlat, this.yFlat, this.wFlat, this.hFlat);}
	public ScreenCoor setXGui(float xGui){return new ScreenCoor(this.xScreen,this.yScreen,this.wScreen,this.hScreen, xGui, this.yGui, this.wGui, this.hGui, this.xFlat, this.yFlat, this.wFlat, this.hFlat);}
	public ScreenCoor setYGui(float yGui){return new ScreenCoor(this.xScreen,this.yScreen,this.wScreen,this.hScreen, this.xGui, yGui, this.wGui, this.hGui, this.xFlat, this.yFlat, this.wFlat, this.hFlat);}
	public ScreenCoor setWGui(float wGui){return new ScreenCoor(this.xScreen,this.yScreen,this.wScreen,this.hScreen, this.xGui, this.yGui, wGui, this.hGui, this.xFlat, this.yFlat, this.wFlat, this.hFlat);}
	public ScreenCoor setHGui(float hGui){return new ScreenCoor(this.xScreen,this.yScreen,this.wScreen,this.hScreen, this.xGui, this.yGui, this.wGui, hGui, this.xFlat, this.yFlat, this.wFlat, this.hFlat);}
	public ScreenCoor setXFlat(float xFlat){return new ScreenCoor(this.xScreen,this.yScreen,this.wScreen,this.hScreen, this.xGui, this.yGui, this.wGui, this.hGui, xFlat, this.yFlat, this.wFlat, this.hFlat);}
	public ScreenCoor setYFlat(float yFlat){return new ScreenCoor(this.xScreen,this.yScreen,this.wScreen,this.hScreen, this.xGui, this.yGui, this.wGui, this.hGui, this.xFlat, yFlat, this.wFlat, this.hFlat);}
	public ScreenCoor setWFlat(float wFlat){return new ScreenCoor(this.xScreen,this.yScreen,this.wScreen,this.hScreen, this.xGui, this.yGui, this.wGui, this.hGui, this.xFlat, this.yFlat, wFlat, this.hFlat);}
	public ScreenCoor setHFlat(float hFlat){return new ScreenCoor(this.xScreen,this.yScreen,this.wScreen,this.hScreen, this.xGui, this.yGui, this.wGui, this.hGui, this.xFlat, this.yFlat, this.wFlat, hFlat);}

	public ScreenCoor addXScreen(float xScreen){return new ScreenCoor(this.xScreen+xScreen,this.yScreen,this.wScreen,this.hScreen, this.xGui, this.yGui, this.wGui, this.hGui, this.xFlat, this.yFlat, this.wFlat, this.hFlat);}
	public ScreenCoor addYScreen(float yScreen){return new ScreenCoor(this.xScreen,this.yScreen+yScreen,this.wScreen,this.hScreen, this.xGui, this.yGui, this.wGui, this.hGui, this.xFlat, this.yFlat, this.wFlat, this.hFlat);}
	public ScreenCoor addWScreen(float wScreen){return new ScreenCoor(this.xScreen,this.yScreen,this.wScreen+wScreen,this.hScreen, this.xGui, this.yGui, this.wGui, this.hGui, this.xFlat, this.yFlat, this.wFlat, this.hFlat);}
	public ScreenCoor addHScreen(float hScreen){return new ScreenCoor(this.xScreen,this.yScreen,this.wScreen,this.hScreen+hScreen, this.xGui, this.yGui, this.wGui, this.hGui, this.xFlat, this.yFlat, this.wFlat, this.hFlat);}
	public ScreenCoor addXGui(float xGui){return new ScreenCoor(this.xScreen,this.yScreen,this.wScreen,this.hScreen, this.xGui+xGui, this.yGui, this.wGui, this.hGui, this.xFlat, this.yFlat, this.wFlat, this.hFlat);}
	public ScreenCoor addYGui(float yGui){return new ScreenCoor(this.xScreen,this.yScreen,this.wScreen,this.hScreen, this.xGui, this.yGui+yGui, this.wGui, this.hGui, this.xFlat, this.yFlat, this.wFlat, this.hFlat);}
	public ScreenCoor addWGui(float wGui){return new ScreenCoor(this.xScreen,this.yScreen,this.wScreen,this.hScreen, this.xGui, this.yGui, this.wGui+wGui, this.hGui, this.xFlat, this.yFlat, this.wFlat, this.hFlat);}
	public ScreenCoor addHGui(float hGui){return new ScreenCoor(this.xScreen,this.yScreen,this.wScreen,this.hScreen, this.xGui, this.yGui, this.wGui, this.hGui+hGui, this.xFlat, this.yFlat, this.wFlat, this.hFlat);}
	public ScreenCoor addXFlat(float xFlat){return new ScreenCoor(this.xScreen,this.yScreen,this.wScreen,this.hScreen, this.xGui, this.yGui, this.wGui, this.hGui, this.xFlat+xFlat, this.yFlat, this.wFlat, this.hFlat);}
	public ScreenCoor addYFlat(float yFlat){return new ScreenCoor(this.xScreen,this.yScreen,this.wScreen,this.hScreen, this.xGui, this.yGui, this.wGui, this.hGui, this.xFlat, this.yFlat+yFlat, this.wFlat, this.hFlat);}
	public ScreenCoor addWFlat(float wFlat){return new ScreenCoor(this.xScreen,this.yScreen,this.wScreen,this.hScreen, this.xGui, this.yGui, this.wGui, this.hGui, this.xFlat, this.yFlat, this.wFlat+wFlat, this.hFlat);}
	public ScreenCoor addHFlat(float hFlat){return new ScreenCoor(this.xScreen,this.yScreen,this.wScreen,this.hScreen, this.xGui, this.yGui, this.wGui, this.hGui, this.xFlat, this.yFlat, this.wFlat, this.hFlat+hFlat);}

	public ScreenCoor clone()
	{
		return screenGuiFlat(this.xScreen,this.yScreen,this.wScreen,this.hScreen, this.xGui, this.yGui, this.wGui, this.hGui, this.xFlat, this.yFlat, this.wFlat, this.hFlat);
	}
}
