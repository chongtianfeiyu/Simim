package com.neuo.glshader;

public interface GlMaterialHandle extends GlMultiSelect {
	public float[] getAmbient();
	public float[] getDiffuse();
	public float[] getSpecular();
	public float getShininess();
}
