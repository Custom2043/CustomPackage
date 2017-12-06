package custom.osu.drawer;

import android.opengl.GLES11;

import java.nio.ByteBuffer;

import custom.osu.util.BufferUtils;
import custom.osu.util.Color;
import custom.osu.util.QuadColor;
import custom.osu.util.ScreenCoor;

public class ScreenCoorModel extends Model
{
    public ScreenCoorModel(ScreenCoor sc)
    {
        this(new ScreenCoor[]{sc});
    }
    public ScreenCoorModel(ScreenCoor[] sc) {
        super(sc.length * 4);
        this.enableVertexArray(0);
        this.enableVertexArray(1);
        this.enableVertexArray(2);

        ByteBuffer buf = BufferUtils.createByteBuffer(this.vertexNumber * 12); // X
        for (ScreenCoor screenCoor : sc) {
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
        VAOLoader.storeBufferInAttributeList(0, 3, buf, GLES11.GL_FLOAT);

        buf = BufferUtils.createByteBuffer(this.vertexNumber * 12); // Y
        for (ScreenCoor screenCoor : sc) {
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
        VAOLoader.storeBufferInAttributeList(1, 3, buf, GLES11.GL_FLOAT);
    }
}

