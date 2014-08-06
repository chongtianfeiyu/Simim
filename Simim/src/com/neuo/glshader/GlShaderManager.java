package com.neuo.glshader;

import java.util.HashMap;

import android.content.Context;

public class GlShaderManager {
	public static final int Direct = 1 << 0;
	public static final int Tex = 1 << 1;
	public static final int Light = 1 << 2;
	public static final int LightTex = 1 << 3;
	public static final int Blur = 1 << 4;
	public static final int All = Direct | Tex | Light | LightTex | Blur;
	
	private HashMap<Integer, GlShader> shaderHashMap = new HashMap<Integer, GlShader>();
	private Context context;
	private static GlShaderManager shaderManager;
	
	public static void release() {
		shaderManager = null;
	}
	
	public static GlShaderManager getShaderManger(Context context) {
		if (null == shaderManager) {
			shaderManager = new GlShaderManager(context);
		}
		return shaderManager;
	}
	
	public static GlShaderManager getShaderManager() {
		return shaderManager;
	}
	
	private GlShaderManager(Context context) {
		this.context = context;
	}
	
	public void refreshShader(int model, long initId) {
		if ((model & Direct) != 0) {
			getShader(ShaderByDirect.myShaderTag).initShader(initId, context);
		}
		if ((model & Tex) != 0) {
			getShader(ShaderByTex.myShaderTag).initShader(initId, context);
		}
		if ((model & Light) != 0) {
			getShader(ShaderByLight.myShaderTag).initShader(initId, context);
		}
		if ((model & LightTex) != 0) {
			getShader(ShaderByLightTex.myShaderTag).initShader(initId, context);
		}
		if ((model & Blur) != 0) {
			getShader(ShaderByBlur.myShaderTag).initShader(initId, context);
		}
	}
	
	public void init(int model) {
		if ((model & Direct) != 0) {
			putShader(new ShaderByDirect());
		}
		if ((model & Tex) != 0) {
			putShader(new ShaderByTex());
		}
		if ((model & Light) != 0) {
			putShader(new ShaderByLight());
		}
		if ((model & LightTex) != 0) {
			putShader(new ShaderByLightTex());
		}
		if ((model & Blur) != 0) {
			putShader(new ShaderByBlur());
		}
	}
	
	public void putShader(GlShader myShader) {
		shaderHashMap.put(myShader.getTag(), myShader);
	}
	
	public GlShader getShader(int shaderTag) {
		return shaderHashMap.get(shaderTag);
	}
}
