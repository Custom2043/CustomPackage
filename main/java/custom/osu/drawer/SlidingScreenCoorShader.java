package custom.osu.drawer;

public class SlidingScreenCoorShader extends ScreenCoorShader
{
    private int location_alpha, location_x, location_y;
    public SlidingScreenCoorShader(String vertexFile, String fragmentFile) {
        super(vertexFile, fragmentFile);
    }

    protected void getAllUniformLocations()
    {
        super.getAllUniformLocations();
        this.location_alpha = super.getUniformLocation("alpha");
        this.location_x = super.getUniformLocation("xx");
        this.location_y = super.getUniformLocation("yy");
    }

    public void loadAlpha(float alpha)
    {
        super.loadFloat(location_alpha, alpha);
    }
    public void loadX(float x)
    {
        super.loadFloat(location_x, x);
    }
    public void loadY(float y)
    {
        super.loadFloat(location_y, y);
    }
}
