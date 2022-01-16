package shaders;

import org.lwjgl.opengl.GL20;
import math.Matrix4;

public class UniformMatrix extends Uniform {
	
	public UniformMatrix(String name) {
		super(name);
	}
	
	public void loadMatrix(Matrix4 matrix){
		GL20.glUniformMatrix4fv(super.getLocation(), false, matrix.toBuffer());
	}
}
