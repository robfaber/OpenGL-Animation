package utils;

import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {

	private static final String TITLE = "ThinMatrix Animation Tutorial";
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static final int FPS_CAP = 100;

	private static long lastFrameTime;
	private static float delta;

	public static void createDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setTitle(TITLE);
			ContextAttribs attribs = new ContextAttribs(3, 2).withProfileCore(true).withForwardCompatible(true);
			Display.create(new PixelFormat().withDepthBits(24).withSamples(4), attribs);
			Display.setInitialBackground(1, 1, 1);
			GL11.glEnable(GL13.GL_MULTISAMPLE);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Couldn't create display!");
			System.exit(-1);
		}
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		lastFrameTime = getCurrentTime();
	}

	public static void update() {
		Display.sync(FPS_CAP);
		Display.update();
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime) / 1_000_000_000f;
		lastFrameTime = currentFrameTime;
	}

	public static float getFrameTime() {
		return delta;
	}

	public static void closeDisplay() {
		Display.destroy();
	}

	private static long getCurrentTime() {
		return System.nanoTime();
	}
}
