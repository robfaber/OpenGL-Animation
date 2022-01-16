package main;

import lombok.extern.slf4j.Slf4j;
import org.lwjgl.opengl.Display;
import math.Matrix4;
import math.Vector3;

import scene.ICamera;
import utils.DisplayManager;
import utils.SmoothFloat;

/**
 * Represents the in-game camera. This class is in charge of keeping the
 * projection-view-matrix updated. It allows the user to alter the pitch and yaw
 * with the left mouse button.
 * 
 * @author Karl
 *
 */
@Slf4j
public class Camera implements ICamera {

	private static final float PITCH_SENSITIVITY = 0.3f;
	private static final float YAW_SENSITIVITY = 0.3f;
	private static final float MAX_PITCH = 90;

	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.2f;
	private static final float FAR_PLANE = 400;

	private static final float Y_OFFSET = 5;

	private Matrix4 projectionMatrix;
	private Matrix4 viewMatrix = new Matrix4();

	private Vector3 position = new Vector3(0, 0, 0);

	private float yaw = 0;
	private SmoothFloat pitch = new SmoothFloat(10, 10);
	private SmoothFloat angleAroundPlayer = new SmoothFloat(0, 10);
	private SmoothFloat distanceFromPlayer = new SmoothFloat(10, 5);

	public Camera() {
		this.projectionMatrix = createProjectionMatrix();
	}

	@Override
	public void move(float dx, float dy) {
		log.debug("Move {},{}", dx, dy);
		calculatePitch(dy);
		calculateAngleAroundPlayer(dx);
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);
		this.yaw = 360 - angleAroundPlayer.get();
		yaw %= 360;
		updateViewMatrix();
	}

	@Override
	public Matrix4 getViewMatrix() {
		return viewMatrix;
	}

	@Override
	public Matrix4 getProjectionMatrix() {
		return projectionMatrix;
	}

	@Override
	public Matrix4 getProjectionViewMatrix() {
		return new Matrix4(projectionMatrix).mult(viewMatrix);
	}

	private void updateViewMatrix() {
		viewMatrix.clearToIdentity();
		viewMatrix.rotate((float) Math.toRadians(pitch.get()), new Vector3(1, 0, 0));
		viewMatrix.rotate((float) Math.toRadians(yaw), new Vector3(0, 1, 0));
		Vector3 negativeCameraPos = new Vector3(-position.x, -position.y, -position.z);
		viewMatrix.translate(negativeCameraPos);
	}

	
	private static Matrix4 createProjectionMatrix() {
		return new Matrix4().clearToPerspectiveDeg(
		      FOV, Display.getWidth(), Display.getHeight(),
		      NEAR_PLANE, FAR_PLANE
		);
	}
//	private static Matrix4 createProjectionMatrix() {
//		Matrix4 projectionMatrix = new Matrix4();
//		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
//		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
//		float x_scale = y_scale / aspectRatio;
//		float frustum_length = FAR_PLANE - NEAR_PLANE;
//
//		projectionMatrix.m00 = x_scale;
//		projectionMatrix.m11 = y_scale;
//		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
//		projectionMatrix.m23 = -1;
//		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
//		projectionMatrix.m33 = 0;
//		return projectionMatrix;
//	}

	private void calculateCameraPosition(float horizDistance, float verticDistance) {
		float theta = angleAroundPlayer.get();
		position.x = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
		position.y = verticDistance + Y_OFFSET;
		position.z = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
	}

	/**
	 * @return The horizontal distance of the camera from the origin.
	 */
	private float calculateHorizontalDistance() {
		return (float) (distanceFromPlayer.get() * Math.cos(Math.toRadians(pitch.get())));
	}

	/**
	 * @return The height of the camera from the aim point.
	 */
	private float calculateVerticalDistance() {
		return (float) (distanceFromPlayer.get() * Math.sin(Math.toRadians(pitch.get())));
	}

	/**
	 * Calculate the pitch and change the pitch if the user is moving the mouse
	 * up or down with the LMB pressed.
	 */
	public void calculatePitch(float dy) {
		if (Math.abs(dy) > 0) {
			float pitchChange = dy * PITCH_SENSITIVITY;
			pitch.increaseTarget(-pitchChange);
			clampPitch();
		}
		pitch.update(DisplayManager.getFrameTime());
	}

	/**
	 * Calculate the angle of the camera around the player (when looking down at
	 * the camera from above). Basically the yaw. Changes the yaw when the user
	 * moves the mouse horizontally with the LMB down.
	 */
	public void calculateAngleAroundPlayer(float dx) {
		if (Math.abs(dx) > 0) {
			float angleChange = dx * YAW_SENSITIVITY;
			angleAroundPlayer.increaseTarget(-angleChange);
		}
		angleAroundPlayer.update(DisplayManager.getFrameTime());
	}

	/**
	 * Ensures the camera's pitch isn't too high or too low.
	 */
	private void clampPitch() {
		if (pitch.getTarget() < 0) {
			pitch.setTarget(0);
		} else if (pitch.getTarget() > MAX_PITCH) {
			pitch.setTarget(MAX_PITCH);
		}
	}

}
