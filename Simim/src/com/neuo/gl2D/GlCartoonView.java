package com.neuo.gl2D;

import java.util.ArrayList;

import com.neuo.common.ColorUtil;
import com.neuo.common.UpdateManager.CommonCalcu;
import com.neuo.glcommon.GlColorMaterialAndTex;
import com.neuo.glcommon.GlTexture;
import com.neuo.glcommon.GlView;
import com.neuo.globject.GlObject;
import com.neuo.glshader.GlShader;

public class GlCartoonView extends GlEventView
implements CommonCalcu {
	protected ArrayList<Integer> colors;
	protected ArrayList<GlTexture> glTextures;
	protected int colorIndex;
	protected int textureIndex;
	protected boolean isTex;
	protected long changeInterval;
	protected long passTime;
	
	public GlCartoonView(String name, GlView glView, GlShader glShader,
			GlObject glObject) {
		super(name, glView, glShader, glObject);
	}

	@Override
	public void calcu(long interval) {
		passTime += interval;
		if (passTime > changeInterval) {
			passTime = 0;
			nextIndex();
		}
	}
	
	private void nextIndex() {
		if (!isTex && colors.size() > 0) {
			colorIndex = (colorIndex + 1) % colors.size();
		} else if (isTex && glTextures.size() > 0) {
			textureIndex = (textureIndex + 1) % glTextures.size();
		}
	}

	public void resetPassTime() {
		passTime = 0;
	}
	
	public void setChangeInterval(long changeInterval) {
		this.changeInterval = changeInterval;
	}

	public long getChangeInterval() {
		return this.changeInterval;
	}
	
	public void resetColor(int colorIndex) {
		if (colorIndex >= 0 && colorIndex < colors.size()) {
			this.colorIndex = colorIndex;
		}
	}
	
	public void resetTexture(int textureIndex) {
		if (textureIndex >= 0 && textureIndex < glTextures.size()) {
			this.textureIndex = textureIndex;
		}
	}

	public void addColor(int color) {
		synchronized (colors) {
			this.colors.add(color);
		}
	}
	
	public void addTexture(GlTexture texture) {
		synchronized (glTextures) {
			this.glTextures.add(texture);
		}
	}
	
	public void clearColors() {
		synchronized (this.colors) {
			this.colors.clear();
			colorIndex = 0;
		}
	}
	
	public void clearTextures() {
		synchronized (this.glTextures) {
			this.glTextures.clear();
			textureIndex = 0;
		}
	}
	
	@Override
	protected void init() {
		super.init();
		colors = new ArrayList<Integer>();
		glTextures = new ArrayList<GlTexture>();
		colorIndex = 0;
		textureIndex = 0;
		isTex = true;
		resetPassTime();
	}
	
	public boolean isTex() {
		return this.isTex;
	}
	
	public void setIsTex(boolean isTex) {
		if (this.isTex != isTex) {
			this.isTex = isTex;
			resetPassTime();
		}
	}
	
	protected int getCurrTexture() {
		synchronized (glTextures) {
			if (textureIndex >= 0 && textureIndex < glTextures.size()) {
				return glTextures.get(textureIndex).initTex(this.glView.getReinitId());
			}
		}
		return 0;
	}
	
	protected int getCurrColor() {
		synchronized (colors) {
			if (colorIndex >= 0 && colorIndex < colors.size()) {
				return colors.get(colorIndex);
			}
		}
		return 0;
	}

	private float[] tmpColor = new float[4];
	@Override
	protected void setDrawSelf() {
		int currTexOrId = 0;
		if (this.isTex) {
			currTexOrId = getCurrTexture();
			glObject.getMaterialColor().setColorSelect(GlColorMaterialAndTex.TEX);
			glObject.getMaterialColor().setTexId(currTexOrId);
		} else {
			currTexOrId = getCurrColor();
			glObject.getMaterialColor().setColorSelect(GlColorMaterialAndTex.DIRECTCOLOR);
			ColorUtil.getRGBAColor(currTexOrId, 1.f, tmpColor);
			glObject.getMaterialColor().setDirectColor(tmpColor[0], tmpColor[1], tmpColor[2], tmpColor[3]);
		}
	}
}
