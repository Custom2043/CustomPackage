package custom.osu.drawer;

import android.opengl.GLES20;

public class ScreenCoorShader extends ColoredScreenCoorShader
{
    private int location_sampler;
	public ScreenCoorShader(String vertexFile, String fragmentFile) {
		super(vertexFile, fragmentFile);
	}
	@Override
	protected void bindAttributes() {
		super.bindAttributes();
		super.bindAttribute(3, "textureCoordinates");
	}

    protected void getAllUniformLocations()
    {
        super.getAllUniformLocations();
        this.location_sampler = super.getUniformLocation("modelTexture");
        this.start();
        GLES20.glUniform1i(this.location_sampler, 0);
        ShaderProgram.stop();
    }
}
