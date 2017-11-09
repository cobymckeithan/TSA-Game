package shaders;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.lwjgl.opengl.GL20;

public class ShaderProgram {

	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	private static int loadShader(String file, int type) {
		StringBuilder shaderSource = new StringBuilder();
		InputStreamReader isr = new InputStreamReader(Class.class.getResourceAsStream(file));
		BufferedReader reader = new BufferedReader(isr);
		int shaderID = GL20.glCreateShader(type);
		
	}
	
}
