package drawer;

public class ColoredScreenCoorShader extends Matrix2DShader
{
	public ColoredScreenCoorShader(String vertexFile, String fragmentFile)
	{
		super(vertexFile, fragmentFile);
	}
	@Override
	protected void bindAttributes()
	{
		super.bindAttribute(0, "x");
		super.bindAttribute(1, "y");
		super.bindAttribute(2, "color");
	}
}
