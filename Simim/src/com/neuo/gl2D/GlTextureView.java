package com.neuo.gl2D;

import android.graphics.Color;

import com.neuo.common.ColorUtil;
import com.neuo.glcommon.GlColorMaterialAndTex;
import com.neuo.glcommon.GlTexture;
import com.neuo.glcommon.GlView;
import com.neuo.globject.GlObject;
import com.neuo.glshader.GlShader;

public class GlTextureView extends GlEventView {
	protected boolean isTex;
	protected boolean isTouchScale;
	protected boolean isTouchChange;
	protected GlTexture downTex, notDownTex;
	protected int downColor, notDownColor;
	protected static final float containScale = 1.04f;
	public GlTextureView(String name, GlView glView, GlShader glShader, GlObject glObject) {
		super(name, glView, glShader, glObject);
	}

	@Override
	protected void init() {
		super.init();
		isTex = true;
		isTouchScale = false;
		isTouchChange = false;
		downColor = Color.WHITE;
		notDownColor = Color.WHITE;
		downTex = null;
		notDownTex = null;
	}

	public void setIsTex(boolean isTex) {
		this.isTex = isTex;
	}
	
	public void setTouchChange(boolean isTouchChange) {
		this.isTouchChange = isTouchChange;
	}
	
	public void setTouchScale(boolean isTouchScale) {
		this.isTouchScale = isTouchScale;
		if (!this.isTouchScale) {
			setScale(1f, 1f);
		}
	}

	private float[] tmpColor = new float[4];

	@Override
	protected void setDrawSelf() {
		boolean isTexure = this.isTex;
		int currTexOrId;
		if (isContain && isTouchChange) {
			if (isTexure) {
				currTexOrId = downTex.initTex(this.glView.getReinitId());
			} else {
				currTexOrId = downColor;
			}
		} else {
			if (isTexure) {
				currTexOrId = notDownTex.initTex(this.glView.getReinitId());
			} else {
				currTexOrId = notDownColor;
			}
		}

		if (isTexure) {
			glObject.getMaterialColor().setColorSelect(GlColorMaterialAndTex.TEX);
			glObject.getMaterialColor().setTexId(currTexOrId);
		} else {
			ColorUtil.getRGBAColor(currTexOrId, 1.f, tmpColor);
			glObject.getMaterialColor().setColorSelect(GlColorMaterialAndTex.DIRECTCOLOR);
			glObject.getMaterialColor().setDirectColor(tmpColor[0], tmpColor[1], tmpColor[2], tmpColor[3]);
		}
	}
	
	public void setTouchTexture(GlTexture down, GlTexture notDown) {
		this.downTex = down;
		this.notDownTex = notDown;
	}
	
	public GlTexture getTouchTexture(boolean isDown) {
		return isDown ? this.downTex : this.notDownTex;
	}
	
	public int getTouchColor(boolean isDown) {
		return isDown ? this.downColor : this.notDownColor;
	}
	
	public void setTouchColor(int down, int notDown) {
		this.downColor = down;
		this.notDownColor = notDown;
	}

	@Override
	protected void onContainState() {
		super.onContainState();
		if (isTouchScale) {
			setScale(containScale, containScale);
		}
	}
	
	@Override
	protected void onUnContainState() {
		super.onContainState();
		if (isTouchScale) {
			setScale(1f, 1f);
		}
	}
}