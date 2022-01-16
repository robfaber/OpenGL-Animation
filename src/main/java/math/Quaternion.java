package math;

public class Quaternion {
	private float x;
	private float y;
	private float z;
	private float w;

	public Quaternion() {
		reset();
	}

	public Quaternion(float x, float y, float z, float w) {
		set(x, y, z, w);
	}

	public Quaternion(float angle, Vector3 vec) {
		float s = (float)Math.sin(angle / 2);

		x = vec.x() * s;
		y = vec.y() * s;
		z = vec.z() * s;
		w = (float)Math.cos(angle / 2);
	}

	public Quaternion(Quaternion q) {
		set(q);
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ", " + w + ")";
	}

	public float x() {
		return x;
	}

	public Quaternion x(float x) {
		this.x = x;
		return this;
	}

	public float y() {
		return y;
	}

	public Quaternion y(float y) {
		this.y = y;
		return this;
	}

	public float z() {
		return z;
	}

	public Quaternion z(float z) {
		this.z = z;
		return this;
	}

	public float w() {
		return w;
	}

	public Quaternion w(float w) {
		this.w = w;
		return this;
	}

	public Quaternion set(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		return this;
	}

	public Quaternion set(Quaternion q) {
		return set(q.x, q.y, q.z, q.w);
	}

	public Quaternion reset() {
		x = 0;
		y = 0;
		z = 0;
		w = 1;
		return this;
	}

	public float length() {
		return (float)Math.sqrt(x * x + y * y + z * z + w * w);
	}

	public Quaternion normalize() {
		float length = 1f / length();
		x *= length;
		y *= length;
		z *= length;
		w *= length;
		return this;
	}

	public float dot(Quaternion q) {
		return x * q.x + y * q.y + z * q.z + w * q.w;
	}

	public Vector3 mult3(Vector3 v, Vector3 result) {
		Vector3 quatVector = new Vector3(x, y, z);

		Vector3 uv = quatVector.cross(v, new Vector3());
		Vector3 uuv = quatVector.cross(uv, new Vector3());

		uv.mult(w * 2);
		uuv.mult(2);

		return result.set(v).add(uv).add(uuv);
	}

	public Quaternion mult(Quaternion q) {
		float xx = w * q.x + x * q.w + y * q.z - z * q.y;
		float yy = w * q.y + y * q.w + z * q.x - x * q.z;
		float zz = w * q.z + z * q.w + x * q.y - y * q.x;
		float ww = w * q.w - x * q.x - y * q.y - z * q.z;

		x = xx;
		y = yy;
		z = zz;
		w = ww;

		return this;
	}

	public Quaternion conjugate() {
		x *= -1;
		y *= -1;
		z *= -1;

		return this;
	}

	public Quaternion inverse() {
		return normalize().conjugate();
	}

//	public Matrix4 toRotationMatrix() {
//		return toMatrix(new Matrix4());
//	}

	public Matrix4 toRotationMatrix() {
		Matrix4 matrix = new Matrix4();
		final float xy = x * y;
		final float xz = x * z;
		final float xw = x * w;
		final float yz = y * z;
		final float yw = y * w;
		final float zw = z * w;
		final float xSquared = x * x;
		final float ySquared = y * y;
		final float zSquared = z * z;
		matrix.put(0,0, 1 - 2 * (ySquared + zSquared));
		matrix.put(0,1,  2 * (xy - zw));
		matrix.put(0,2,  2 * (xz + yw));
		matrix.put(0,3, 0);
		matrix.put(1,0, 2 * (xy + zw));
		matrix.put(1,1, 1 - 2 * (xSquared + zSquared));
		matrix.put(1,2, 2 * (yz - xw));
		matrix.put(1,3, 0);
		matrix.put(2,0, 2 * (xz - yw));
		matrix.put(2,1, 2 * (yz + xw));
		matrix.put(2,2, 1 - 2 * (xSquared + ySquared));
		matrix.put(2,3, 0);
		matrix.put(3,0, 0);
		matrix.put(3,1, 0);
		matrix.put(3,2, 0);
		matrix.put(3,3, 1);
		return matrix;
	}

	/**
	 * Interpolates between two quaternion rotations and returns the resulting
	 * quaternion rotation. The interpolation method here is "nlerp", or
	 * "normalized-lerp". Another mnethod that could be used is "slerp", and you
	 * can see a comparison of the methods here:
	 * https://keithmaggio.wordpress.com/2011/02/15/math-magician-lerp-slerp-and-nlerp/
	 * 
	 * and here:
	 * http://number-none.com/product/Understanding%20Slerp,%20Then%20Not%20Using%20It/
	 * 
	 * @param a
	 * @param b
	 * @param blend
	 *            - a value between 0 and 1 indicating how far to interpolate
	 *            between the two quaternions.
	 * @return The resulting interpolated rotation in quaternion format.
	 */
	public static Quaternion interpolate(Quaternion a, Quaternion b, float blend) {
		Quaternion result = new Quaternion(0, 0, 0, 1);
		float dot = a.w * b.w + a.x * b.x + a.y * b.y + a.z * b.z;
		float blendI = 1f - blend;
		if (dot < 0) {
			result.w = blendI * a.w + blend * -b.w;
			result.x = blendI * a.x + blend * -b.x;
			result.y = blendI * a.y + blend * -b.y;
			result.z = blendI * a.z + blend * -b.z;
		} else {
			result.w = blendI * a.w + blend * b.w;
			result.x = blendI * a.x + blend * b.x;
			result.y = blendI * a.y + blend * b.y;
			result.z = blendI * a.z + blend * b.z;
		}
		result.normalize();
		return result;
	}
	
	/**
	 * Extracts the rotation part of a transformation matrix and converts it to
	 * a quaternion using the magic of maths.
	 * 
	 * More detailed explanation here:
	 * http://www.euclideanspace.com/maths/geometry/rotations/conversions/matrixToQuaternion/index.htm
	 * 
	 * @param matrix
	 *            - the transformation matrix containing the rotation which this
	 *            quaternion shall represent.
	 */
	public static Quaternion fromMatrix(Matrix4 matrix) {
		float w, x, y, z;
		float diagonal = matrix.get(0,0) + matrix.get(1,1) + matrix.get(2,2);
		if (diagonal > 0) {
			float w4 = (float) (Math.sqrt(diagonal + 1f) * 2f);
			w = w4 / 4f;
			x = (matrix.get(2,1) - matrix.get(1,2)) / w4;
			y = (matrix.get(0,2) - matrix.get(2,0)) / w4;
			z = (matrix.get(1,0) - matrix.get(0,1)) / w4;
		} else if ((matrix.get(0,0) > matrix.get(1,1)) && (matrix.get(0,0) > matrix.get(2,2))) {
			float x4 = (float) (Math.sqrt(1f + matrix.get(0,0) - matrix.get(1,1) - matrix.get(2,2)) * 2f);
			w = (matrix.get(2,1) - matrix.get(1,2)) / x4;
			x = x4 / 4f;
			y = (matrix.get(0,1) + matrix.get(1,0)) / x4;
			z = (matrix.get(0,2) + matrix.get(2,0)) / x4;
		} else if (matrix.get(1,1) > matrix.get(2,2)) {
			float y4 = (float) (Math.sqrt(1f + matrix.get(1,1) - matrix.get(0,0) - matrix.get(2,2)) * 2f);
			w = (matrix.get(0,2) - matrix.get(2,0)) / y4;
			x = (matrix.get(0,1) + matrix.get(1,0)) / y4;
			y = y4 / 4f;
			z = (matrix.get(1,2) + matrix.get(2,1)) / y4;
		} else {
			float z4 = (float) (Math.sqrt(1f + matrix.get(2,2) - matrix.get(0,0) - matrix.get(1,1)) * 2f);
			w = (matrix.get(1,0) - matrix.get(0,1)) / z4;
			x = (matrix.get(0,2) + matrix.get(2,0)) / z4;
			y = (matrix.get(1,2) + matrix.get(2,1)) / z4;
			z = z4 / 4f;
		}
		return new Quaternion(x, y, z, w);
	}
}