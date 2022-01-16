package scene;

import math.Matrix4;

public interface ICamera {
	
	public Matrix4 getViewMatrix();
	public Matrix4 getProjectionMatrix();
	public Matrix4 getProjectionViewMatrix();
	public void move(float dx, float dy);

}
