package com.neuo.globject;

import java.nio.FloatBuffer;

import com.neuo.common.MathUtil;
import com.neuo.glcommon.GlLight;
import com.neuo.glshader.GlCameraHandle;
import com.neuo.glshader.GlLightHandle;
import com.neuo.glshader.GlNorMatrixHandle;
import com.neuo.glshader.GlPositionNormalHandle;

public class GlObjectByLight extends GlObject
implements GlLightHandle, GlNorMatrixHandle, GlPositionNormalHandle, GlCameraHandle {
	protected GlLight light;
	protected float[] cameraPos;
	protected float[] norMatrix = MathUtil.getInitStack();
	protected float[] tmpNorMatrix = MathUtil.getInitStack();
	protected float[] MMatrix;

	public GlObjectByLight() {
		setDrawObject(this);
	}

	@Override
	public void initMember() {
		super.initMember();
		if (cameraPos == null) {
			cameraPos = new float[3];
		}
		if (light == null) {
			light = new GlLight();
		}
		if (MMatrix == null) {
			MMatrix = new float[16];
		}
	}
	
	public void setCameraPos(float[] cameraPos) {
		this.cameraPos = cameraPos;
	}

	public float[] getCameraPos() {
		return cameraPos;
	}
	
	public void setGlLight(GlLight glLight) {
		this.light = glLight;
	}
	
	public GlLight getGlLight() {
		return light;
	}

	@Override
	public float[] getAmbient() {
		return colorMaterialAndTex.getAmbient();
	}

	@Override
	public float[] getDiffuse() {
		return colorMaterialAndTex.getDiffuse();
	}

	@Override
	public float[] getSpecular() {
		return colorMaterialAndTex.getSpecular();
	}

	@Override
	public float getShininess() {
		return colorMaterialAndTex.getShininess();
	}

	@Override
	public int getColorMode() {
		return colorMaterialAndTex.getColorSelect();
	}

	@Override
	public float[] getMMatrix() {
		return MMatrix;
	}
	
	public void setMMatrix(float[] MMatrix) {
		this.MMatrix = MMatrix;
	}

	@Override
	public float[] getCamera() {
		return cameraPos;
	}

	@Override
	public FloatBuffer getPositionNormalBuffer() {
		return posAttribute.getPosNormalBuffer();
	}

	@Override
	public float[] getNorMatrix() {
		MathUtil.normalMatrix(MMatrix, norMatrix, tmpNorMatrix);
		return norMatrix;
	}

	@Override
	public float[] getLightPos() {
		return light.getPosition();
	}

	@Override
	public float[] getLightAmbient() {
		return light.getAmbient();
	}

	@Override
	public float[] getLightDiffuse() {
		return light.getDiffuse();
	}

	@Override
	public float[] getLightSpecular() {
		return light.getSpecular();
	}

	@Override
	public int getIsLightOpen() {
		return light.getIsOpen();
	}
	

}
