package com.neuo.glcommon;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;

public class GlTexture {
	private String texTag;
	private String bitmapName;
	private int[] texId = new int[1];
	private long reinitId = -1;
	private Bitmap bitmap;
	private int model;
	private boolean isNeedReinit;
	private Context context;
	private int width;
	private int height;
	//protected Object initLock = new Object();
	private static final int ResIdModel 	= 0;
	private static final int BitmapModel 	= 1;
	private static final int ThirdModel 	= 2;
	private static final int CreateModel 	= 3;
	
	public GlTexture(String texTag) {
		this.texTag = texTag;
		model = ThirdModel;
		isNeedReinit = true;
	}
	
	public GlTexture(String texTag, int width, int height) {
		this.texTag = texTag;
		model = CreateModel;
		isNeedReinit = true;
		this.width = width;
		this.height = height;
	}
	
	public GlTexture(String texTag, String name, Context context) {
		this.texTag = texTag;
		this.bitmapName = name;
		this.context = context;
		model = ResIdModel;
	}
	
	public GlTexture(String texTag, Bitmap bitmap) {
		this.texTag = texTag;
		this.bitmap = bitmap;
		model = BitmapModel;
		isNeedReinit = true;
	}
	
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
		isNeedReinit = true;
	}
	
	protected void setNeedReinit() {
		isNeedReinit = true;
	}
	
	protected Bitmap getBitmap() {
		return null;
	}
	
	public String getTexTag() {
		return this.texTag;
	}
	
	public boolean isInit(long reinitId) {
		return this.reinitId >= reinitId;
	}
	
	public boolean getIsReinit() {
		return isNeedReinit;
	}
	
	public int getTexId() {
		return texId[0];
	}
	
	private void deleteTexture(long reinitId) {
		if (this.reinitId >= reinitId){	
			GLES20.glDeleteTextures(1, texId, 0);
			this.reinitId = -1;
		}
	}
	
	public void uninit(long reinitId) {
		deleteTexture(reinitId);
	}
	
	public int initTex(long reinitId) {
		if (ThirdModel == model && (isNeedReinit || !isInit(reinitId))) {
			isNeedReinit = false;
			deleteTexture(reinitId);
			bitmap = getBitmap();
		} else if (BitmapModel == model && isNeedReinit) {
			isNeedReinit = false;
			deleteTexture(reinitId);
		}
		int tmp = reinitTex(reinitId);
		return tmp;
	}

	public int reinitTex(long reinitId) {
		if (this.reinitId >= reinitId) {
			return texId[0];
		}
		this.reinitId = reinitId;
		if (model == BitmapModel || model == ThirdModel) {
			GlTextureUtil.initTexture2D(bitmap, texId);
		} else if (model == ResIdModel) {
			GlTextureUtil.initTexture2D(context, bitmapName, texId);
		} else if (model == CreateModel) {
			GlTextureUtil.createTexture2D(width, height, texId);
		}
		return texId[0];
	}
}
