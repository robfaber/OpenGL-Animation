package org.lwjgl.opengl;

import lombok.extern.slf4j.Slf4j;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_RENDERER;
import static org.lwjgl.opengl.GL11.GL_VENDOR;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL20.GL_SHADING_LANGUAGE_VERSION;

@Slf4j
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

		glfwShowWindow(window);

		FloatBuffer xBuffer = BufferUtils.createFloatBuffer(1);
		FloatBuffer yBuffer = BufferUtils.createFloatBuffer(1);
		glfwGetWindowContentScale(window, xBuffer, yBuffer);
		int xscale = (int) xBuffer.get();
		int yscale = (int) yBuffer.get();
		log.info("xscale: {} yscale: {}", xscale, yscale);

		org.lwjgl.opengl.DisplayMode mode = new DisplayMode(DisplayMode.getWidth()* xscale, DisplayMode.getHeight()* yscale);
		Display.setDisplayMode(mode);

		GL.createCapabilities();
		glViewport(0, 0, DisplayMode.getWidth(), DisplayMode.getHeight());
		log.info("Vendor: {}", glGetString(GL_VENDOR));
		log.info("Version: {}", glGetString(GL_VERSION));
		log.info("Renderer: {}", glGetString(GL_RENDERER));
		log.info("Shading language: {}", glGetString(GL_SHADING_LANGUAGE_VERSION));

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
