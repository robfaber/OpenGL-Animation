package org.lwjgl.opengl;

import static org.lwjgl.glfw.GLFW.*;

public class Display {
	
	private static DisplayMode DisplayMode;
	private static String Title;
	private static long window;

	public static long getWindow() {
		return window;
	}

	public static void setDisplayMode(DisplayMode displayMode) {
		DisplayMode = displayMode;
		
	}

	public static boolean isCloseRequested() {
		return glfwWindowShouldClose(window);
	}

	public static void setTitle(String title) {
		Title = title;
		
	}

	public static void setInitialBackground(int r, int g, int b) {
	    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	    GL11.glClearColor(r, g, b, 1);
		
	}

	public static void create(PixelFormat withSamples, ContextAttribs attribs) {
		glfwInit();


		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE); 
		window = glfwCreateWindow(DisplayMode.getWidth(), DisplayMode.getHeight(), Title, 0, 0);
		if (window == 0) {
		    throw new RuntimeException("Failed to create window");
		}
//		glfwSetWindowSizeCallback(window, new GLFWWindowSizeCallback() {
//			@Override
//			public void invoke(long window, int width, int height) {
//				WINDOW_WIDTH = width;
//				WINDOW_HEIGHT = height;
//				GL11.glViewport(0,  0, WINDOW_WIDTH, WINDOW_HEIGHT);
//			}
//		});

		glfwMakeContextCurrent(window);
		GL.createCapabilities();
		
		GL11.glViewport(0,  0, DisplayMode.getWidth(), DisplayMode.getHeight());
		
		glfwShowWindow(window);
	}

	public static void destroy() {
		glfwDestroyWindow(window);
	}

	public static void sync(int fpsCap) {
	}

	public static void update() {
		glfwPollEvents();
		glfwSwapBuffers(window);
	}

	public static float getWidth() {
		return DisplayMode.getWidth();
	}

	public static float getHeight() {
		return DisplayMode.getHeight();
	}

}
