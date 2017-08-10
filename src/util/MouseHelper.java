package util;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class MouseHelper
{
	public static boolean grabbed = false;
	public static int getXMouse()
	{
		return Mouse.getX();
	}
	public static int getYMouse()
	{
		return Display.getHeight() - Mouse.getY() - 1;
	}
	public static int getXEventMouse()
	{
		return Mouse.getEventX();
	}
	public static int getYEventMouse()
	{
		return Display.getHeight() - Mouse.getEventY() - 1;
	}
	public static int getDXMouse()
	{
		return Mouse.getDX();
	}
	public static int getDYMouse()
	{
		return -Mouse.getDY();
	}
	public static void changeMouseGrabState()
	{
		if (grabbed)
			releaseMouse();
		else
			grab();
	}
	public static void grab()
	{
		grabbed = true;
		Mouse.setGrabbed(true);
	}
	public static void releaseMouse()
	{
		grabbed = false;
		Mouse.setGrabbed(false);
	}
	public static void update()
	{
		if (grabbed)
			Mouse.setCursorPosition(Display.getWidth()/2, Display.getHeight()/2);
	}
}
