package shaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public abstract class ShaderProgram {

	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	public ShaderProgram(String vertexFile, String fragmentFile) {
		// Initialize GL objects
		vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		// Attach shaders
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		// Compile
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		// Call on the children
		bindAttributes();
	}
	
	protected abstract void bindAttributes();
	
	protected void bindAttribute(int attribute, String variableName) {
		// Bind a VAO slot to an in variable in shader code
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}
	
	public void start() {
		// Activate this shader
		GL20.glUseProgram(programID);
	}
	
	public void stop() {
		// Activate no shader
		GL20.glUseProgram(0);
	}
	
	public void cleanUp() {
		// Delete memories
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}
	
	private static int loadShader(String file, int type) {
		// Set up file reader to load shader
		StringBuilder shaderSource = new StringBuilder();
		InputStreamReader isr = new InputStreamReader(Class.class.getResourceAsStream(file));
		BufferedReader reader = new BufferedReader(isr);
		try {
			String line;
			while ((line = reader.readLine()) != null) {
				// Read a line and consider it a line of shader source code
				shaderSource.append(line).append("\n");
			}
			reader.close();
		} catch (IOException e) {
			// Throw all the errors
			System.err.println("Shader reading machine broke");
			e.printStackTrace();
			System.exit(-1);
		}
		// Create shader in OpenGL
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			// If shader compiled wrong throw more errors
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 1024));
			System.err.println("Shader compiling machine broke");
			System.exit(-1);
		}
		return shaderID;
	}
	
}
