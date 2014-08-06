package com.neuo.globject;

import java.nio.FloatBuffer;

import com.neuo.glshader.GlPositionTexHandle;
import com.neuo.glshader.GlTexHandle;

public class GlObjectByLightTex extends GlObjectByLight
implements GlTexHandle, GlPositionTexHandle {
	public GlObjectByLightTex() {
		setDrawObject(this);
	}

	public FloatBuffer getPositionTexBuffer() {
		return posAttribute.getTexCoorBuffer();
	}

	@Override
	public int getTexId() {
		return colorMaterialAndTex.getTexId();
	}
}
