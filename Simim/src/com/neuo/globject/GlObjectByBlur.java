package com.neuo.globject;

import com.neuo.glcommon.GlBlur;
import com.neuo.glshader.GlBlurHandle;

public class GlObjectByBlur extends GlObjectByTex
implements GlBlurHandle {
	protected GlBlur blurInfo;
	public GlObjectByBlur() {
		setDrawObject(this);
	}
	
	@Override
	public void initMember() {
		super.initMember();
		if (blurInfo == null) {
			blurInfo = new GlBlur();
		}
	}

	public void setGlBlur(GlBlur blur) {
		this.blurInfo = blur;
	}
	
	public GlBlur getGlBlur() {
		return this.blurInfo;
	}
	
	@Override
	public int getOrientation() {
		return blurInfo.getOrientation();
	}

	@Override
	public int getBlurAmount() {
		return blurInfo.getBlurAmount();
	}

	@Override
	public float getBlurScale() {
		return blurInfo.getBlurScale();
	}

	@Override
	public float getBlurStrength() {
		return blurInfo.getBlurForce();
	}

	@Override
	public float[] getBlurTexSize() {
		return blurInfo.getBlurTexSize();
	}
}
