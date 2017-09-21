package drawer;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public abstract class Matrix2DShader extends ShaderProgram
{
	private Vector3f screenData;
	private Matrix4f currentMatrix;
	private static Matrix4f trueMatrix;
	private static Vector3f trueScreenData;
	private int location_projectionMatrix, location_ScreenData;

	public Matrix2DShader(String vertexFile, String fragmentFile)
	{
		super(vertexFile, fragmentFile);
	}
	private void loadProjectionMatrix()
	{
		this.loadMatrix(this.location_projectionMatrix, this.currentMatrix);
	}
	private void loadScreenData()
	{
		this.loadVector(this.location_ScreenData, this.screenData);
	}
	@Override
	public void start()
	{
		super.start();
		if (this.currentMatrix != trueMatrix)
		{
			this.currentMatrix = trueMatrix;
			this.screenData = trueScreenData;
			this.loadProjectionMatrix();
			this.loadScreenData();
		}
	}
	@Override
	protected void getAllUniformLocations()
	{
		this.location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		this.location_ScreenData = super.getUniformLocation("screen");
	}
	public static void setMatrix(Matrix4f m){trueMatrix = m;}
	public static void setScreenData(int w, int h, int g){trueScreenData = new Vector3f(w,h,g);}
}
