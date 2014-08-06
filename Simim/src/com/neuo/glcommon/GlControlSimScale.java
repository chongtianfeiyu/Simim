package com.neuo.glcommon;

import com.neuo.common.Controler;
import com.neuo.common.Controler.TriggleEventHandle;

public class GlControlSimScale extends GlControler.GlControlScale
implements TriggleEventHandle {
	public static class SimScaleTriggleInfo extends Controler.TriggleInfo {
		public float scale;
		
		public SimScaleTriggleInfo() {
			clear();
		}
		
		public void clear() {
			super.clear();
			scale = 0;
		}

		@Override
		public Object getValue(int i) {
			return (Float)scale;
		}
	}
	
	public class SimScaleControler extends Controler {
		public SimScaleControler() {
			super(1);
		}
		@Override
		public TriggleInfo createInfo() {
			return new SimScaleTriggleInfo();
		}

		@Override
		public float calcutLength() {
			return Math.abs(endScale - startScale);
		}

		@Override
		public void calcuValue() {
			float currLength = getCurrLength();
			float length = getLength();
			float process;
			if (length == 0f) {
				process = 0f;
			} else if (scaleMode == constantMode) {
				process = currLength / length;
			} else if (scaleMode == upMode) {
				process = (float)Math.pow(currLength / length, 2);
			} else {
				process = 1f - (float)Math.pow(1 - currLength / length, 2);
			}
			currScale = (endScale - startScale) * process + startScale;
			notifyChange();
		}

		@Override
		public void setValueS(int i, Object value) {
			startScale = (Float)value;
		}

		@Override
		public void setValueE(int i, Object value) {
			endScale = (Float)value;
		}

		@Override
		public void active(boolean isActive) {
			if (isActive) {
				activeSelf();
			} else {
				unActiveSelf();
			}
		}

		@Override
		public SimScaleTriggleInfo getTriggleInfo() {
			return (SimScaleTriggleInfo)priGetTriggleInfo();
		}
		@Override
		public void updateValueS() {
			startScale = currScale;
		}
		
	}
	
	private float startScale;
	private float endScale;
	private float currScale;
	private Controler controler;
	private int scaleMode;
	
	public static final int constantMode = 0;
	public static final int upMode = 1;
	public static final int downMode = 2;
	
	private static final int defaultMode = constantMode;
	public GlControlSimScale() {
		startScale = 0;
		endScale = 0;
		currScale = 0;
		scaleMode = defaultMode;
		controler = new SimScaleControler();
	}
	
	public void setScaleMode(int mode) {
		this.scaleMode = mode;
	}
	
	public void setStartAndEnd(float s, float e, int mode) {
		controler.setMode(mode);
		startScale = s;
		endScale = e;
		controler.triggleCalcuLength();
	}
	
	@Override
	public Controler getControler() {
		return controler;
	}

	@Override
	public float getScaleX() {
		return currScale;
	}

	public float getScaleY() {
		return currScale;
	}

}
