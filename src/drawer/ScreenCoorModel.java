package drawer;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import util.QuadColor;
import util.ScreenCoor;
import util.TextureCoor;

public class ScreenCoorModel extends ColoredScreenCoorModel
{
	public ScreenCoorModel(ScreenCoor sc, QuadColor c, TextureCoor tc, Texture text)
	{
		this(new ScreenCoor[]{sc}, new QuadColor[]{c}, new TextureCoor[]{tc}, text);
	}
	public ScreenCoorModel(ScreenCoor[] sc, QuadColor[] c, TextureCoor[] tc, Texture text)
	{
		super(sc, c);
		this.texture = text;
		this.enableVertexArray(3);

		ByteBuffer buf = BufferUtils.createByteBuffer(this.vertexNumber * 8); // Textures
		if (tc != null && this.texture != null)
		for (TextureCoor t : tc)
			if (t != null)
				for (float i : t.inFloatArray(this.texture))
					buf.putFloat(i);
			else
			{	buf.putFloat(0);buf.putFloat(0);buf.putFloat(0);buf.putFloat(0);
				buf.putFloat(0);buf.putFloat(0);buf.putFloat(0);buf.putFloat(0);}
		VAOLoader.storeBufferInAttributeList(3, 2, buf, GL11.GL_FLOAT);
	}
}
