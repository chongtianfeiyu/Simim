package com.neuo.common;

import android.annotation.SuppressLint;
import android.view.MotionEvent;

public abstract class CommonEvent {
	public static final int None = 0;
	public static final int Down = 1 << 0;
	public static final int Up = 1 << 1;
	public static final int Move = 1 << 2;
	public static final int Back = 1 << 3;
	public static final int Cancle = 1 << 4;
	public static final int KeyUp = 1 << 5;
	public static final int KeyDown = 1 << 6;
	public static final int SingleConfirm = 1 << 7;
	public static final int Fling = 1 << 8;
	public static final int LongPress = 1 << 9;
	public static final int Double = 1 << 10;
	public static final int Scroll = 1 << 11;
	public static final int All = Down | Up | Move | Cancle | SingleConfirm | Fling | LongPress | Double | Scroll;
	public static final int KeyEvent = KeyDown | KeyUp | Back;

	public static class Event {
		public int action;
		public MotionEvent e;
		public boolean isDouble;
		public float x, y;
		
		public Event(int action) {
			this.action = action;
			isDouble = false;
		}
		
		public Event(int action, float x, float y) {
			this.action = action;
			this.e = null;
			isDouble = false;
			this.x = x;
			this.y = y;
		}
		
		@SuppressLint("Recycle")
		public Event(int action, MotionEvent e) {
			this.action = action;
			this.e = MotionEvent.obtainNoHistory(e);
			isDouble = false;
		}
		
		public Event(int action, MotionEvent e, boolean isDouble) {
			this.action = action;
			this.e = MotionEvent.obtainNoHistory(e);
			this.isDouble = isDouble;
		}
		
		public Event(int action, MotionEvent e, boolean isDouble, float x, float y) {
			this.action = action;
			this.e = MotionEvent.obtainNoHistory(e);
			this.isDouble = isDouble;
			this.x = x;
			this.y = y;
		}
		
		public MotionEvent remove() {
			MotionEvent tmp = e;
			if (e != null) {
				e = null;
			}
			return tmp;
		}
		
		public void clear() {
			if (e != null) {
				e.recycle();
				e = null;
			}
		}
	}
	
	public CommonEvent() {
		init();
	}
	
	protected void init() {
		clear();
	}
	
	public void clear() {
	}

	protected abstract void deal(Event e);
}
