package com.neuo.glcommon;

import java.util.ArrayList;
import java.util.Iterator;

import com.neuo.common.MathUtil;

import android.opengl.Matrix;

public class GlPosition {
	protected static final float[] defaultPosion = MathUtil.getInitStack();
	protected float[] position = defaultPosion.clone();
	protected float[] tmpPosition = new float[16];
	protected ArrayList<float[]> stack = new ArrayList<float[]>();
	protected int stackSize = 0;
	protected int stackLen = 0;

	public GlPosition() {
		
	}
	
	public GlPosition(GlPosition that) {
		this.position = that.position.clone();
		this.stackSize = that.stackLen;
		this.stackLen = that.stackLen;
		
		for (Iterator<float[]> iterator = that.stack.iterator();iterator.hasNext();) {
			this.stack.add(iterator.next());
		}
	}
	
	public GlPosition clone() {
		return new GlPosition(this);
	}
	
	public void clear() {
		stack.clear();
		stackLen = 0;
		stackSize = 0;
	}
	
	public void pop() {
		if (stackLen > 0) {
			float[] tmp = stack.get(stackLen - 1);
			for (int i = 0; i < position.length; i++) {
				position[i] = tmp[i];
			}
			stackLen--;
		}
	}
	
	public void push() {
		if (stackLen < stackSize) {
			float[] tmp = stack.get(stackLen);
			for (int i = 0; i < position.length; i++) {
				tmp[i] = position[i];
			}
			stackLen++;
		} else {
			stack.add(position.clone());
			stackLen++;
			stackSize++;
		}
	}
	
	public float[] getPostion() {
		return position;
	}
	
	public void reset() {
		MathUtil.setInitStack(position);
	}
	
	public void translate(float x, float y, float z) {
		Matrix.translateM(position, 0, x, y, z);
	}
	
	public void rotate(float a, float x, float y, float z) {
		Matrix.rotateM(position, 0, a, x, y, z);
	}
	
	public void scale(float x, float y, float z) {
		Matrix.scaleM(position, 0, x, y, z);
	}
	
	public void normalMatrix(float[] norMatrix) {
		MathUtil.normalMatrix(getPostion(), norMatrix, tmpPosition);
	}
	
}
