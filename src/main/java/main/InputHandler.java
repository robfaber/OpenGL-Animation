package main;

import lombok.extern.slf4j.Slf4j;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.Display;
import scene.ICamera;

@Slf4j
public final class InputHandler {
	private static final int KEYBOARD_SIZE = 512;
	private static final int MOUSE_SIZE = 16;

	private int[] keyStates = new int[KEYBOARD_SIZE];
	private boolean[] activeKeys = new boolean[KEYBOARD_SIZE];

	private int[] mouseButtonStates = new int[MOUSE_SIZE];
	private boolean[] activeMouseButtons = new boolean[MOUSE_SIZE];

//	private long lastMouseNS = 0;
//	private long mouseDoubleClickPeriodNS = 1000000000 / 5; // 5th of a second for double click.

	private long window;
	private boolean mouseLocked;
//	private Player player;
	private ICamera camera;

	private double mouseScrollX = 0;
	private double mouseScrollY = 0;
	private double mouseX = 0;
	private double mouseY = 0;

	public InputHandler(ICamera camera) {
		this.window = Display.getWindow();
		this.camera = camera;
		lockMouse();

		GLFW.glfwSetKeyCallback(window, this::handleKeyPress);
		GLFW.glfwSetMouseButtonCallback(window, this::handleMouseButton);
		GLFW.glfwSetCursorPosCallback(window, this::handleMouseMove);
		GLFW.glfwSetScrollCallback(window, this::handleMouseScroll);

//		resetKeyboard();
//		resetMouse();
	}

	/**
	 * 
	 * @param window
	 * @param key      any one of the GLFW_KEY_* constants
	 * @param scancode
	 * @param action   any one of GLFW_PRESS, GLFW_RELEASE of GLFW_REPEAT
	 * @param mods
	 */
	private void handleKeyPress(long window, int key, int scancode, int action, int mods) {
		log.debug("Key press key:{}, scancode:{}, action: {}, mods:{}", key, scancode, action, mods);
		activeKeys[key] = action != GLFW.GLFW_RELEASE;
		keyStates[key] = action;
	}

	private void handleMouseButton(long window, int button, int action, int mods) {
		activeMouseButtons[button] = action != GLFW.GLFW_RELEASE;
		mouseButtonStates[button] = action;
	}

	private void handleMouseScroll(long window, double xoffset, double yoffset) {
		log.debug("Scroll wheel xoffset={}, yoffset={}", xoffset, yoffset);
		mouseScrollX += xoffset;
		mouseScrollY += yoffset;
	}

	private void handleMouseMove(long window, double xpos, double ypos) {
		log.debug("Mouse move xpos={}, ypos={}", xpos, ypos);
		mouseX = (float)xpos;
		mouseY = (float)ypos;
	}

	/**
	 * Lock the mouse so the pointer becomes invisible and the movement of the mouse
	 * is handled by the player in the world
	 */
	private void lockMouse() {
		GLFW.glfwSetCursorPos(window, Display.getWidth() / 2, Display.getHeight() / 2);
		GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
		mouseLocked = true;
	}

	/**
	 * Unlock the mouse so the pointer becomes visible and the user can interact
	 * with the menu bar
	 */
	private void unlockMouse() {
		GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
		mouseLocked = false;

	}

	public boolean isKeyDown(int key) {
		return activeKeys[key];
	}

	public void update(float deltaTime) {
		GLFW.glfwPollEvents();
		
		if (mouseLocked) {
			if (activeKeys[GLFW.GLFW_KEY_ESCAPE]) {
				unlockMouse();
			}
//			if (activeKeys[GLFW.GLFW_KEY_W]) {
//				player.moveForward(deltaTime);
//			}
//			if (activeKeys[GLFW.GLFW_KEY_S]) {
//				player.moveBackward(deltaTime);
//			}
//			if (activeKeys[GLFW.GLFW_KEY_A]) {
//				player.moveLeft(deltaTime);
//			}
//			if (activeKeys[GLFW.GLFW_KEY_D]) {
//				player.moveRight(deltaTime);
//			}
//			if (activeKeys[GLFW.GLFW_KEY_SPACE]) {
//				player.jump();
//			}

			float dX = (float)((Display.getWidth() / 2) - mouseX);
			float dY = (float)((Display.getHeight() / 2) - mouseY);

//			if (activeMouseButtons[GLFW.GLFW_MOUSE_BUTTON_2]) {
				camera.move(dX, dY);
//				camera.rotate(deltaTime, dX);
//				camera.pitch(deltaTime, dY);
//				camera.zoom(deltaTime, (float)mouseScrollY);
//			} else {
//				player.rotate(deltaTime, dX);
//			}

			// If the mouse is locked we always return back to the middle of the screen
			mouseX = Display.getWidth() / 2;
			mouseY = Display.getHeight() / 2;
			GLFW.glfwSetCursorPos(window, mouseX, mouseY);
		} else {
			if (activeMouseButtons[GLFW.GLFW_MOUSE_BUTTON_1]) {
				lockMouse();
			}
		}

		//After we handled all interactions we reset the delta's for the next frame
		mouseScrollX = 0;
		mouseScrollY = 0;
	}
}