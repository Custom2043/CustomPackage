package drawer;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

import util.QuadColor;
import util.ScreenCoor;

public class ColoredScreenCoorModel extends Model
{
	public ColoredScreenCoorModel(ScreenCoor sc, QuadColor c)
	{
		this(new ScreenCoor[]{sc}, new QuadColor[]{c});
	}
	protected ColoredScreenCoorModel(ScreenCoor[] sc, QuadColor[] c)
	{
		super(sc.length*4);
		this.enableVertexArray(0);
		this.enableVertexArray(1);
		this.enableVertexArray(2);

		ByteBuffer buf = BufferUtils.createByteBuffer(this.vertexNumber * 12); // X
		for (ScreenCoor screenCoor : sc)
		{
			buf.putFloat(screenCoor.xScreen);
			buf.putFloat(screenCoor.xGui);
			buf.putFloat(screenCoor.xFlat);
			
			buf.putFloat(screenCoor.xScreen + screenCoor.wScreen);
			buf.putFloat(screenCoor.xGui + screenCoor.wGui);
			buf.putFloat(screenCoor.xFlat + screenCoor.wFlat);
			
			buf.putFloat(screenCoor.xScreen + screenCoor.wScreen);
			buf.putFloat(screenCoor.xGui + screenCoor.wGui);
			buf.putFloat(screenCoor.xFlat + screenCoor.wFlat);
			
			buf.putFloat(screenCoor.xScreen);
			buf.putFloat(screenCoor.xGui);
			buf.putFloat(screenCoor.xFlat);
		}
		VAOLoader.storeBufferInAttributeList(0, 3, buf, GL11.GL_FLOAT);
		
		buf = BufferUtils.createByteBuffer(this.vertexNumber * 12); // Y
		for (ScreenCoor screenCoor : sc)
		{
			buf.putFloat(screenCoor.yScreen);
			buf.putFloat(screenCoor.yGui);
			buf.putFloat(screenCoor.yFlat);
			
			buf.putFloat(screenCoor.yScreen);
			buf.putFloat(screenCoor.yGui);
			buf.putFloat(screenCoor.yFlat);
			
			buf.putFloat(screenCoor.yScreen + screenCoor.hScreen);
			buf.putFloat(screenCoor.yGui + screenCoor.hGui);
			buf.putFloat(screenCoor.yFlat + screenCoor.hFlat);
			
			buf.putFloat(screenCoor.yScreen + screenCoor.hScreen);
			buf.putFloat(screenCoor.yGui + screenCoor.hGui);
			buf.putFloat(screenCoor.yFlat + screenCoor.hFlat);
		}
		VAOLoader.storeBufferInAttributeList(1, 3, buf, GL11.GL_FLOAT);
		
		buf = BufferUtils.createByteBuffer(this.vertexNumber * 4); // Couleurs
		for (QuadColor qc : c)
			for (Color co : qc.getAsColorArray())
			{
				buf.put((byte)co.getRed());
				buf.put((byte)co.getGreen());
				buf.put((byte)co.getBlue());
				buf.put((byte)co.getAlpha());
			}
		VAOLoader.storeBufferInAttributeList(2, 4, buf, GL11.GL_UNSIGNED_BYTE);
	}
}

