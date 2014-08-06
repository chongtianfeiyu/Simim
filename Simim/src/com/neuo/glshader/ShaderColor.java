package com.neuo.glshader;

import com.neuo.globject.GlObject;

import android.opengl.GLES20;

public class ShaderColor {
	public static final int DIRECT_COLOR = 0;
	public static final int DIRECT_MATERIAL = 1;
	public static final int BOTH_COLORANDMETAERIAL = 2;
	public static final int DIRECT_TEX = 3;
	public static final int BOTH_COLORANDTEX = 4;
	public static final int TRIPLE = 5;
	
	public static final int COLOR = 1;
	public static final int MATERIAL = 2;
	
	private int colorHandle;
	private int ambientHandle;
	private int diffuseHandle;
	private int specularHandle;
	private int modelHandle;
	private int shininess;
	private int texIdHandle;
	private int alphaHandle;
	
	public void setAlphaHandle(int alphaHandle) {
		this.alphaHandle = alphaHandle;
	}
	
	public void setTexHandle(int texHandle) {
		this.texIdHandle = texHandle;
	}
	
	public void setShininess(int shininess) {
		this.shininess = shininess;
	}
	
	public void setColorHandle(int colorHandle) {
		this.colorHandle = colorHandle;
	}
	
	public void setAmbientHandle(int ambientHandle) {
		this.ambientHandle = ambientHandle;
	}
	
	public void setDiffuseHandle(int diffuseHandle) {
		this.diffuseHandle = diffuseHandle;
	}
	
	public void setSpecularHandle(int specularHandle) {
		this.specularHandle = specularHandle;
	}
	
	public void setModelHandle(int modeHandle) {
		this.modelHandle = modeHandle;
	}
	
	private void drawDirectColor(GlObject glObject) {
		GlColorHandle directColorHandle = (GlColorHandle)glObject;
		float[] color = directColorHandle.getColor();
		GLES20.glUniform4fv(colorHandle, 1, color, 0);
		GLES20.glUniform1f(alphaHandle, directColorHandle.getAlpha());
	}
	
	private void drawMaterialColor(GlObject glObject) {
		GlMaterialHandle materialHandle = (GlMaterialHandle)glObject;
		float[] color = materialHandle.getAmbient();
		GLES20.glUniform4fv(ambientHandle, 1, color, 0);
		color = materialHandle.getDiffuse();
		GLES20.glUniform4fv(diffuseHandle, 1, color, 0);
		color = materialHandle.getSpecular();
		GLES20.glUniform4fv(specularHandle, 1, color, 0);
		GLES20.glUniform1f(shininess, materialHandle.getShininess());
	}

	private void drawSelectColor(GlObject glObject) {
		GlMultiSelect multiSelect = (GlMultiSelect)glObject;
		GLES20.glUniform1i(modelHandle, multiSelect.getColorMode());
	}
	
	private void drawTex(GlObject glObject) {
		GlTexHandle textureHandle = (GlTexHandle)glObject;
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle.getTexId());
		GLES20.glUniform1i(texIdHandle, 0);// π”√0∫≈Œ∆¿Ì
	}
	
	public void drawColor(GlObject glObject, int model) {
		switch (model)
		{
		case DIRECT_COLOR:
			drawDirectColor(glObject);
			break;
		case BOTH_COLORANDMETAERIAL:
			drawDirectColor(glObject);
			drawMaterialColor(glObject);
			drawSelectColor(glObject);
			break;
		case DIRECT_TEX:
			drawTex(glObject);
			break;
		case BOTH_COLORANDTEX:
			drawDirectColor(glObject);
			drawTex(glObject);
			drawSelectColor(glObject);
			break;
		case TRIPLE:
			drawDirectColor(glObject);
			drawMaterialColor(glObject);
			drawTex(glObject);
			drawSelectColor(glObject);
			break;
		}
	}
}
