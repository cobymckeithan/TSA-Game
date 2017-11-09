package main;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;

import render.Display;
import render.RawModel;
import render.Renderer;
import shaders.StaticShader;
import util.Loader;

public class MainGameLoop {

	public static void main(String[] args) {
		// Print LWJGL version
		System.out.println("Running LWJGL " + Version.getVersion());
		// Create a display
		Display d = new Display();
		
		// Instance of loader utility and rendering objects
		Loader loader = new Loader();
		Renderer renderer = new Renderer();
		StaticShader shader = new StaticShader();
		
		// Set up the model
		float[] positions = { -0.5f, 0.5f, 0f, -0.5f, -0.5f, 0f, 0.5f, -0.5f, 0f, 0.5f, 0.5f, 0f };
		int[] indices = { 0, 1, 3, 3, 1, 2 };
		RawModel model = loader.loadToVao(positions, indices);
		
		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the escape key
		while (!GLFW.glfwWindowShouldClose(d.window)) {
			renderer.prepare();
			renderer.render(model, shader);
			d.update();
		}
		
		// Destroy the window and clean up memory
		shader.cleanUp();
		d.destroy();
	}
	
}
