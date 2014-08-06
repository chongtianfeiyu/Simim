package com.neuo.gl2D;

import com.neuo.common.CommonText.TextInterface;
import com.neuo.glcommon.GlColorMaterialAndTex;
import com.neuo.glcommon.GlTextureText;
import com.neuo.glcommon.GlView;
import com.neuo.globject.GlObject;
import com.neuo.glshader.GlShader;

public class GlTextView extends GlEventView {
	protected GlTextureText textureText;
	protected TextInterface textInterface;
	protected String suffix = "";
	public GlTextView(String name, GlView glView, GlShader glShader,
			GlObject glObject) {
		super(name, glView, glShader, glObject);
		setCanTouch(false);
	}
	
	public GlTextureText getTextureText() {
		return textureText;
	}
	
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
	public void initTextTexture(int width, int height) {
		textureText = new GlTextureText("", width, height, "");
	}
	
	public void initTextTexture() {
		textureText = new GlTextureText("", (int)getWidth(true), (int)getHeight(true), "");
	}
	
	public void setTextInterface(TextInterface textInterface) {
		this.textInterface = textInterface;
	}
	
	public TextInterface getTextInterface() {
		return this.textInterface;
	}
	
	@Override
	protected void init() {
		super.init();
		this.textureText = null;
	}
	
	@Override
	protected void setDrawSelf() {
		if (textInterface == null) {
			this.textureText.setText(suffix);
		} else {
			this.textureText.setText(textInterface.getText() + suffix);
		}
		int texId = this.textureText.initTex(this.glView.getReinitId());
		glObject.getMaterialColor().setTexId(texId);
		glObject.getMaterialColor().setColorSelect(GlColorMaterialAndTex.TEX);
	}

}
