package com.neuo.glshader;

public interface GlBlurHandle {
	public int getOrientation();
	public int getBlurAmount();
	public float getBlurScale();
	public float getBlurStrength();
	public float[] getBlurTexSize();
}
