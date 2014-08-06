package com.neuo.glcommon;

import java.util.HashMap;

public class GlTextureManager {
	private HashMap<String, GlTexture> texMap = new HashMap<String, GlTexture>();
	
	private static GlTextureManager glTextureManager;
	public static void release() {
		glTextureManager = null;
	}

	public static GlTextureManager getTextureManager() {
		if (null == glTextureManager) {
			glTextureManager = new GlTextureManager();
		}
		return glTextureManager;
	}
	
	public void putTex(GlTexture glTexture) {
		texMap.put(glTexture.getTexTag(), glTexture);
	}
	
	public GlTexture getTex(String texTag) {
		return texMap.get(texTag);
	}
}
