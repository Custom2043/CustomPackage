package custom.osu.drawer;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import custom.osu.util.Color;
import custom.osu.util.QuadColor;
import custom.osu.util.ScreenCoor;
import custom.osu.util.Vector2f;

public class LogoShader extends ColoredScreenCoorShader
{
    private int location_fft;
    public LogoShader(String vertexFile, String fragmentFile)
    {
        super(vertexFile, fragmentFile);
    }
    @Override
    protected void getAllUniformLocations()
    {
        super.getAllUniformLocations();
        this.location_fft = super.getUniformLocation("fft");
    }
    public void loadFFTDatas(float[] fft)
    {
        GLES20.glUniform1fv(location_fft, 200, fft, 0);
    }
    @Override
    public void bindAttributes()
    {
        super.bindAttributes();
        this.bindAttribute(3, "freq");
    }
}
