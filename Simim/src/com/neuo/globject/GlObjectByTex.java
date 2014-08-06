package com.neuo.globject;

import java.nio.FloatBuffer;

import com.neuo.glshader.GlPositionTexHandle;
import com.neuo.glshader.GlTexHandle;

public class GlObjectByTex extends GlObject 
implements GlPositionTexHandle, GlTexHandle {
	public GlObjectByTex() {
		setDrawObject(this);
	}

	@Override
	public int getColorMode() {
		return colorMaterialAndTex.getColorSelect();
	}

	@Override
	public int getTexId() {
		return colorMaterialAndTex.getTexId();
	}

	@Override
	public FloatBuffer getPositionTexBuffer() {
		return posAttribute.getTexCoorBuffer();
	}

}
