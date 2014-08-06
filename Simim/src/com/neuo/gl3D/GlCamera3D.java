package com.neuo.gl3D;

import com.neuo.glcommon.GlCamera;

public class GlCamera3D extends GlCamera {
	protected boolean hasChange1 = true;
	protected boolean hasChange2 = true;
	protected float[] eyePos  = new float[]{0f, 0f, 1000f};
	protected float[] desPos = new float[]{0f, 0f, -1f};
	protected float[] upDir = new float[]{0f, 1f, 0f};
	protected float[] desDir = new float[3];
	protected float near = 200f;
	protected float far = 2000f;
	protected float widthL = -100f;
	protected float heightB = -100f;
	protected float widthR = 100f;
	protected float heightT = 100f;

	public GlCamera3D() {
		super();
		calcuDesDir();
	}
	
	public float getNear() {
		return near;
	}
	
	public float getFar() {
		return far;
	}
	
	public float getButton() {
		return heightB;
	}
	
	public float getTop() {
		return heightT;
	}
	
	public float getLeft() {
		return widthL;
	}
	
	public float getRight() {
		return widthR;
	}
	
	public float[] getEyePos() {
		return eyePos;
	}
	
	public float[] getDesPos() {
		return desPos;
	}
	
	public float[] getDesDir() {
		return desDir;
	}
	
	public float[] getUpDir() {
		return upDir;
	}
	
	protected void calcuDesDir() {
		for (int i = 0; i < 3; i++) {
			desDir[i] = desPos[i] - eyePos[i];
		}
	}
	
	protected void calcuDesPos() {
		for (int i = 0; i < 3; i++) {
			desPos[i] = desDir[i] + eyePos[i];
		}
	}
	
	public void setMatrix() {
		setProjectMatrix();
		setCameraMatrix();
	}
	
	
	public void setProjectMatrix() {
		if (hasChange1) {
			hasChange1 = false;
			setProjectFrustumChange(widthL, widthR, heightB, heightT, near, far);
		}
	}

	public void setFrustum(float widthL, float widthR, float heightB, float heightT,
									float near, float far, boolean isCalcu) {
		setFrustum(widthL, widthR, heightB, heightT, near, far);
		if (isCalcu) {
			setProjectMatrix();
		}
	}
	
	public void setFrustum(float widthL, float widthR, float heightB, float heightT,
									float near, float far) {
		boolean isChange = false;
		if (widthL != this.widthL) {
			this.widthL = widthL;
			isChange = true;
		}
		if (widthR != this.widthR) {
			this.widthR = widthR;
			isChange = true;
		}
		if (heightB != this.heightB) {
			this.heightB = heightB;
			isChange = true;
		}
		if (heightT != this.heightT) {
			this.heightT = heightT;
			isChange = true;
		}
		if (far != this.far) {
			this.far = far;
			isChange = true;
		}
		if (near != this.near) {
			this.near = near;
			isChange = true;
		}
		if (isChange) {
			hasChange1 = true;
		}
	}
	
	public void setCameraMatrix() {
		if (hasChange2) {
			setCameraChange(eyePos[0], eyePos[1], eyePos[2],
										desPos[0], desPos[1], desPos[2],
												upDir[0], upDir[1], upDir[2]);
			hasChange2 = false;
		}
	}
	
	public void setDesPos(float dx, float dy, float dz,
									boolean isCalcu) {
		setDesPos(dx, dy, dz);
		if (isCalcu) {
			setCameraMatrix();
		}
	}
	
	public void setDesDir(float drx, float dry, float drz,
									 boolean isCalcu) {
		setDesPos(drx + eyePos[0], dry + eyePos[1], drz + eyePos[2]);
		if (isCalcu) {
			setCameraMatrix();
		}
	}
	
	public void setUpDir(float ux, float uy, float uz, boolean isCalcu) {
		setUpDir(ux, uy, uz);
		if (isCalcu) {
			setCameraMatrix();
		}
	}
	
	protected void setUpDir(float ux, float uy, float uz) {
		if (ux != upDir[0]) {
			upDir[0] = ux;
			hasChange2 = true;
		}
		if (uy != upDir[1]) {
			upDir[1] = uy;
			hasChange2 = true;
		}
		if (uz != upDir[2]) {
			upDir[2] = uz;
			hasChange2 = true;
		}
	}
	
	protected void setDesPos(float dx, float dy, float dz) {
		boolean tmp = false;
		if (dx != desPos[0]) {
			desPos[0] = dx;
			tmp = true;
		}
		if (dy != desPos[1]) {
			desPos[1] = dy;
			tmp = true;
		}
		if (dz != desPos[2]) {
			desPos[2] = dz;
			tmp = true;
		}
		if (tmp) {
			hasChange2 = true;
			calcuDesDir();
		}
	}
	
	protected void setEyePos(float ex, float ey, float ez, boolean isDesMove) {
		boolean tmp = false;
		if (ex != eyePos[0]) {
			eyePos[0] = ex;
			tmp = true;
		}
		if (ey != eyePos[1]) {
			eyePos[1] = ey;
			tmp = true;
		}
		if (ez != eyePos[2]) {
			eyePos[2] = ez;
			tmp = true;
		}
		if (tmp && isDesMove) {
			calcuDesPos();
			hasChange2 = true;
		} else if (tmp && !isDesMove) {
			calcuDesDir();
			hasChange2 = true;
		}
	}
	
	public void setEyePos(float ex, float ey, float ez,
								boolean isDesMove,
								boolean isCalcu) {
		setEyePos(ex, ey, ez, isDesMove);
		if (isCalcu) {
			setCameraMatrix();
		}
	}
	
}
