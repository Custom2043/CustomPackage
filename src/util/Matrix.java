package util;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;

public class Matrix 
{
	public static Matrix4f createOrthographicMatrix(float w, float h) {
		Matrix4f m = new Matrix4f();
		m.m00 = 2/w;
		m.m11 = 2/-h;
		m.m22 = 1;
		m.m30 = -1;
		m.m31 = 1;
		m.m32 = 0;
		return m;
	}
	public static Matrix4f createProjectionMatrix(float FOV){
		float FAR_PLANE = 100f;
		float NEAR_PLANE = 0.05f;
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV/2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;
        
        Matrix4f projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
        return projectionMatrix;
    }
}
