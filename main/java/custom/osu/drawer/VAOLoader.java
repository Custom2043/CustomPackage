package custom.osu.drawer;

import android.opengl.GLES11;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL11;

import custom.osu.util.BufferUtils;

public class VAOLoader
{
	private static int currentVAO = 0;
	private static List<VAO> vaos = new ArrayList<VAO>();

	public static int createVAO()
	{
		currentVAO = vaos.size();
		vaos.add(new VAO());
		return currentVAO;
	}
	public static void setIndexBuffer(int numberOfQuads)
	{
		int[] ids = new int[1];
		GLES20.glGenBuffers(1, ids, 0);
		getCurrentVAO().indexBuffers = ids[0];

		ByteBuffer indexes = ByteBuffer.allocateDirect(numberOfQuads * 6 * 2);
		indexes.order(ByteOrder.nativeOrder());
        for (short i=0;i<numberOfQuads;i++)
        {
            indexes.putShort((short)(i*4+0));indexes.putShort((short)(i*4+1));indexes.putShort((short)(i*4+2));
            indexes.putShort((short)(i*4+0));indexes.putShort((short)(i*4+2));indexes.putShort((short)(i*4+3));
        } indexes.flip();

		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, ids[0]);
		GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, numberOfQuads * 6 * 2, indexes, GLES20.GL_STATIC_DRAW);
	}
	public static void storeDataInAttributeList(int attributeNumber, int coordinateSize,float[] data){
		int[] ids = new int[1];
		GLES20.glGenBuffers(1, ids, 0);
		vaos.get(currentVAO).vbosIndex[attributeNumber] = ids[0];
		vaos.get(currentVAO).coordinatesSize[attributeNumber] = coordinateSize;
		vaos.get(currentVAO).types[attributeNumber] = GLES11.GL_FLOAT;
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, ids[0]);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, data.length * 4, buffer, GLES20.GL_STATIC_DRAW);


	}
	public static void storeBufferInAttributeList(int attributeNumber, int coordinateSize, ByteBuffer buf, int type)
	{
		buf.flip();
		int[] ids = new int[1];
		GLES20.glGenBuffers(1, ids, 0);
		vaos.get(currentVAO).vbosIndex[attributeNumber] = ids[0];
		vaos.get(currentVAO).coordinatesSize[attributeNumber] = coordinateSize;
		vaos.get(currentVAO).types[attributeNumber] = type;
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, ids[0]);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, buf.capacity(), buf, GLES20.GL_STATIC_DRAW);
	}

	private static FloatBuffer storeDataInFloatBuffer(float[] data){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	public static void unbind()
	{
		currentVAO = 0;
	}
	public static void bind(int id) {currentVAO = id;}
	public static VAO getCurrentVAO(){return vaos.get(currentVAO);}
	public static class VAO
	{
		public int[] vbosIndex = new int[16];
		public int[] coordinatesSize= new int[16];
		public int[] types = new int[16];
 		public int indexBuffers;
	}
}
