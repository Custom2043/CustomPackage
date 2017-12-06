package custom.osu.drawer;

import android.opengl.GLES10;
import android.opengl.GLES11;
import android.opengl.GLES20;

import javax.microedition.khronos.opengles.GL10;

import custom.osu.gui.Gui;
import custom.osu.gui.GuiMainMenu;
import custom.osu.main.MainActivity;
import custom.osu.util.Color;
import custom.osu.util.Matrix4f;
import custom.osu.util.QuadColor;
import custom.osu.util.ScreenCoor;

public abstract class CustomDrawer
{
    private static TextureManager loader = new TextureManager();
    public static Texture bg = loader.loadTexture("mainMenu/menubg1.jpg"), logo = loader.loadTexture("mainMenu/logo.png"), welcomeText = loader.loadTexture("mainMenu/welcome.png"),
                        play = loader.loadTexture("mainMenu/boutons/play.png"),edit = loader.loadTexture("mainMenu/boutons/edit.png"), options = loader.loadTexture("mainMenu/boutons/options.png"),
                         exit = loader.loadTexture("mainMenu/boutons/exit.png");
    public static ScreenCoorShader texturedShader = new ScreenCoorShader("texturevertex.txt", "texturefragment.txt");
    public static SlidingScreenCoorShader alphatexturedShader = new SlidingScreenCoorShader("slidingtexturevertex.txt", "slidingtexturefragment.txt"),
            cuttexturedShader = new SlidingScreenCoorShader("cuttexturevertex.txt", "cuttexturefragment.txt");
    public static LogoShader logoShader = new LogoShader("logovertex.txt", "colorfragment.txt");
    public static LogoModel logoModel = new LogoModel(ScreenCoor.screenGui(.5f, .5f, 0, 0, 0, -3, 400, 6), new QuadColor(new Color(255, 255, 255, 251)));
    public static ScreenCoorShader getTexturedShader = new ScreenCoorShader("texturevertex.txt", "texturefragment.txt");

    public static AngelCodeFont fnt = new AngelCodeFont(loader, "fonts/test.fnt");

    private static Gui screen;

    public static void init()
    {
        logoShader.start();
        float[] f = new float[200];
        for (int i=0;i<f.length;i++)
            f[i] = i / 200f;
        logoShader.loadFFTDatas(f);
        Matrix2DShader.setMatrix(Matrix4f.createOrthographicMatrix());
        Matrix2DShader.setScreenData(MainActivity.getWidth(), MainActivity.getHeight(), 1);
        screen = new GuiMainMenu();
    }
	public static void drawModel(Model model)
	{
		drawModelPart(model, 0, model.vertexNumber);
	}
	public static void drawModelPart(Model model, int base, int length)
	{
        if (model.texture != null)
			model.texture.bind();

		VAOLoader.bind(model.vaoId);

		VAOLoader.VAO vao = VAOLoader.getCurrentVAO();
		for (int i=0;i<16;i++)
			if (model.isVertexArrayEnabled(i)) {
				GLES20.glEnableVertexAttribArray(i);
				GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vao.vbosIndex[i]);
				GLES20.glVertexAttribPointer(i,vao.coordinatesSize[i],vao.types[i],false,0,0);
			}

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, vao.indexBuffers);
        GLES11.glDrawElements(GL10.GL_TRIANGLES, length / 4 * 6, GLES10.GL_UNSIGNED_SHORT, 0);

		for (int i=0;i<16;i++)
			if (model.isVertexArrayEnabled(i))
				GLES20.glDisableVertexAttribArray(i);
	}
	public static void draw()
    {
        screen.draw();
    }
    public static Gui getScreen(){return screen;}
    public static void setScreen(Gui g){screen.quit(); screen = g;}
}
