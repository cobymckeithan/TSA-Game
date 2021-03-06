package render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import shaders.ShaderProgram;

public class Renderer {

	public void prepare() {
		// Reset screen
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
	}
	
	public void render(RawModel model, ShaderProgram shader) {
		// Start shader
		shader.start();
		// Bind array and enable the attributes we need
		GL30.glBindVertexArray(model.vaoID);
		GL20.glEnableVertexAttribArray(0);
		// Draw
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.vertexNumber, GL11.GL_UNSIGNED_INT, 0);
		// Unbind array and disable the attributes
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		// Stop shader
		shader.stop();
	}
	
}
