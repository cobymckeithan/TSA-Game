package main;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import render.Display;

public class MainGameLoop {

	public static void main(String[] args) {
		// Print LWJGL version
		System.out.println("Running LWJGL " + Version.getVersion());
		// Create a display
		Display d = new Display();
		
		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key
		while (!GLFW.glfwWindowShouldClose(d.window)) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // Clear the window
			GLFW.glfwSwapBuffers(d.window); // Pop it out of memory and on the screen
			// Poll for GLFW events
			GLFW.glfwPollEvents();
		}
		
		// Destroy the window and clean up memory
		d.destroy();
	}
	
}
