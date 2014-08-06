package com.neuo.gl2D;

import com.neuo.glcommon.GlCamera;

public class GlSim2DCamera extends GlCamera{
	protected float width = 0f;
	protected float height = 0f;
	protected float z = 0f;
	protected float zLength = 1000f;
	protected boolean hasChange = true;
	
	public float getHeight() {
		return height;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getZ() {
		return z;
	}
	
	public float getTDef() {
		return z + zLength;
	}
	
	public float getBDef() {
		return z - zLength;
	}
	
	protected void setZlength(float zLen) {
		if (zLength != zLen) {
			zLength = zLen;
			hasChange = true;
		}
	}
	
	public void setZLength(float zLen, boolean isCalcu) {
		setZlength(zLen);
		if (isCalcu) {
			setMatrix();
		}
	}
	
	protected void setZ(float z) {
		if (this.z != z) {
			this.z = z;
			hasChange = true;
		}
	}
	
	public void setZ(float z, boolean isCalcu) {
		setZ(z);
		if (isCalcu) {
			setMatrix();
		}
	}
	
	public GlSim2DCamera() {
		super();
		setCameraChange(0, 0, zLength + z);
	}
	
	public void setMatrix() {
		if (!hasChange)return;
		hasChange = false;
		setProjectOrthoChange(-this.width / 2f, this.width / 2f,
										-this.height / 2f, this.height / 2f,
										-zLength + z, zLength + z);
	}
	
	protected void setWH(float width, float height) {
		if (this.height != height) {
			this.height = height;
			hasChange = true;
		}
		if (this.width != width) {
			this.width = width;
			hasChange = true;
		}
	}
	
	public void setProjMatrix2D(float width, float height,
					boolean isCalcu) {
		setWH(width, height);
		if (isCalcu) {
			setMatrix();
		}
	}

	public void setProjMatrix2D(float width, float height, float z, float zLength,
									boolean isCalcu) {
		setWH(width, height);
		setZ(z);
		setZlength(zLength);
		if (isCalcu) {
			setMatrix();
		}
	}
	
}
