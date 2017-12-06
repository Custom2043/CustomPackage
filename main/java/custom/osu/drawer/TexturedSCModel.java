package custom.osu.drawer;

import java.nio.ByteBuffer;

import javax.microedition.khronos.opengles.GL11;

import custom.osu.util.BufferUtils;
import custom.osu.util.QuadColor;
import custom.osu.util.ScreenCoor;
import custom.osu.util.TextureCoor;

public class TexturedSCModel extends ColoredSCModel
{
	public TexturedSCModel(ScreenCoor sc, QuadColor c, TextureCoor tc, Texture text)
	{
		this(new ScreenCoor[]{sc}, new QuadColor[]{c}, new TextureCoor[]{tc}, text);
	}
	public TexturedSCModel(ScreenCoor[] sc, QuadColor[] c, TextureCoor[] tc, Texture text)
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
                    for (float i : TextureCoor.allPicture.inFloatArray(this.texture))
                        buf.putFloat(i);
		VAOLoader.storeBufferInAttributeList(3, 2, buf, GL11.GL_FLOAT);
	}
}
