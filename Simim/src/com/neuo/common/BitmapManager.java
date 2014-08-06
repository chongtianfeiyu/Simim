package com.neuo.common;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Iterator;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.SparseArray;

public class BitmapManager {
	private static final int CreateWidthFactor = 1024;
	public static class BitmapInfo{
		public SoftReference<Bitmap> bitmapReference;
	}
	HashMap<String, BitmapInfo> bitmaps = new HashMap<String, BitmapManager.BitmapInfo>();
	SparseArray<BitmapInfo> createBitmaps = new SparseArray<BitmapManager.BitmapInfo>();
	
	private static BitmapManager bitmapManager;
	
	public static void release() {
		if (bitmapManager != null) {
			bitmapManager.uninit();
			bitmapManager = null;
		}
	}
	
	public synchronized static BitmapManager getBitmapManager() {
		if (null == bitmapManager) {
			bitmapManager = new BitmapManager();
		}
		return bitmapManager;
	}
	
	public void uninit() {

		for (Iterator<HashMap.Entry<String,BitmapManager.BitmapInfo>> iterator = bitmaps.entrySet().iterator();
						iterator.hasNext();) {
			Bitmap tmp = iterator.next().getValue().bitmapReference.get();
			if (tmp != null) {
				tmp.recycle();
			}
		}
		bitmaps.clear();
		for (int i = 0; i < createBitmaps.size(); i++) {
			Bitmap tmp = createBitmaps.valueAt(i).bitmapReference.get();
			if (tmp != null) {
				tmp.recycle();
			}
		}
		createBitmaps.clear();
	}
	
	private Bitmap loadBitmap(String name, Resources r) {
		Bitmap bitmap = BitmapUtil.readBitmap(r, name);
		if (bitmap == null) {
			return null;
		}
		BitmapInfo bInfo = new BitmapInfo();
		bInfo.bitmapReference = new SoftReference<Bitmap>(bitmap);
		synchronized (bitmaps) {
			bitmaps.put(name, bInfo);
		}
		return bitmap;
	}
	
	
	private Bitmap createBitmap(int resId, int width, int height) {
		Bitmap bitmap = BitmapUtil.createBitmap(width, height);
		BitmapInfo bInfo = new BitmapInfo();
		bInfo.bitmapReference = new SoftReference<Bitmap>(bitmap);
		synchronized (createBitmaps) {
			createBitmaps.put(resId, bInfo);
		}
		return bitmap;
	}
	
	public Bitmap getBitmap(int width, int height) {
		BitmapInfo bInfo;
		int resId = width * CreateWidthFactor + height;
		synchronized (createBitmaps) {
			bInfo = createBitmaps.get(resId);
		}
		if (null == bInfo) {
			return createBitmap(resId, width, height);
		}
		Bitmap bitmap = bInfo.bitmapReference.get();
		if (null == bitmap) {
			return createBitmap(resId, width, height);
		}
		return bitmap;
	}
	
	public Bitmap getBitmap(String name, Resources r) {
		BitmapInfo bInfo;
		synchronized (bitmaps) {
			bInfo = bitmaps.get(name);
		}
		if (null == bInfo) {
			return loadBitmap(name, r);
		}
		Bitmap bitmap = bInfo.bitmapReference.get();
		if (null == bitmap) {
			return loadBitmap(name, r);
		}
		return bitmap;
	}
	
	/*
	public Bitmap getBitmap(int resId, Resources r) {
		BitmapInfo bInfo;
		synchronized (bitmaps) {
			bInfo = bitmaps.get(resId);
		}
		if (null == bInfo) {
			return loadBitmap(resId, r);
		}
		Bitmap bitmap = bInfo.bitmapReference.get();
		if (null == bitmap) {
			return loadBitmap(resId, r);
		}
		return bitmap;
	}
	*/
}
