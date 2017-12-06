package custom.osu.util;

import custom.osu.drawer.Texture;
import custom.osu.main.MainActivity;

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
	public static TextureCoor scaleToScreen(Texture t)//Height et width of screen are inversed
    {
        float ratio = ((float)t.width) / t.height;
        if (ratio < ((float)MainActivity.getWidth()) / MainActivity.getHeight()) { // Screen larger than texture
            System.out.println("Larger");
            float height = MainActivity.getWidth() / ratio;
            float imageToScreenRatio = ((float)MainActivity.getWidth()) / t.width;
            return new TextureCoor(0, 0, 1, 0, 0, (height - MainActivity.getHeight()) / 2 / imageToScreenRatio, 0, MainActivity.getHeight() / imageToScreenRatio);
        }
        else { // Plus haut
            System.out.println("Higher");
            float width = MainActivity.getHeight() * ratio;
            float imageToScreenRatio = ((float)MainActivity.getHeight()) / t.height;
            return new TextureCoor(0, 0, 0, 1, (width - MainActivity.getWidth()) / 2 / imageToScreenRatio, 0, MainActivity.getWidth() / imageToScreenRatio, 0);
        }
    }
	public float getXStart(Texture text)
	{
		return (this.xSize + (this.xFlat/text.width));
	}
	public float getYStart(Texture text)
	{
		return (this.ySize + (this.yFlat/text.height));
	}
	public float getWidth(Texture text)
	{
		return (this.wSize + (this.wFlat/text.width));
	}
	public float getHeight(Texture text)
	{
		return (this.hSize + (this.hFlat/text.height));
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
        return new float[]{this.getXStart(text), this.getYStart(text), this.getXEnd(text), this.getYStart(text), this.getXEnd(text), this.getYEnd(text), this.getXStart(text), this.getYEnd(text)};
	}
}

