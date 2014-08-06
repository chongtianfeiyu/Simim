package com.neuo.glcommon;

import android.opengl.GLES20;

public class GlRenderBuffer {
	private long reinitId = -1;
	private int[] renderId = new int[1];
	private int width;
	private int height;
	private String renderName;
	private int model;
	
	public static final int RGBA = 0;
	public static final int DEPTH = 1;
	public static final int STENCIL = 2;
	
	public GlRenderBuffer(String renderName, int width, int height, int model) {
		this.renderName = renderName;
		this.width = width;
		this.height = height;
		this.model = model;
	}
	
	public String getRenderName() {
		return renderName;
	}
	
	public int getWidth() { 
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int initRender(long reinitId) {
		return reinitRender(reinitId);
	}
	
	public int getRenderId() {
		return renderId[0];
	}
	
	private int reinitRender(long reinitId) {
		if (this.reinitId >= reinitId) {
			return renderId[0];
		}
		this.reinitId  = reinitId;

		GLES20.glGenRenderbuffers(1, renderId, 0);
		GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, renderId[0]);
		if (this.model == RGBA) {
			GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_RGBA,
														width, height);
		} else if (this.model == DEPTH) {
			GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16,
														width, height);
		} else if (this.model == STENCIL) {
			GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_STENCIL_INDEX8,
														width, height);
		} else {
			throw new RuntimeException("model value is error");
		}
		
		return renderId[0];
	}
	
	public void uninit(long reinitId) {
		deleteBuffer(reinitId);
	}
	
	private void deleteBuffer(long reinitId) {
		if (this.reinitId >= reinitId){	
			GLES20.glDeleteRenderbuffers(1, renderId, 0);
			this.reinitId = -1;
		}
	}
}
