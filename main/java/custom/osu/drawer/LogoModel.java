package custom.osu.drawer;

import android.opengl.GLES11;

import java.nio.ByteBuffer;

import custom.osu.util.BufferUtils;
import custom.osu.util.Color;
import custom.osu.util.QuadColor;
import custom.osu.util.ScreenCoor;

public class LogoModel extends ColoredSCModel
{
    private static QuadColor[] toColorArray(QuadColor qc, int to)
    {
        QuadColor[] c = new QuadColor[to];
        for (int i=0;i<to;i++)
            c[i] = qc;
        return c;
    }
    private static ScreenCoor[] toSCArray(ScreenCoor qc, int to)
    {
        ScreenCoor[] c = new ScreenCoor[to];
        for (int i=0;i<to;i++)
            c[i] = qc;
        return c;
    }
    public LogoModel(ScreenCoor sc, QuadColor qc)
    {
        this(toSCArray(sc, 200), toColorArray(qc, 200));
    }
    public LogoModel(ScreenCoor[] sc, QuadColor[] qc)
    {
        super(sc, qc);
        this.enableVertexArray(3);
        ByteBuffer buf = BufferUtils.createByteBuffer(this.vertexNumber); // frequence
        for (int i = 0; i < 200; i++)
            for (int j = 0; j < 4; j++)
                buf.put((byte) (i & 255));
        VAOLoader.storeBufferInAttributeList(3, 1, buf, GLES11.GL_UNSIGNED_BYTE);
    }
}
