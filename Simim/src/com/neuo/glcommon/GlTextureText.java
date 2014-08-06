package com.neuo.glcommon;

import com.neuo.common.BitmapManager;
import com.neuo.common.BitmapUtil;
import com.neuo.common.ColorUtil;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextPaint;

public class GlTextureText extends GlTexture {
	private static final float defaultTextSize = 25f;
	private static final int defaultColor = ColorUtil.White;
	private static final int defaultAlpha = 255;
	private static final boolean defaultIsBold = false;
	private static final int defaultHAlign = BitmapUtil.Center;
	private static final int defaultVAlign = BitmapUtil.Center;
	private TextPaint textPaint;
	private float textSize;
	private String text;
	private int textColor;
	private int textAlpha;
	private int width;
	private int height;
	private boolean isBold;
	private int hAlign, vAlign;
	private Typeface textFont;
	
	public GlTextureText(String tagName, int width, int height, String text) {
		super(tagName);
		textSize = defaultTextSize;
		this.width = width;
		this.height = height;
		this.text = text;
		this.textPaint = new TextPaint();
		this.textColor = defaultColor;
		this.textAlpha = defaultAlpha;
		this.textFont = null;
		this.isBold = defaultIsBold;
		this.hAlign = defaultHAlign;
		this.vAlign = defaultVAlign;
	}

	public void setTextSize(float textSize) {
		if (this.textSize != textSize) {
			this.textSize = textSize;
			setNeedReinit();
		}
	}
	
	public void setTextColor(int textColor) {
		if (this.textColor != textColor) {
			this.textColor = textColor;
			setNeedReinit();
		}
	}
	
	public void setTextFont(Typeface font) {
		if (this.textFont != font) {
			this.textFont = font;
			setNeedReinit();
		}
	}
	
	public void setTextBold(boolean isBold) {
		if (this.isBold != isBold) {
			this.isBold = isBold;
			setNeedReinit();
		}
	}
	
	public void setTextAlign(int hAlign, int vAlign) {
		boolean isChange = false;
		if (this.hAlign != hAlign) {
			this.hAlign = hAlign;
			isChange = true;
		}
		if (this.vAlign != vAlign) {
			this.vAlign = vAlign;
			isChange = true;
		}
		if (isChange) {
			setNeedReinit();
		}
	}

	public void setTextAlpha(int textAlpha) {
		if (this.textAlpha != textAlpha) {
			this.textAlpha = textAlpha;
			setNeedReinit();
		}
	}
	
	public void setText(String text) {
		if (!this.text.equals(text)) {
			this.text = text;
			setNeedReinit();
		}
	}
	
	protected Bitmap getBitmap() {
		Bitmap bitmap = BitmapManager.getBitmapManager().getBitmap(width, height);
		textPaint.setTextSize(textSize);
		textPaint.setColor(textColor);
		textPaint.setAlpha(textAlpha);
		textPaint.setTypeface(textFont);
		textPaint.setFakeBoldText(isBold);
		bitmap.eraseColor(Color.TRANSPARENT);
		BitmapUtil.writeText(bitmap, text, hAlign, vAlign, textPaint);
		return bitmap;
	}
	
}