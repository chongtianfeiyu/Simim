package com.neuo.gl3D;

public class GlMove3DCamera extends GlCamera3D {
	
	protected float[] moveDir;
	
	public GlMove3DCamera() {
		super();
		init();
	}
	
	protected void init() {
		this.moveDir = new float[2];
	}
	
	public void setCameraPos(float x, float y, float z, boolean isCalcu) {
		setEyePos(x, y, z, false);
		setDesPos(x, y, z - 1, false);
		setUpDir(0, 1, 0, isCalcu);
	}
	
	public void setMoveDir(float x, float y) {
		moveDir[0] = x;
		moveDir[1] = y;
	}
	
	public void move(boolean isRevert, float ratio, boolean isCalcu) {
		float x = ratio * moveDir[0];
		float y = ratio * moveDir[1];
		setCameraPos(this.eyePos[0] + x, this.eyePos[1] + y, this.eyePos[2], isCalcu);
	}
	
	public float getLeftX(float z) {
		if (this.eyePos[2] <= z || this.eyePos[2] >= far || this.near == 0) {
			return 0f;
		} else {
			float ratio = (this.eyePos[2] - z) / this.near;
			return ratio * widthL + this.eyePos[0];
		}
	}
	
	public float getRightX(float z) {
		if (this.eyePos[2] <= z || this.eyePos[2] >= far || this.near == 0) {
			return 0f;
		} else {
			float ratio = (this.eyePos[2] - z) / this.near;
			return ratio * widthR + this.eyePos[0];
		}
	}
	
	public float getTopY(float z) {
		if (this.eyePos[2] <= z || this.eyePos[2] >= far || this.near == 0) {
			return 0f;
		} else {
			float ratio = (this.eyePos[2] - z) / this.near;
			return ratio * heightT + this.eyePos[1];
		}
	}
	
	public float getBottomY(float z) {
		if (this.eyePos[2] <= z || this.eyePos[2] >= far || this.near == 0) {
			return 0f;
		} else {
			float ratio = (this.eyePos[2] - z) / this.near;
			return ratio * heightB + this.eyePos[1];
		}
	}
}
