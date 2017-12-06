package custom.osu.drawer;

import android.opengl.GLES20;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL11;

import custom.osu.main.MainActivity;
import custom.osu.util.BufferUtils;
import custom.osu.util.Matrix4f;
import custom.osu.util.Vector2f;
import custom.osu.util.Vector3f;

public abstract class ShaderProgram {

	private static ArrayList<ShaderProgram> created = new ArrayList<ShaderProgram>();

	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;

	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

	public ShaderProgram(String vertexFile,String fragmentFile){
		this.vertexShaderID = loadShader(vertexFile,GLES20.GL_VERTEX_SHADER);
		this.fragmentShaderID = loadShader(fragmentFile,GLES20.GL_FRAGMENT_SHADER);
		this.programID = GLES20.glCreateProgram();
		GLES20.glAttachShader(this.programID, this.vertexShaderID);
		GLES20.glAttachShader(this.programID, this.fragmentShaderID);
		this.bindAttributes();
		GLES20.glLinkProgram(this.programID);
		GLES20.glValidateProgram(this.programID);
		this.getAllUniformLocations();
		created.add(this);
	}
	protected abstract void getAllUniformLocations();

	protected int getUniformLocation(String uniformName){
		return GLES20.glGetUniformLocation(this.programID,uniformName);
	}
	public void start(){
		GLES20.glUseProgram(this.programID);
	}

	public static void stop(){
		GLES20.glUseProgram(0);
	}

	protected abstract void bindAttributes();

	protected void bindAttribute(int attribute, String variableName){
		GLES20.glBindAttribLocation(this.programID, attribute, variableName);
	}

	protected void loadFloat(int location, float value){
		GLES20.glUniform1f(location, value);
	}

	protected void loadVector(int location, Vector3f vector){
        GLES20.glUniform3f(location,vector.x,vector.y,vector.z);
    }

    protected void loadVector4f(int location, float x, float y, float z, float w){
        GLES20.glUniform4f(location,x, y, z, w);
    }

	protected void loadVector2f(int location, Vector2f vector){
		GLES20.glUniform2f(location,vector.x,vector.y);
	}

	protected void loadBoolean(int location, boolean value){
		GLES20.glUniform1f(location, value ? 1:0);
	}

	protected void loadMatrix(int location, Matrix4f matrix){
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GLES20.glUniformMatrix4fv(location, 1, false, matrixBuffer);
	}

	private static int loadShader(String file, int type){
		StringBuilder shaderSource = new StringBuilder();
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(MainActivity.getAsset("shaders/"+file)));
			String line;
			while((line = reader.readLine())!=null)
				shaderSource.append(line).append("//\n");
			reader.close();
		}catch(IOException e){
			e.printStackTrace();
			System.exit(-1);
		}
		int shaderID = GLES20.glCreateShader(type);
		GLES20.glShaderSource(shaderID, shaderSource.toString());
		GLES20.glCompileShader(shaderID);
		int[] shaderOK = new int[1];
		GLES20.glGetShaderiv(shaderID, GLES20.GL_COMPILE_STATUS, shaderOK, 0);
		if(shaderOK[0] == GL11.GL_FALSE){
			System.out.println(GLES20.glGetShaderInfoLog(shaderID));
			System.err.println("Could not compile shader!");
			System.exit(-1);
		}
		return shaderID;
	}

}
