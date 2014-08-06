package com.neuo.glcommon;

public class GlBlur {
	public static final int horizontal = 0;
	public static final int vertical = 1;
	private int orientation = horizontal;
	private int blurAmount = 10;
	private float blurScale = 0.005f;
	private float blurForce = 0.8f;
	private float[] blurTexSize = new float[]{1f, 1f};
	
	public GlBlur() {
	}
	
	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}
	
	public int getOrientation() {
		return this.orientation;
	}
	
	public void setBlurAmount(int amount) {
		this.blurAmount = amount;
	}
	
	public void setBlurTexSize(float x, float y) {
		this.blurTexSize[0] = x;
		this.blurTexSize[1] = y;
	}
	
	public float[] getBlurTexSize() {
		return this.blurTexSize;
	}
	
	public int getBlurAmount() {
		return this.blurAmount;
	}
	
	public void setBlurScale(float scale) {
		this.blurScale = scale;
	}
	
	public float getBlurScale() {
		return this.blurScale;
	}
	
	public void setBlurForce(float force) {
		this.blurForce = force;
	}
	
	public float getBlurForce() {
		return this.blurForce;
	}
}
