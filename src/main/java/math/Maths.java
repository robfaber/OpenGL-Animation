package math;

public class Maths {
	public static Matrix4 createTransformationMatrix(Vector3 translation, float rx, float ry, float rz, float scale) {
		Matrix4 matrix = new Matrix4();
		matrix.translate(translation);
		matrix.rotateDeg(rx,  Vector3.RotX);
		matrix.rotateDeg(ry,  Vector3.RotY);
		matrix.rotateDeg(rz,  Vector3.RotZ);
		matrix.scale(scale);
		return matrix;
	}

//	public static Matrix4 createViewMatrix(Camera camera) {
//
//		Matrix4 matrix = new Matrix4();
//		matrix.rotateDeg(camera.getPitch(), Vector3.RotX);
//		matrix.rotateDeg(camera.getYaw(),   Vector3.RotY);
//		Vector3 cameraPos = new Vector3(camera.getPosition()).mult(-1);
//		
//		matrix.translate(cameraPos);
//		return matrix;
//	}
	
	public static float barryCentric(Vector3 p1, Vector3 p2, Vector3 p3, Vector2 pos) {
		float det = (p2.z() - p3.z()) * (p1.x() - p3.x()) + (p3.x() - p2.x()) * (p1.z() - p3.z());
		float l1 = ((p2.z() - p3.z()) * (pos.x() - p3.x()) + (p3.x() - p2.x()) * (pos.y() - p3.z())) / det;
		float l2 = ((p3.z() - p1.z()) * (pos.x() - p3.x()) + (p1.x() - p3.x()) * (pos.y() - p3.z())) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y() + l2 * p2.y() + l3 * p3.y();
	}
	
	public static Matrix4 createTransformationMatrix(Vector2 translation, Vector2 scale) {
		Matrix4 matrix = new Matrix4();
		matrix.translate(translation.x(), translation.y(), 0);
		matrix.scale(new Vector3(scale.x(), scale.y(), 1f));
		return matrix;
	}
}
