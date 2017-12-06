package drawer;

import org.lwjgl.opengl.GL20;

public class TexturedScreenCoorShader extends ScreenCoorShader
{
	private int location_x, location_y, location_sampler;
	public TexturedScreenCoorShader(String vertexFile, String fragmentFile) {
		super(vertexFile, fragmentFile);
	}

	@Override
	protected void getAllUniformLocations() {
		super.getAllUniformLocations();
		this.location_x = super.getUniformLocation("xPos");
		this.location_y = super.getUniformLocation("yPos");
		this.location_sampler = super.getUniformLocation("modelTexture");
		this.start();
		GL20.glUniform1i(location_sampler, 0);
		ShaderProgram.stop();
	}

	public void loadPos(float x, float y)
	{
		super.loadFloat(this.location_x, x);super.loadFloat(this.location_y, y);
	}
}
