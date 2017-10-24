package util;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import render.RawModel;

public class Loader {
	
	// Keep track of VAOs, VBOs, and textures so we can delete them on game close
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();

	public RawModel loadToVao(float[] positions) {
		// Load model data into OpenGL
		int vaoID = createVao();
		storeDataInAttributeList(0, 3, positions);
		unbindVao();
		return new RawModel(vaoID, positions.length / 3);
	}
	
	public void cleanUp() {
		// Destroy all VAOs
		for (int vao : vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		// Destroy all VBOs
		for (int vbo : vbos) {
			GL15.glDeleteBuffers(vbo);
		}
		// Destroy all textures
		for (int tex : textures) {
			GL11.glDeleteTextures(tex);
		}
	}
	
	private int createVao() {
		// Make a VAO on the GPU, bind it, and return the pointer
		int vaoID = GL30.glGenVertexArrays();
		// Add the VAO to the clean up list
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
		// Create and bind a VBO 
		int vboID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		// Add VBO to clean up
		vbos.add(vboID);
		// Store the data into the VBO
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		// Unbind the VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	private void unbindVao() {
		// Set VAO pointer to 0 therefore unbinding in case of rogue data writing
		GL30.glBindVertexArray(0);
	}
	
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		// Create a buffer and write data
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		// Flip and return for reading
		buffer.flip();
		return buffer;
	}
	
}
