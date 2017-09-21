package drawer;

public class TexturedScreenCoorShader extends ScreenCoorShader
{
	private int location_x, location_y;
	public TexturedScreenCoorShader(String vertexFile, String fragmentFile) {
		super(vertexFile, fragmentFile);
	}

	@Override
	protected void getAllUniformLocations() {
		super.getAllUniformLocations();
		this.location_x = super.getUniformLocation("xPos");
		this.location_y = super.getUniformLocation("yPos");
	}

	public void loadPos(float x, float y)
	{
		super.loadFloat(this.location_x, x);super.loadFloat(this.location_y, y);
	}
}
