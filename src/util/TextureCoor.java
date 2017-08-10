package util;

import org.newdawn.slick.opengl.Texture;

public class TextureCoor
{
	public static TextureCoor allPicture = new TextureCoor(0,0,1,1,0,0,0,0);
	public float xSize, ySize, wSize, hSize;
	public float xFlat, yFlat, wFlat, hFlat;
	public TextureCoor(float xFlat, float yFlat, float wFlat, float hFlat)
	{
		this.xFlat = xFlat;
		this.yFlat = yFlat;
		this.wFlat = wFlat;
		this.hFlat = hFlat;
	}
	public TextureCoor(float xSize,float ySize,float wSize,float hSize, float xFlat, float yFlat, float wFlat, float hFlat)
	{
		this.xSize = xSize;
		this.ySize = ySize;
		this.wSize = wSize;
		this.hSize = hSize;
		this.xFlat = xFlat;
		this.yFlat = yFlat;
		this.wFlat = wFlat;
		this.hFlat = hFlat;
	}
	public float getXStart(Texture text)
	{
		return (this.xSize + (this.xFlat/(float)text.getImageWidth()))*text.getWidth();
	}
	public float getYStart(Texture text)
	{
		return (this.ySize + (this.yFlat/(float)text.getImageHeight()))*text.getHeight();
	}
	public float getWidth(Texture text)
	{
		return (this.wSize + (this.wFlat/(float)text.getImageWidth()))*text.getWidth();
	}
	public float getHeight(Texture text)
	{
		return (this.hSize + (this.hFlat/(float)text.getImageHeight()))*text.getHeight();
	}
	public float getXEnd(Texture text)
	{
		return this.getXStart(text) + this.getWidth(text);
	}
	public float getYEnd(Texture text)
	{
		return this.getYStart(text) + this.getHeight(text);
	}
	public float[] inFloatArray(Texture text)
	{
		return new float[]{getXStart(text), getYStart(text), getXEnd(text), getYStart(text), getXEnd(text), getYEnd(text), getXStart(text), getYEnd(text)};
	}
}

