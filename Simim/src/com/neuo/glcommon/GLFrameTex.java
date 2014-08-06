package com.neuo.glcommon;

import android.opengl.GLES20;

public class GLFrameTex {
	
	private static final String texSuffix = "tex";
	private static final String depthSuffix = "depth";
	private static final String stencilSuffix = "stencil";
	
	private String frameName;
	private int[] frameId = new int[1];
	private long reinitId = -1;
	private GlTexture texture = null;
	private GlRenderBuffer depthBuffer = null;
	private GlRenderBuffer stencilBuffer = null;
	private int width = -1;
	private int height = -1;
	
	private int model;
	public static final int TEX = 1 << 0;
	public static final int DEPTH = 1 << 1;
	public static final int STENCIL = 1 << 2;
	
	public GLFrameTex(String frameName, int model) {
		this.frameName = frameName;
		this.model = model;
	}
	
	public int getTexId() {
		return texture.getTexId();
	}
	
	public GlTexture getTexture() {
		return texture;
	}
	
	private void releaseAttach(long reinitId) {
		if ((this.model & TEX) == 1) {
			releaseTex(reinitId);
		}
		if ((this.model & DEPTH) == 1) {
			releaseDepth(reinitId);
		}
		if ((this.model & STENCIL) == 1) {
			releaseStencil(reinitId);
		}
	}
	
	private void releaseTex(long reinitId) {
		if (texture != null) {
			texture.uninit(reinitId);
			texture = null;
		}
	}
	
	private void initTex() {
		if (texture == null) {
			texture = new GlTexture(frameName + texSuffix, this.width, this.height);
		}
	}
	
	private void releaseDepth(long reinitId) {
		if (depthBuffer != null) {
			depthBuffer.uninit(reinitId);
			depthBuffer = null;
		}
	}
	
	private void releaseStencil(long reinitId) {
		if (stencilBuffer != null) {
			stencilBuffer.uninit(reinitId);
			stencilBuffer = null;
		}
	}
	
	private void initStencil() {
		if (stencilBuffer == null) {
			stencilBuffer = new GlRenderBuffer(frameName + stencilSuffix,
													this.width, this.height,
													GlRenderBuffer.STENCIL);
		}
	}
	
	private void initDepth() {
		if (depthBuffer == null) {
			depthBuffer = new GlRenderBuffer(frameName + depthSuffix,
												this.width, this.height,
												GlRenderBuffer.DEPTH);
		}
	}
	
	public void active(boolean isActive) {
		if (isActive) {
			GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameId[0]);
		} else {
			GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
		}
	}
	
	public void uninit(long reinitId) {
		deleteFrame(reinitId);
	}
	
	public int getFrameId() {
		return frameId[0];
	}
	
	private void deleteFrame(long reinitId) {
		if (this.reinitId > reinitId) {
			releaseAttach(reinitId);
			GLES20.glDeleteFramebuffers(1, frameId, 0);
			this.reinitId = -1;
		}
	}
	
	private void initAttachInfo() {
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameId[0]);
		if ((this.model & TEX) == 1) {
			texture.initTex(this.reinitId);
			GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
							GLES20.GL_TEXTURE_2D, texture.getTexId(), 0);
		}
		if ((this.model & DEPTH) == 1) {
			depthBuffer.initRender(this.reinitId);
			GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT,
												GLES20.GL_RENDERBUFFER,  depthBuffer.getRenderId());
		}
		if ((this.model & STENCIL) == 1) {
			stencilBuffer.initRender(this.reinitId);
			GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_STENCIL_ATTACHMENT,
												GLES20.GL_RENDERBUFFER,  stencilBuffer.getRenderId());
		}
		
		//GLES20.glCheckFramebufferStatus(frameId[0]);
		if (GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER)  != GLES20.GL_FRAMEBUFFER_COMPLETE) {
			throw new RuntimeException("fbo check fail");
		}

		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
	}

	public void initSize(int width, int height) {
		this.width = width;
		this.height = height;
		initTex();
		initDepth();
		initStencil();
	}

	public boolean initFBObject(long reinitId) {
		if (this.reinitId >= reinitId) {
			return true;
		} else {
			this.reinitId = reinitId;
			GLES20.glGenFramebuffers(1, frameId, 0);
			initAttachInfo();
			return false;
		}
	}
}