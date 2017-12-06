package util;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
@SuppressWarnings("serial")
public class Matrix extends Matrix4f 
{
    public static Matrix4f createOrthographicMatrix() {
        Matrix4f m = new Matrix4f();
        m.m00 = 2f/ Display.getWidth();
        m.m11 = -2f/Display.getHeight();
        m.m22 = 1;
        m.m30 = -1;
        m.m31 = 1;
        m.m32 = 0;
        return m;
    }
}