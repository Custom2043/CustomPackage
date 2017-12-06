package custom.osu.drawer;

import android.opengl.GLES11;

import java.nio.ByteBuffer;

import custom.osu.util.BufferUtils;
import custom.osu.util.Color;
import custom.osu.util.QuadColor;
import custom.osu.util.ScreenCoor;

public class ColoredSCModel extends ScreenCoorModel
{
	public ColoredSCModel(ScreenCoor sc, QuadColor c)
	{
		this(new ScreenCoor[]{sc}, new QuadColor[]{c});
	}
	public ColoredSCModel(ScreenCoor[] sc, QuadColor[] c)
	{
		super(sc);
		ByteBuffer buf = BufferUtils.createByteBuffer(this.vertexNumber * 4); // Couleurs
		for (QuadColor qc : c)
			for (Color co : qc.getAsColorArray())
			{
				buf.put(co.getRed());
				buf.put(co.getGreen());
				buf.put(co.getBlue());
				buf.put(co.getAlpha());
			}
		VAOLoader.storeBufferInAttributeList(2, 4, buf, GLES11.GL_UNSIGNED_BYTE);
	}
}

