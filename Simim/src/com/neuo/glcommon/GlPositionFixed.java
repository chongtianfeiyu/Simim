package com.neuo.glcommon;

import com.neuo.common.MathUtil;

import android.opengl.Matrix;

public class GlPositionFixed extends GlPosition {
	protected float[] fixPosition = MathUtil.getInitStack();

	public void fixAndClear() {
		for (int i = 0; i < 16; i++) {
			fixPosition[i] = position[i];
		}
		reset();
	}
	
	public void resetFix() {
		MathUtil.setInitStack(fixPosition);
	}
	
	public void translateFix(float x, float y, float z) {
		Matrix.translateM(fixPosition, 0, x, y, z);
	}
	
	public void rotateFix(float a, float x, float y, float z) {
		Matrix.rotateM(fixPosition, 0, a, x, y, z);
	}
	
	public void scaleFix(float x, float y, float z) {
		Matrix.scaleM(fixPosition, 0, x, y, z);
	}

	public float[] getPostion() {
		Matrix.multiplyMM(tmpPosition, 0, position, 0, fixPosition, 0);
		return tmpPosition;
	}
}
