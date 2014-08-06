package com.neuo.glcommon;

import com.neuo.common.Controler;
import com.neuo.common.Controler.TriggleEventHandle;

public class GlControlSimAlpha extends GlControler.GlControlAlpha
implements TriggleEventHandle {
	public static class SimAlphaTriggleInfo extends Controler.TriggleInfo {
		public float alpha;
		
		public SimAlphaTriggleInfo() {
			clear();
		}
		
		public void clear() {
			super.clear();
			alpha = 0;
		}

		@Override
		public Object getValue(int i) {
			return (Float)alpha;
		}
	}
	
	public class SimAlphaControler extends Controler {
		public SimAlphaControler() {
			super(1);
		}
		@Override
		public TriggleInfo createInfo() {
			return new SimAlphaTriggleInfo();
		}

		@Override
		public float calcutLength() {
			return Math.abs(endAlpha - startAlpha);
		}

		@Override
		public void calcuValue() {
			float currLength = getCurrLength();
			float length = getLength();
			float process;
			if (length == 0f) {
				process = 0f;
			} else if (alphaMode == constantMode) {
				process = currLength / length;
			} else if (alphaMode == upMode) {
				process = (float)Math.pow(currLength / length, 2);
			} else {
				process = 1f - (float)Math.pow(1 - currLength / length, 2);
			}
			currAlpha = (endAlpha - startAlpha) * process + startAlpha;
			notifyChange();
		}

		@Override
		public void setValueS(int i, Object value) {
			startAlpha = (Float)value;
		}

		@Override
		public void setValueE(int i, Object value) {
			endAlpha = (Float)value;
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
		public SimAlphaTriggleInfo getTriggleInfo() {
			return (SimAlphaTriggleInfo)priGetTriggleInfo();
		}
		@Override
		public void updateValueS() {
			startAlpha = currAlpha;
		}
		
	}
	
	private float startAlpha;
	private float endAlpha;
	private float currAlpha;
	private Controler controler;
	private int alphaMode;

	public static final int constantMode = 0;
	public static final int upMode = 1;
	public static final int downMode = 2;
	
	public GlControlSimAlpha() {
		startAlpha = 0;
		endAlpha = 0;
		currAlpha = 0;
		alphaMode = constantMode;
		controler = new SimAlphaControler();
	}
	
	public void setAlphaMode(int alphaMode) {
		this.alphaMode = alphaMode;
	}
	
	public void setStartAndEnd(float s, float e, int mode) {
		controler.setMode(mode);
		startAlpha = s;
		endAlpha = e;
		controler.triggleCalcuLength();
	}
	
	@Override
	public Controler getControler() {
		return controler;
	}

	@Override
	public float getAlpha() {
		return currAlpha;
	}
}
