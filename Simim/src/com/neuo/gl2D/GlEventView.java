package com.neuo.gl2D;


import com.neuo.common.CommonEvent;
import com.neuo.common.CommonEvent.Event;
import com.neuo.glcommon.GlView;
import com.neuo.globject.GlObject;
import com.neuo.glshader.GlShader;

public abstract class GlEventView extends GlContainer2D {

	public static interface ClickedListener {
		public void onClicked(GlEventView view, Event e, boolean isDouble);
	}
	
	public static interface ClickListener {
		public void onClick(GlEventView view, Event e, boolean isDouble);
	}
	
	public static interface MoveListener {
		public void onMove(GlEventView view, Event e, boolean isScroll);
	}
	
	public static interface DownStateListener {
		public void onDownState(GlEventView view, boolean isDown);
	}
	
	public static interface ContainStateListener {
		public void onContainState(GlEventView view, boolean isContain);
	}
	
	protected boolean isDown;
	protected boolean isContain;
	protected ClickedListener clickedListener;
	protected ClickListener clickListener;
	protected MoveListener moveListener;
	protected DownStateListener downStateListener;
	protected ContainStateListener containStateListener;
	public GlEventView(String name, GlView glView, GlShader glShader,
			GlObject glObject) {
		super(name, glView, glShader, glObject);
		setCanTouch(true);
	}
	
	public void setDownStateListner(DownStateListener downStateListener) {
		this.downStateListener = downStateListener;
	}
	
	public void setContainStateListener(ContainStateListener containStateListener) {
		this.containStateListener = containStateListener;
	}
	
	public void setClickListener(ClickListener clickListener) {
		this.clickListener = clickListener;
	}

	public void setClickedListener(ClickedListener clickedListener) {
		this.clickedListener = clickedListener;
	}
	
	public void setMoveListener(MoveListener moveListener) {
		this.moveListener = moveListener;
	}
	
	@Override
	protected void init() {
		super.init();
		isDown = false;
		isContain = false;
		clickedListener = null;
		moveListener = null;
	}
	
	@Override
	protected void removeParent() {
		if (this.parent != null && isDown) {
			this.parent.resetTouchState();
		}
		super.removeParent();
	}

	@Override
	public int onHanderEventSelf(Event e) {
		int res = CommonEvent.None;
		if ((e.action & keyEvent) != 0) {
			res = handerKeyEvent(e);
		} else {
			if (e.action == CommonEvent.Double) {
				res = onDouble(e);
			} else if (e.action == CommonEvent.Down) {
				res = onSingleDown(e);
			} else if (e.action == CommonEvent.Up) {
				res = onUp(e);
			} else if (e.action == CommonEvent.Fling) {
				res = onFling(e);
			} else if (e.action == CommonEvent.LongPress) {
				res = onLongPress(e);
			} else if (e.action == CommonEvent.Scroll) {
				res = onScroll(e);
			} else if (e.action == CommonEvent.SingleConfirm) {
				res = onSingleConfirm(e);
			} else if (e.action == CommonEvent.Cancle) {
				res = onCancle(e);
			}
		}
		return res;
	}
	
	@Override
	public void setCanTouch(boolean isCanTouch) {
		this.canDealEvent = isCanTouch;
		if (!isCanTouch) {
			resetTouchState();
		}
	}
	
	protected int onSingleConfirm(Event e) {
		if (clickedListener != null)clickedListener.onClicked(this, e, false);
		setContainState(false);
		setDownState(false);
		return CommonEvent.None;
	}
	
	protected int onDouble(Event e) {
		if (clickListener != null)clickListener.onClick(this, e, true);
		return CommonEvent.None;
	}
	
	protected int onSingleDown(Event e) {
		setContainState(true);
		setDownState(true);
		if (clickListener != null)clickListener.onClick(this, e, false);
		return CommonEvent.Up | CommonEvent.Fling | CommonEvent.SingleConfirm
				| CommonEvent.LongPress | CommonEvent.Scroll | CommonEvent.Cancle;
	}
	
	protected int onFling(Event e) {
		if (moveListener != null)moveListener.onMove(this, e, false);
		setContainState(false);
		setDownState(false);
		return CommonEvent.SingleConfirm;
	}
	
	protected int onScroll(Event e) {
		if (moveListener != null)moveListener.onMove(this, e, true);
		setContainState(false);
		return CommonEvent.Up | CommonEvent.Fling | CommonEvent.LongPress |
				CommonEvent.Scroll | CommonEvent.Cancle | CommonEvent.SingleConfirm;
	}
	
	protected int onUp(Event e) {
		setContainState(false);
		setDownState(false);
		return CommonEvent.SingleConfirm;
	}
	
	protected int onCancle(Event e) {
		setContainState(false);
		setDownState(false);
		return CommonEvent.None;
	}
	
	protected int onLongPress(Event e) {
		setContainState(false);
		return CommonEvent.Up | CommonEvent.Fling | CommonEvent.Scroll | CommonEvent.Cancle;
	}
	
	protected void setContainState(boolean containState) {
		if (this.isContain != containState) {
			this.isContain = containState;
			if (this.isContain) {
				onContainState();
			} else {
				onUnContainState();
			}
			if (containStateListener != null) {
				containStateListener.onContainState(this, this.isContain);
			}
		}
	}
	
	protected void onContainState() {
	}
	
	protected void onUnContainState() {
	}
	
	protected void setDownState(boolean downState) {
		if (this.isDown != downState) {
			this.isDown = downState;
			if (downState) {
				onDownState();
			} else {
				onUnDownState();
			}
			if (downStateListener != null) {
				downStateListener.onDownState(this, this.isDown);
			}
		}
	}
	
	protected void onDownState() {
	}
	
	protected void onUnDownState() {
	}
	
	@Override
	public void resetTouchState() {
		setDownState(false);
		setContainState(false);
		if (this.parent != null) {
			this.parent.resetTouchState();
		}
	}
}
