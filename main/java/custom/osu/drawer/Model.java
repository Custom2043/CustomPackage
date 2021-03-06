package custom.osu.drawer;

public abstract class Model
{
	private short usedVertexArray = 0;
	public final int vaoId, vertexNumber;
	public Texture texture = null;

	public Model(int vN)
	{
		this.vertexNumber = vN;

		this.vaoId = VAOLoader.createVAO();
		VAOLoader.setIndexBuffer(vN / 4);
	}
	public void enableVertexArray(int index)
	{
		this.usedVertexArray |= (1 << index);
	}

	public void disableVertexArray(int index)
	{
		this.usedVertexArray &= (0b1111111111111111 - (1 << index));
	}
	public boolean isVertexArrayEnabled(int index)
	{
		return ((this.usedVertexArray >> index) & 1) == 1 ? true : false;
	}
}
