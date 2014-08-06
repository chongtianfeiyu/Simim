package com.neuo.common;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;

public class BitmapUtil {
	private static final String BitMapPre = "data";
	private static boolean isCrypto = false;
	
	public static void setIsCrypton(boolean isCrypto) {
		BitmapUtil.isCrypto = isCrypto;
	}
	
	public static Bitmap readBitmap(Resources r, int resId) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		InputStream isInputStream = r.openRawResource(resId);
		return BitmapFactory.decodeStream(isInputStream, null, options);
	}
	
	public static Bitmap readBitmap(Resources r, String name) {
		String bitMapName;
		String la = ConfigureManager.getConfigureManager().getLaugauge();
		InputStream inputStream = null;
		if (la == null || la.equals("")) {
			bitMapName = BitMapPre + "/" + name;
		} else {
			bitMapName = BitMapPre + "-" + la + "/" + name;
			try {
				inputStream = r.getAssets().open(bitMapName);
			} catch (IOException e) {
				bitMapName = BitMapPre + "/" + name;
			}
		}
		
		byte[] bytes = null;
		if (inputStream == null) {
			try {
				inputStream = r.getAssets().open(bitMapName);
			} catch (IOException e) {
				throw new RuntimeException("load resource fail " + name + " " + la);
			}
		}
		try {
			bytes = new byte[inputStream.available()];
			inputStream.read(bytes);
		} catch (IOException e) {
			throw new RuntimeException("read file " + name + " " + la);
		}
		if (isCrypto) {
			bytes = SimCrypto.decodeFile(bytes, 0, bytes.length);
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
	}
	
	public static Bitmap createBitmap(int width, int height) {
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		return bitmap;
	}
	
	public static final int Right 	= 0;
	public static final int Center 	= 1;
	public static final int Left 	= 2;
	public static final int Top 	= 3;
	public static final int Bottom 	= 4;
	
	public static void writeText(Bitmap bitmap, String text, int horizontal, int vertical, TextPaint paint) {
		Canvas canvas = new Canvas(bitmap);
		if (horizontal == Center) {
			paint.setTextAlign(Align.CENTER);
		} else if (horizontal == Left) {
			paint.setTextAlign(Align.LEFT);
		} else {
			paint.setTextAlign(Align.RIGHT);
		}
		
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		StaticLayout test = new StaticLayout(text, paint, bitmap.getWidth(), Alignment.ALIGN_NORMAL, 1, 1, true);
		float x = 0, y = 0;
		if (vertical == Center) {
			y = (canvas.getHeight() - test.getHeight()) / 2;
		} else if (vertical == Top) {
			y = 0;
		} else {
			y = canvas.getHeight() - test.getHeight();
		}
		if (horizontal == Center) {
			x = canvas.getWidth() / 2;
		} else if (horizontal == Left) {
			x = 0;
		} else {
			x = canvas.getWidth();
		}
		canvas.translate(x, y);
		test.draw(canvas);
		//text.
		//canvas.drawText(text, x, y, paint);
	}
	
}
