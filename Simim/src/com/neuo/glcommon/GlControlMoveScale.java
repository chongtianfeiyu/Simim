package com.neuo.glcommon;

import com.neuo.common.Controler;


public class GlControlMoveScale extends GlControler.GlControlScale
implements Controler.TriggleEventHandle {
	public static class MoveScaleTriggleInfo extends Controler.TriggleInfo {
		
		public MoveScaleTriggleInfo() {
			clear();
		}
		
		public void clear() {
			super.clear();
		}

		@Override
		public Object getValue(int i) {
			return null;
		}
	}
	
	public class MoveScaleControler extends Controler {
		public MoveScaleControler() {
			super(1);
		}
		
		@Override
		public TriggleInfo createInfo() {
			return new MoveScaleTriggleInfo();
		}

		@Override
		public float calcutLength() {
			return baseTime;
		}

		@Override
		public void calcuValue() {
			float currLength = getCurrLength();
			float length = getLength();
			float process = currLength / length;
			float process1 = 0f;
			float process2 = 0f;
			if (process == 0f) {
				process1 = 0;
				process2 = 0;
			} else if (process == 1f) {
				process1 = 1f;
				process2 = 1f;
			} else if (process >= extendTime) {
				process2 = (process - extendTime) / (1f - extendTime) * (1f - extendStart) + extendStart;
				process1 = extendScale + (process - extendTime) / (1f - extendTime) * (1f - extendScale);
			} else {
				process2 = extendStart;
				process1 = process / extendTime * extendScale;
			}
			if (scaleMode == XMode) {
				currScaleX = (endScaleX - startScaleX) * process1 + startScaleX;
				currScaleY = (endScaleY - startScaleY) * process2 + startScaleY;
			} else {
				currScaleX = (endScaleX - startScaleX) * process2 + startScaleX;
				currScaleY = (endScaleY - startScaleY) * process1 + startScaleY;
			}
			notifyChange();
		}

		@Override
		protected void resetCalcu() {
			currScaleX = startScaleX;
			currScaleY = startScaleY;
			notifyChange();
		}

		@Override
		public void setValueS(int i, Object value) {
		}

		@Override
		public void setValueE(int i, Object value) {
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
		public MoveScaleTriggleInfo getTriggleInfo() {
			return (MoveScaleTriggleInfo)priGetTriggleInfo();
		}
		
		@Override
		public void updateValueS() {
		}
		
	}
	
	private float startScaleX;
	private float endScaleX;
	private float currScaleX;
	private float startScaleY;
	private float endScaleY;
	private float currScaleY;
	private Controler controler;
	private int scaleMode;
	private float extendScale;
	private float extendTime;
	private float extendStart;
	
	private static final long baseTime = 3000;
	public static final int XMode = 0;
	public static final int YMode = 1;
	
	private static final int defaultScaleMode = YMode;
	private static final float defaultExtScale = 0.8f;
	private static final float defaultExtTime = 0.5f;
	private static final float defaultExtStart = 0.1f;
	
	public GlControlMoveScale() {
		startScaleX = 0;
		endScaleX = 0;
		currScaleX = 0;
		startScaleY = 0;
		endScaleY = 0;
		currScaleY = 0;
		scaleMode = defaultScaleMode;
		extendScale = defaultExtScale;
		extendTime = defaultExtTime;
		extendStart = defaultExtStart;
		controler = new MoveScaleControler();
		controler.setSpeed(1000);
		controler.setProcessMode(Controler.processRevert);
	}
	
	public void setScaleMode(int scaleMode) {
		this.scaleMode = scaleMode;
	}
	
	public void setExtStart(float extStart) {
		if (extStart > 1f) {
			extStart = 1f;
		} else if (extStart < 0f) {
			extStart = 0f;
		}
		this.extendStart = extStart;
	}
	
	public void setExtTime(float extTime) {
		if (extTime > 1f) {
			extTime = 1f;
		} else if (extTime < 0f) {
			extTime = 0f;
		}
		this.extendTime = extTime;
	}
	
	public void setExtScale(float extScale) {
		if (extScale > 1f) {
			extScale = 1f;
		} else if (extScale < 0f) {
			extScale = 0f;
		}
		this.extendScale = extScale;
	}
	
	public void setStartAndEnd(float sX, float eX, float sY, float eY, int mode) {
		controler.setMode(mode);
		startScaleX = sX;
		startScaleY = sY;
		endScaleX = eX;
		endScaleY = eY;
		controler.triggleCalcuLength();
	}
	
	public void setSumTime(long sumTime) {
		controler.setSpeed((float)baseTime / (float)sumTime * 1000);
	}
	
	@Override
	public Controler getControler() {
		return controler;
	}

	@Override
	public float getScaleX() {
		return currScaleX;
	}

	public float getScaleY() {
		return currScaleY;
	}
}
