package com.neuo.glcommon;


public class GlLight {
	private static final float[] defaultAmbient = {0.5f, 0.5f, 0.5f, 1f};
	private float[] ambient = defaultAmbient.clone();
	private static final float[] defaultDiffuse = {0.6f, 0.6f, 0.6f, 1f};
	private float[] diffuse = defaultDiffuse.clone();
	private static final float[] defaultSpecular = {0.3f, 0.3f, 0.3f, 1f};
	private float[] specular = defaultSpecular.clone();
	
	private static final float[] defaultPosition = {60, 80, 60, 1};
	private float[] position = defaultPosition.clone();
	private static final boolean defaultIsParallel = true;
	private boolean isParallel = defaultIsParallel;
	public static final int OPENLIGHT = 1;
	public static final int CLOSELIGHT = 0;
	private static final int defaultIsOpen = OPENLIGHT;
	private int isOpen = defaultIsOpen;
	private int texId;
	
	public GlLight() {
		setParallel(defaultIsParallel);
	}

	public void setTexId(int texId) {
		this.texId = texId;
	}
	
	public int getTexId() {
		return this.texId;
	}
	
	public void setIsOpen(int isOpen) {
		this.isOpen = isOpen;
	}
	
	public int getIsOpen() {
		return this.isOpen;
	}
	
	public void setParallel(boolean isParallel)
	{
		this.isParallel = isParallel;
		if (this.isParallel){
			position[3] = 0;
		}
		else{
			position[3] = 1;
		}
	}
	
	@Override
	public GlLight clone(){
		// TODO Auto-generated method stub
		GlLight newLight = new GlLight();
		newLight.isParallel = this.isParallel;
		newLight.ambient = this.ambient.clone();
		newLight.diffuse = this.diffuse.clone();
		newLight.specular = this.specular.clone();
		newLight.position = this.position.clone();
		newLight.isOpen = this.isOpen;
		newLight.texId = this.texId;
		return newLight;
	}
	
	public boolean getIsParallel() {
		return isParallel;
	}
	
	public void setPosition(float x, float y, float z, float a) {
		this.position[0] = x;
		this.position[1] = y;
		this.position[2] = z;
		this.position[3] = a;
	}
	
	public void setAmbient(float r, float g, float b, float a) {
		this.ambient[0] = r;
		this.ambient[1] = g;
		this.ambient[2] = b;
		this.ambient[3] = a;
	}
	
	public void setDiffuse(float r, float g, float b, float a) {
		this.diffuse[0] = r;
		this.diffuse[1] = g;
		this.diffuse[2] = b;
		this.diffuse[3] = a;
	}
	
	public void setSpecular(float r, float g, float b, float a) {
		this.specular[0] = r;
		this.specular[1] = g;
		this.specular[2] = b;
		this.specular[3] = a;
	}
	
	public float[] getPosition() {
		return position;
	}
	
	public float[] getSpecular() {
		return specular;
	}
	
	public float[] getDiffuse() {
		return diffuse;
	}

	public float[] getAmbient() {
		return ambient;
	}
}
