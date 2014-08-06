package com.neuo.glcommon;

import com.neuo.common.MathUtil;
import com.neuo.common.UpdateManager.CommonUpdate;

import android.opengl.Matrix;

public abstract class GlCamera 
implements CommonUpdate {
	protected float[] finalEyes = new float[3];
	protected float[] preEyes = new float[3];
	protected float[] afterEyes = new float[3];
	protected float[] mProjMatrix = MathUtil.getInitStack();
	protected float[] mProjMatrixT = MathUtil.getInitStack();
	protected float[] mVMatrix = MathUtil.getInitStack();  
	protected float[] mVMatrixT = MathUtil.getInitStack();
	protected float[] mMVPMatrix = MathUtil.getInitStack();
	private boolean isChange = true;
 	private boolean projChange = true;
 	private boolean cameraChange = true;

	public GlCamera() {
	}
	
	public void confirmAll() {
		confirmCamera();
		confirmProjectFrustum();
	}
	
	public void confirmCamera() {
		synchronized (this) {
			if (!cameraChange)return;
			cameraChange = false;
			boolean tmpIsChange = false;
			for (int i = 0; i < 16; i ++) {
				if (mVMatrix[i] != mVMatrixT[i]) {
					tmpIsChange = true;
					mVMatrix[i] = mVMatrixT[i];
				}
			}
			for (int i = 0; i < 3; i ++) {
				if (afterEyes[i] != preEyes[i]) {
					afterEyes[i] = preEyes[i];
					tmpIsChange = true;
				}
			}
			if(tmpIsChange)isChange = true;
		}
	}
  
	public void setCameraChange(float cx, float cy, float cz) {
		synchronized (this) {
			preEyes[0] = cx;
			preEyes[1] = cy;
			preEyes[2] = cz;
			cameraChange = true;
		}
	}
	
	public void setCameraChange(float cx, float cy, float cz, float tx, float ty, float tz, 
										float upx, float upy, float upz) {
		synchronized (this) {
			Matrix.setLookAtM (
					mVMatrixT, 0, cx, cy, cz, tx, ty, tz,
					upx, upy, upz
					);
			preEyes[0] = cx;
			preEyes[1] = cy;
			preEyes[2] = cz;
			cameraChange = true;
		}
	}
	
	public void setProjectFrustumChange (float left, float right, float bottom, float top,
												float near, float far) {
		synchronized (this) {
			Matrix.frustumM(mProjMatrixT, 0, left, right, bottom, top, near, far);    	
			projChange = true;
		}
	}
	
	public void confirmProjectFrustum() {
		synchronized (this) {
			if (!projChange)return;
			projChange = false;
			boolean tmpIsChange = false;
			for (int i = 0; i < 16; i ++) {
				if (mProjMatrix[i] != mProjMatrixT[i]) {
					tmpIsChange = true;
					mProjMatrix[i] = mProjMatrixT[i];
				}
			}
			if (tmpIsChange)isChange = true;
		}
	}
	
	public void setProjectOrthoChange(float left, float right, float bottom, float top,
												float near, float far) {
		synchronized (this) {
			Matrix.orthoM(mProjMatrixT, 0, left, right, bottom, top, near, far);
			projChange = true;
		}
	}

	public float[] getFinalMatrix(float[] currMatrix) {
		float[] finalMatrix = new float[16];
		Matrix.multiplyMM(finalMatrix, 0, getMVPMatrix(), 0, currMatrix, 0);        
		return finalMatrix;
	}
	  
	public void getFinalMatrix(float[] currMatrix, float[] resMatrix) {
		Matrix.multiplyMM(resMatrix, 0, getMVPMatrix(), 0, currMatrix, 0);        
	}
	
	@Override
	public void update() {
		if (isChange) {
			isChange = false;
			for (int i = 0; i < 3; i++) {
				finalEyes[i] = afterEyes[i];
			}
			Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);
		}
	}

	public float[] getMVPMatrix() {
		return mMVPMatrix;
	}
	
	public float[] getCameraPos() {
		return finalEyes;
	}
}
