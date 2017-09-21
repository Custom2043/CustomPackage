package drawer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public abstract class ShaderProgram {

	private static String vertexFolder = "", fragmentFolder = "";

	private static ArrayList<ShaderProgram> created = new ArrayList<ShaderProgram>();

	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;

	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

	public static void setVertexFolder(String folder){vertexFolder = folder;}
	public static void setFragmentFolder(String folder){fragmentFolder = folder;}

	public ShaderProgram(String vertexFile,String fragmentFile){
		this.vertexShaderID = loadShader(ShaderProgram.vertexFolder+vertexFile,GL20.GL_VERTEX_SHADER);
		this.fragmentShaderID = loadShader(ShaderProgram.fragmentFolder+fragmentFile,GL20.GL_FRAGMENT_SHADER);
		this.programID = GL20.glCreateProgram();
		GL20.glAttachShader(this.programID, this.vertexShaderID);
		GL20.glAttachShader(this.programID, this.fragmentShaderID);
		this.bindAttributes();
		GL20.glLinkProgram(this.programID);
		GL20.glValidateProgram(this.programID);
		this.getAllUniformLocations();
		created.add(this);
	}
	protected abstract void getAllUniformLocations();

	protected int getUniformLocation(String uniformName){
		return GL20.glGetUniformLocation(this.programID,uniformName);
	}
	public static void quit()
	{
		for (ShaderProgram sp : created)
		{
			ShaderProgram.stop();
			GL20.glDetachShader(sp.programID, sp.vertexShaderID);
			GL20.glDetachShader(sp.programID, sp.fragmentShaderID);
			GL20.glDeleteShader(sp.vertexShaderID);
			GL20.glDeleteShader(sp.fragmentShaderID);
			GL20.glDeleteProgram(sp.programID);
		}
	}

	public void start(){
		GL20.glUseProgram(this.programID);
	}

	public static void stop(){
		GL20.glUseProgram(0);
	}

	protected abstract void bindAttributes();

	protected void bindAttribute(int attribute, String variableName){
		GL20.glBindAttribLocation(this.programID, attribute, variableName);
	}

	protected void loadFloat(int location, float value){
		GL20.glUniform1f(location, value);
	}

	protected void loadVector(int location, Vector3f vector){
		GL20.glUniform3f(location,vector.x,vector.y,vector.z);
	}

	protected void loadVector2f(int location, Vector2f vector){
		GL20.glUniform2f(location,vector.x,vector.y);
	}

	protected void loadBoolean(int location, boolean value){
		GL20.glUniform1f(location, value ? 1:0);
	}

	/*protected void loadScreenCoor(int loX, int loY, int loW, int loH, ScreenCoor sc)
	{
		GL20.glUniform3f(loX,sc.xScreen,sc.xGui,sc.xFlat);
		GL20.glUniform3f(loY,sc.yScreen,sc.yGui,sc.yFlat);
		GL20.glUniform3f(loW,sc.wScreen,sc.wGui,sc.wFlat);
		GL20.glUniform3f(loH,sc.hScreen,sc.hGui,sc.hFlat);
	}  Supposed to be in the model, no shader*/

	protected void loadMatrix(int location, Matrix4f matrix){
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4(location, false, matrixBuffer);
	}

	private static int loadShader(String file, int type){
		StringBuilder shaderSource = new StringBuilder();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = reader.readLine())!=null)
				shaderSource.append(line).append("//\n");
			reader.close();
		}catch(IOException e){
			e.printStackTrace();
			System.exit(-1);
		}
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE){
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader!");
			System.exit(-1);
		}
		return shaderID;
	}

}
