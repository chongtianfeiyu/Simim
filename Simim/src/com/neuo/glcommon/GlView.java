package com.neuo.glcommon;

import android.content.Context;

import com.neuo.common.CommonEvent.Event;
import com.neuo.common.CommonEvent;
import com.neuo.common.EventHander;

public abstract class GlView
implements Comparable<Object>, EventHander {
	protected long resizeId = -1;
	protected long reinitId = -1;
	protected int viewId = -1;
	protected boolean isVisible = true;
	protected Context context = null;
	protected int width;
	protected int height;
	protected boolean isSizeInit = false;
	
	public GlView(Context context) {
		this.context = context;
	}
	
	public Context getContext() {
		return context;
	}

	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int compareTo(Object another) {
		GlView view = (GlView)another;
		return this.viewId - view.viewId;
	}
	
	public boolean getIsVisible() {
		return isVisible;
	}
	
	public void setIsVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	
	public int getViewId() {
		return viewId;
	}
	
	public void setViewId(int viewId) {
		this.viewId = viewId;
	}
	
	public long getResizeId() {
		return resizeId;
	}
	
	protected void setResizeId(long resizeId) {
		this.resizeId = resizeId;
	}
	
	public long getReinitId() {
		return reinitId;
	}
	
	protected void setReinitId(long reinitId) {
		this.reinitId = reinitId;
	}
	
	public void onDraw() {
		drawSelf();
	}

	public void reGlInit(long reinitId) {
		if (this.getReinitId() >= reinitId)return;
		this.setReinitId(reinitId);	
	}
	
	public abstract void setScreenSize();
	public void reScreenSize(long resizeId, int width, int height) {
		if (this.getResizeId() >= resizeId || isSizeInit)return;
		this.setResizeId(resizeId);
		this.width = width;
		this.height = height;
		setScreenSize();
		isSizeInit = true;
	}
	
	public abstract EventHander isHanderEventSelf(Event e);
	public EventHander isHanderEvent(Event e) {
		if (isSizeInit) {
			return isHanderEventSelf(e);
		} else {
			return null;
		}
	}

	public abstract int onHanderEventSelf(Event e);
	public int onHanderEvent(Event e) {
		if (isSizeInit) {
			return onHanderEventSelf(e);
		} else {
			return CommonEvent.None;
		}
	}

	public abstract void runSelf(long interval);
	public void run(long interval) {
		if (isSizeInit) {
			runSelf(interval);
		}
	}
	
	public abstract void drawSelf();
	public abstract void init();
	public abstract void uninit();
	public abstract void onResume();
	public abstract void onPause();
}
