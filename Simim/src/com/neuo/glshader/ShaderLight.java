package com.neuo.glshader;

import com.neuo.globject.GlObject;

import android.opengl.GLES20;

public class ShaderLight {
	protected int mulightLocation;
	protected int mulightAmbient;
	protected int mulightDiffuse;
	protected int mulightSpecular;
	protected int muIsLightOpen;
	
	public void setLightLocation(int lightLocation) {
		this.mulightLocation = lightLocation;
	}
	
	public void setLightAmbient(int lightAmbient) {
		this.mulightAmbient = lightAmbient;
	}
	
	public void setLightDiffuse(int lightDiffuse) {
		this.mulightDiffuse = lightDiffuse;
	}
	
	public void setLightSpecular(int lightSpecular) {
		this.mulightSpecular = lightSpecular;
	}
	
	public void setIsLightOpen(int isLightOpen) {
		this.muIsLightOpen = isLightOpen;
	}

	public void drawLight(GlObject glObject) {
		GlLightHandle lightHandle = (GlLightHandle)glObject;
		GLES20.glUniform4fv(mulightLocation, 1, lightHandle.getLightPos(), 0);
		GLES20.glUniform4fv(mulightAmbient, 1, lightHandle.getLightAmbient(), 0);
		GLES20.glUniform4fv(mulightDiffuse, 1, lightHandle.getLightDiffuse(), 0);
		GLES20.glUniform4fv(mulightSpecular, 1, lightHandle.getLightSpecular(), 0);
		GLES20.glUniform1i(muIsLightOpen, lightHandle.getIsLightOpen());
	}
}
