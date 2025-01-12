package shaders;

import math.Matrix4;

public class UniformMat4Array extends Uniform{
	
	private UniformMatrix[] matrixUniforms;
	
	public UniformMat4Array(String name, int size) {
		super(name);
		matrixUniforms = new UniformMatrix[size];
		for(int i=0;i<size;i++){
			matrixUniforms[i] = new UniformMatrix(name + "["+i+"]");
		}
	}
	
	@Override
	protected void storeUniformLocation(int programID) {
		for(UniformMatrix matrixUniform : matrixUniforms){
			matrixUniform.storeUniformLocation(programID);
		}
	}

	public void loadMatrixArray(Matrix4[] matrices){
		for(int i=0;i<matrices.length;i++){
			matrixUniforms[i].loadMatrix(matrices[i]);
		}
	}
	
	

}
