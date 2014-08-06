package com.neuo.glcommon;

public class GlColorMaterialAndTex {
	private static final float[] defaultColor = {0.7f, 0.2f, 0.5f, 1f};
	private float[] directColor = defaultColor.clone();
	private static final float[] defaultAmbient = {0.2f, 0.2f, 0.2f, 1f};
	private float[] ambient = defaultAmbient.clone();
	private static final float[] defaultDiffuse = {0.3f, 0.3f, 0.3f, 1f};
	private float[] diffuse = defaultDiffuse.clone();
	private static final float[] defaultSpecular = {0.5f, 0.5f, 0.5f, 1f};
	private float[] specular = defaultSpecular.clone();
	
	private static final float defaultShininess = 10.0f;
	private float shininess = defaultShininess;
	
	private int texId;
	private static final float defaultAlpha = 1f;
	private float alpha = defaultAlpha;
	
	public static final int DIRECTCOLOR = 1;
	public static final int MATERIAL = 0;
	public static final int TEX = 2;
	private static final int defaultColorSelect = DIRECTCOLOR;
	private int colorSelect = defaultColorSelect;
	
	public GlColorMaterialAndTex() {
	}
	
	public void setAlpha(float alpha, float maxAlpha) {
		this.alpha = alpha / maxAlpha;
		if (this.alpha > 1f) {
			this.alpha = 1f;
		}
	}
	
	public float getAlpha() {
		return this.alpha;
	}

	public void setTexId(int texId) {
		this.texId = texId;
	}
	
	public int getTexId() {
		return texId;
	}
		
	public void setShininess(float shininess) {
		this.shininess = shininess;
	}
	
	public float getShininess() {
		return shininess;
	}
	
	public void setColorSelect(int isDirect) {
		colorSelect = isDirect;
	}
	
	public int getColorSelect() {
		return colorSelect;
	}
	
	public void setAmbient(float rr, float rg, float rb, float ra) {
		this.ambient[0] = rr;
		this.ambient[1] = rg;
		this.ambient[2] = rb;
		this.ambient[3] = ra;
	}
	
	public float[] getAmbient() {
		return ambient;
	}
	
	public void setDiffuse(float dr, float dg, float db, float da) {
		this.diffuse[0] = dr;
		this.diffuse[1] = dg;
		this.diffuse[2] = db;
		this.diffuse[3] = da;
	}
	
	public float[] getDiffuse() {
		return diffuse;
	}
	
	public void setSpecular(float sr, float sg, float sb, float sa) {
		this.specular[0] = sa;
		this.specular[1] = sg;
		this.specular[2] = sb;
		this.specular[3] = sa;
	}
	
	public float[] getSpecular() {
		return specular;
	}
	
	public void setDirectColor(float cr, float cg, float cb, float ca) {
		this.directColor[0] = cr;
		this.directColor[1] = cg;
		this.directColor[2] = cb;
		this.directColor[3] = ca;
		
	}
	
	public float[] getDirectColor() {
		return directColor;
	}
	
	
}
