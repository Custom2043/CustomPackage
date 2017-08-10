package drawer;

public class ScreenCoorShader extends ColoredScreenCoorShader
{

	public ScreenCoorShader(String vertexFile, String fragmentFile) {
		super(vertexFile, fragmentFile);
	}
	@Override
	protected void bindAttributes() {
		super.bindAttributes();
		super.bindAttribute(3, "textureCoordinates");
	}
}
