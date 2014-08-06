package com.neuo.glcommon;

import java.nio.FloatBuffer;

import com.neuo.common.MathUtil;
import com.neuo.common.VectorUtil;

public class GlPosAttribute {
	protected FloatBuffer positionBuffer;
	
	protected FloatBuffer posNormalBuffer;
	
	protected int posModel;
	protected int posCount;
	
	protected FloatBuffer texCoorBuffer;
	protected float[] posCoor;
	protected float[] posScale = new float[] {1f, 1f, 1f};
	
	public void setTexCoor(float[] texCoor) {
		this.texCoorBuffer = VectorUtil.convertToBuffer(texCoor);
	}
	
	public float[] getPosScale() {
		return posScale;
	}
	
	public void setPosScale(float x, float y, float z) {
		posScale[0] = x;
		posScale[1] = y;
		posScale[2] = z;
	}
	
	public FloatBuffer getTexCoorBuffer() {
		return texCoorBuffer;
	}
	
	public void setPositionNormal(float[] normal) {
		posNormalBuffer = VectorUtil.convertToBuffer(normal);
	}
	
	public FloatBuffer getPosNormalBuffer()
	{
		return posNormalBuffer;
	}
	
	public void setPosition(float[] position) {
		this.posCount = position.length / 3;
		posCoor = MathUtil.getRectangleByPoints3D(position, 0, 1, 2, position.length - 1, 3);
		positionBuffer = VectorUtil.convertToBuffer(position);
		setPosScale(1f, 1f, 1f);
	}
	
	public float[] getPosCoor() {
		return posCoor;
	}
	
	public void setPosModel(int model) {
		posModel = model;
	}
	
	public int getPosCount() {
		return this.posCount;
	}
	
	public int getPosModel() {
		return posModel;
	}
	
	public FloatBuffer getPostionBuffer() {
		return positionBuffer;
	}
}
