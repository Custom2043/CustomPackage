package drawer;

public class StateScreenCoorShader extends ScreenCoorShader
{
	private int location_state;
	public StateScreenCoorShader(String vertexFile, String fragmentFile) {
		super(vertexFile, fragmentFile);
	}

	@Override
	protected void getAllUniformLocations() {
		super.getAllUniformLocations();
		this.location_state = super.getUniformLocation("state");
	}

	public void loadState(float value)
	{
		super.loadFloat(this.location_state, value);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttributes();
	}
}
