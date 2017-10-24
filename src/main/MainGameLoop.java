package main;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;

import render.Display;
import render.RawModel;
import render.Renderer;
import util.Loader;

public class MainGameLoop {

	public static void main(String[] args) {
		// Print LWJGL version
		System.out.println("Running LWJGL " + Version.getVersion());
		// Create a display
		Display d = new Display();
		
		// Instance of loader and renderer utilities
		Loader loader = new Loader();
		Renderer renderer = new Renderer();
		
		// Set up the model
		float[] positions = { -0.5f, 0.5f, 0f, -0.5f, -0.5f, 0f, 0.5f, 0.5f, 0f, 0.5f, 0.5f, 0f, -0.5f, -0.5f, 0f, 0.5f, -0.5f, 0f };
		RawModel model = loader.loadToVao(positions);
		
		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the escape key
		while (!GLFW.glfwWindowShouldClose(d.window)) {
			renderer.prepare();
			renderer.render(model);
			d.update();
		}
		
		// Destroy the window and clean up memory
		d.destroy();
	}
	
}
