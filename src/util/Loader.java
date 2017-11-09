package util;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import render.RawModel;

public class Loader {
	
	// Keep track of VAOs, VBOs, and textures so we can delete them on game close
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();

	public RawModel loadToVao(float[] positions, int[] indices) {
		// Load model data into OpenGL
		int vaoID = createVao();
		bindIndexBuffer(indices);
		storeDataInAttributeList(0, 3, positions);
		unbindVao();
		return new RawModel(vaoID, indices.length);
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
	
	public int loadTexture(String path) {
		// TODO This seems hacky, see if there's a better way
		String fullPath = Class.class.getResource(path).getPath();
		File tempFile = new File(fullPath);
		fullPath = tempFile.getAbsolutePath();
		// %20s are spaces
		fullPath = fullPath.replace("%20", " ");

		ByteBuffer image;
		int width, height;
		try (MemoryStack stack = MemoryStack.stackPush()) {
			// Prepare image buffers
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer comp = stack.mallocInt(1);

			// Load image
			STBImage.stbi_set_flip_vertically_on_load(true);
			image = STBImage.stbi_load(fullPath, w, h, comp, 4);
			if (image == null) {
				throw new RuntimeException("Failed to load a texture file!\n"
						+ STBImage.stbi_failure_reason());
			}

			// Get width and height of image
			width = w.get();
			height = h.get();
		}

		// Create the image in VRAM and return texture id
		int id = setTextureData(width, height, image);
		textures.add(id);
		return id;
	}

	public int setTextureData(int width, int height, ByteBuffer image) {
		// Generate a texture to write to
		int id = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
		// Set texture parameters
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S,
				GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T,
				GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
				GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
				GL11.GL_NEAREST);
		// Upload texture to GPU
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height,
				0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);

		return id;
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
	
	private void bindIndexBuffer(int[] indices) {
		// Gen index fbo
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		// Store array in buffer
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer indexBuffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL15.GL_STATIC_DRAW);
	}
	
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		// Create a buffer and write data
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		// Flip and return for reading
		buffer.flip();
		return buffer;
	}
	
	private IntBuffer storeDataInIntBuffer(int[] data) {
		// See floatbuffer page for documentation
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
}
