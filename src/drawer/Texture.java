package drawer;

import static org.lwjgl.opengl.GL11.*;

public class Texture
{
	private final int id;
	public final int width, height;

	public Texture(int id, int width, int height)
	{
		this.id = id;
		this.width = width;
		this.height = height;
		this.bind();
	}
	public void bind()
	{
		glBindTexture(GL_TEXTURE_2D, this.id);
	}
	public static void unbind()
	{
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	public void release()
	{
		glDeleteTextures(id);
	}
}
