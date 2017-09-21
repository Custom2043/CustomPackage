package drawer;

import org.lwjgl.util.vector.Matrix4f;

public abstract class Matrix3DShader extends ShaderProgram
{
	private int location_projectionMatrix, location_viewMatrix;
	public Matrix3DShader(String vertexFile, String fragmentFile)
	{
		super(vertexFile, fragmentFile);
	}
	@Override
	protected void getAllUniformLocations()
	{
		this.location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		this.location_viewMatrix = super.getUniformLocation("viewMatrix");
	}
	public void loadProjectionMatrix(Matrix4f m)
	{
		this.loadMatrix(this.location_projectionMatrix, m);
	}
	public void loadViewMatrix(Matrix4f m)
	{
		this.loadMatrix(this.location_viewMatrix, m);
	}
}
