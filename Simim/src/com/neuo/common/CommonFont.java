package com.neuo.common;

import android.content.Context;
import android.graphics.Typeface;

public class CommonFont {
	private static final String FontPre = "font/";
	private String fontName;
	private String fileName;
	private Typeface fontTypeface;
	
	public CommonFont(String fontName, String fileName) {
		this.fontName = fontName;
		this.fileName = fileName;
	}
	
	public String getFontName() {
		return fontName;
	}
	
	public Typeface getFont(Context context) {
		if (null != fontTypeface) {
			return fontTypeface;
		} else {
			fontTypeface = Typeface.createFromAsset(context.getAssets(), FontPre + fileName);
			return fontTypeface;
		}
	}
}
