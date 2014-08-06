package com.neuo.android;

public class ScreenRatioUtil {
	
	public static class ScreenRatio {
		public static final int X3X2 = 0;
		public static final int X5X3 = 1;
		public static final int X16X9 = 2;
	}
	
	private static ScreenRatioUtil screenUtil;
	
	private int screenHeight;
	private int screenWidth;
	private int screenRatio;
	private final int defaultRatio = ScreenRatio.X16X9;
	
	private final float x3x2Ratio = 3f / 2f;
	private final float x5x3Ratio = 5f / 3f;
	private final float x16x9Ratio = 16f / 9f;
	
	private final float disInterval = 0.05f;
	
	public static void release() {
		screenUtil = null;
	}
	
	public static ScreenRatioUtil getBitmapUtil() {
		return screenUtil;
	}
	
	public static ScreenRatioUtil initScreenUtil(int height, int width) {
		if (null == screenUtil) {
			screenUtil = new ScreenRatioUtil(height, width);
		}
		return screenUtil;
	}
	
	private ScreenRatioUtil(int height, int width) {
		screenHeight = height;
		screenWidth = width;
		
		float tmpRatio = (float)screenHeight / (float)screenWidth;
		if (Math.abs(tmpRatio - x16x9Ratio) < disInterval) {
			screenRatio = ScreenRatio.X16X9;
		} else if (Math.abs(tmpRatio - x5x3Ratio) < disInterval) {
			screenRatio = ScreenRatio.X5X3;
		} else if (Math.abs(tmpRatio - x3x2Ratio) < disInterval) {
			screenRatio = ScreenRatio.X3X2;
		} else {
			screenRatio = defaultRatio;
		}
	}
	
	public int getRatio() {
		return screenRatio;
	}
	
	public int getScreenRatio() {
		return screenRatio;
	}
	
	/*
	public RectF toScreenFit(RectF sRectPer, RectF dRectF) {
		dRectF.left = sRectPer.left / fixWidth * screenWidth;
		dRectF.top = sRectPer.top / fixHeight * screenHeight;
		dRectF.right = sRectPer.right / fixWidth * screenWidth;
		dRectF.bottom = sRectPer.bottom / fixHeight * screenHeight;
		return dRectF;
	}
	
	public static RectF toScreenFit(RectF sRectPer, RectF dRectF, int sWidth, int sHeight) {
		dRectF.left = sRectPer.left / fixWidth * sWidth;
		dRectF.top = sRectPer.top / fixHeight * sHeight;
		dRectF.right = sRectPer.right / fixWidth * sWidth;
		dRectF.bottom = sRectPer.bottom / fixHeight * sHeight;
		return dRectF;
	}
	
	private static final float fixWidth = 1024;
	private static final float fixHeight = 1024;
	
	public static float getFixWidth() {
		return fixWidth;
	}
	
	public static float getFixHeight() {
		return fixHeight;
	}
	
	public static RectF perToFit(float left, float top, float right, float bottom,
										float wScale, float hScale, RectF sRectPer) {
		if (left >= 0)sRectPer.left = fixWidth * left / wScale;
		if (right >= 0)sRectPer.right = fixWidth * right / wScale;
		if (top >= 0)sRectPer.top = fixHeight * top / hScale;
		if (bottom >= 0)sRectPer.bottom = fixHeight * bottom / hScale;
		return sRectPer;
	}
	
	public static void toFit(int dWidth, int dHeight, int sWidth, int sHeight,
			RectF sRectPer, Rect dRect, Rect sRect) {
		float left = sRectPer.left / fixWidth * dWidth;
		float top = sRectPer.top / fixHeight * dHeight;
		float right = sRectPer.right / fixWidth * dWidth;
		float bottom = sRectPer.bottom / fixHeight * dHeight;
		
		dRect.left = (int)left;
		dRect.top = (int)top;
		dRect.right = (int)right;
		dRect.bottom = (int)bottom;
		
		float widthRatio = (float) sWidth / dRect.width();
		float heightRatio = (float) sHeight / dRect.height();
		
		if (heightRatio > widthRatio) {
			sRect.left = 0;
			sRect.right = sWidth;
			sRect.top = sHeight / 2 - (int)(widthRatio * dRect.height() / 2);
			sRect.bottom = sHeight - sRect.top;
		} else {
			sRect.top = 0;
			sRect.bottom = sHeight;
			sRect.left = sWidth / 2 - (int)(heightRatio * dRect.width() / 2);
			sRect.right = sWidth - sRect.left;
		}
		
	}
	*/
}
