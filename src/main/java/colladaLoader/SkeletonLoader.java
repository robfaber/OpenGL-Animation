package colladaLoader;

import java.util.List;

import math.Matrix4;
import math.Vector3;

import dataStructures.JointData;
import dataStructures.SkeletonData;
import xmlParser.XmlNode;

public class SkeletonLoader {

	private XmlNode armatureData;
	
	private List<String> boneOrder;
	
	private int jointCount = 0;
	
	private static final Matrix4 CORRECTION = new Matrix4().rotate((float) Math.toRadians(-90), new Vector3(1, 0, 0));

	public SkeletonLoader(XmlNode visualSceneNode, List<String> boneOrder) {
		this.armatureData = visualSceneNode.getChild("visual_scene").getChildWithAttribute("node", "id", "Armature");
		this.boneOrder = boneOrder;
	}
	
	public SkeletonData extractBoneData(){
		XmlNode headNode = armatureData.getChild("node");
		JointData headJoint = loadJointData(headNode, true);
		return new SkeletonData(jointCount, headJoint);
	}
	
	private JointData loadJointData(XmlNode jointNode, boolean isRoot){
		JointData joint = extractMainJointData(jointNode, isRoot);
		for(XmlNode childNode : jointNode.getChildren("node")){
			joint.addChild(loadJointData(childNode, false));
		}
		return joint;
	}
	
	private JointData extractMainJointData(XmlNode jointNode, boolean isRoot){
		String nameId = jointNode.getAttribute("id");
		int index = boneOrder.indexOf(nameId);
		String[] matrixData = jointNode.getChild("matrix").getData().split(" ");
		Matrix4 matrix = new Matrix4(convertData(matrixData));
		matrix.transpose();
		if(isRoot){
			//because in Blender z is up, but in our game y is up.
			matrix = new Matrix4(CORRECTION).mult(matrix);
		}
		jointCount++;
		return new JointData(index, nameId, matrix);
	}
	
	private float[] convertData(String[] rawData){
		float[] matrixData = new float[16];
		for(int i=0;i<matrixData.length;i++){
			matrixData[i] = Float.parseFloat(rawData[i]);
		}
		return matrixData;
	}

}
