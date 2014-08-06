package com.neuo.common;

import java.util.HashMap;

public class FontManager {
	private static FontManager fontManager;
	private HashMap<String, CommonFont> fonts = new HashMap<String, CommonFont>();
	
	public static void release() {
		fontManager = null;
	}

	public static FontManager getFontManager() {
		if (null == fontManager) {
			fontManager = new FontManager();
		}
		return fontManager;
	}
	
	public void putFont(CommonFont font) {
		fonts.put(font.getFontName(), font);
	}
	
	public CommonFont getFont(String fontName) {
		return fonts.get(fontName);
	}
}
