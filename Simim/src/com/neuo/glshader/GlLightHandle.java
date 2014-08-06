package com.neuo.glshader;

public interface GlLightHandle extends GlMaterialHandle{
	public float[] getLightPos();
	public float[] getLightAmbient();
	public float[] getLightDiffuse();
	public float[] getLightSpecular();
	public int getIsLightOpen();
}
