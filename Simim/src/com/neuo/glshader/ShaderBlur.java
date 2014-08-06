package com.neuo.glshader;

import android.opengl.GLES20;

import com.neuo.globject.GlObject;

public class ShaderBlur {
    protected int muOriantHandle;
    protected int muBlurNumHandle;
    protected int muBlurScaleHandle;
    protected int muBlurForceHandle;
    protected int muBlurTexSizeHandle;
	
    public void setBlurTexSizeHandle(int blurTexSizeHandle) {
    	this.muBlurTexSizeHandle = blurTexSizeHandle;
    }
    
	public void setOriantHandle(int oriantHandle) {
		this.muOriantHandle = oriantHandle;
	}
	
	public void setBlurNumHandle(int blurNumHandle) {
		this.muBlurNumHandle = blurNumHandle;
	}
	
	public void setBlurScaleHandle(int blurScaleHandle) {
		this.muBlurScaleHandle = blurScaleHandle;
	}
	
	public void setBlurForceHandle(int blurForceHandle) {
		this.muBlurForceHandle = blurForceHandle;
	}
	
	public void drawBlur(GlObject glObject) {
		GlBlurHandle blurHandle = (GlBlurHandle)glObject;
		GLES20.glUniform1i(muOriantHandle, blurHandle.getOrientation());
		GLES20.glUniform1i(muBlurNumHandle, blurHandle.getBlurAmount());
		GLES20.glUniform1f(muBlurScaleHandle, blurHandle.getBlurScale());
		GLES20.glUniform1f(muBlurForceHandle, blurHandle.getBlurStrength());
		GLES20.glUniform2fv(muBlurTexSizeHandle, 1, blurHandle.getBlurTexSize(), 0);
	}
}
