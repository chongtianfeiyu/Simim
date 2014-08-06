package com.neuo.glcommon;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class GlControler {
	public static interface GlControlHandle {
		public void unActiveState(int state);
		public void activeState(int state);
		public void setStateChange(int state);
		public void register(GlControler glControler);
		public void unRegister(int state);
	}

	
	public static final int transState = 1 << 0;
	public static final int rotateState = 1 << 1;
	public static final int scaleState = 1 << 2;
	public static final int alphaState = 1 << 3;
	
	protected ArrayList<GlControlHandle> controls;
	protected int state;
	
	public GlControler() {
		state = 0;
		controls = new ArrayList<GlControlHandle>();
	}
	
	protected void setState(int state) {
		this.state = state;
	}
	
	public int getState() {
		return this.state;
	}
	
	public void unRegisterAll() {
		for (Iterator<GlControlHandle> iterator = controls.iterator();iterator.hasNext();) {
			GlControlHandle tmpControl = iterator.next();
			tmpControl.unActiveState(state);
			iterator.remove();
		}
	}
	
	public void registerSelf(GlControlHandle control) {
		this.controls.add(control);
	}

	public void notifyChange() {
		for (Iterator<GlControlHandle> iterator = controls.iterator(); iterator.hasNext();) {
			iterator.next().setStateChange(state);
		}
	}
	
	public void activeSelf() {
		for (Iterator<GlControlHandle> iterator = controls.iterator();iterator.hasNext();) {
			iterator.next().activeState(state);
		}
	}
	
	public void unActiveSelf() {
		for (Iterator<GlControlHandle> iterator = controls.iterator();iterator.hasNext();) {
			iterator.next().unActiveState(state);
		}
	}

	public void unRegisterSelf(GlControlHandle control) {
		if (controls.remove(control)) {
			control.unActiveState(state);
		}
	}
	
	public abstract static class GlControlAlpha extends GlControler {
		public GlControlAlpha() {
			setState(alphaState);
		}
		
		public abstract float getAlpha();
	}
	
	public abstract static class GlControlScale extends GlControler {
		public GlControlScale() {
			setState(scaleState);
		}
		
		public abstract float getScaleX();
		public abstract float getScaleY();
	}
	
	public abstract static class GlControlRotate extends GlControler {
		public GlControlRotate() {
			setState(rotateState);
		}
		
		public abstract int getAxis();
		public abstract float getAngle();
	}
	
	public abstract static class GlControlTrans extends GlControler {
		public GlControlTrans() {
			setState(transState);
		}

		public abstract float getX();
		public abstract float getY();
		public abstract float getZ();
	}
}
