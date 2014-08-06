package com.neuo.glcommon;

import com.neuo.common.Controler;

public class GlControlSimRotate extends GlControler.GlControlRotate 
implements Controler.TriggleEventHandle {
	public static class SimRotateTriggleInfo extends Controler.TriggleInfo {
		public float rotate;
		
		public SimRotateTriggleInfo() {
			clear();
		}
		
		public void clear() {
			super.clear();
			rotate = 0;
		}

		@Override
		public Object getValue(int i) {
			return (Float)rotate;
		}
	}
	
	public class SimRotateControler extends Controler {
		public SimRotateControler() {
			super(1);
		}
		@Override
		public TriggleInfo createInfo() {
			return new SimRotateTriggleInfo();
		}

		@Override
		public float calcutLength() {
			return Math.abs(endAngle - startAngle);
		}

		@Override
		public void calcuValue() {
			float currLength = getCurrLength();
			float length = getLength();
			float process;
			if (length == 0f) {
				process = 0f;
			} else {
				process = currLength / length;
			}
			currAngle = (endAngle - startAngle) * process + startAngle;
			notifyChange();
		}

		@Override
		public void setValueS(int i, Object value) {
			startAngle = (Float)value;
		}

		@Override
		public void setValueE(int i, Object value) {
			endAngle = (Float)value;
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
		public SimRotateTriggleInfo getTriggleInfo() {
			return (SimRotateTriggleInfo)priGetTriggleInfo();
		}

		@Override
		public void updateValueS() {
			startAngle = currAngle;
		}
		
	}
	
	private float startAngle;
	private float endAngle;
	private float currAngle;
	private int axis;
	private Controler controler;
	
	public GlControlSimRotate() {
		controler = new SimRotateControler();
	}
	
	public void setStartAndEnd(float s, float e, int axis, int mode) {
		this.axis = axis;
		controler.setMode(mode);
		startAngle = s;
		startAngle = e;
		controler.triggleCalcuLength();
	}
	
	@Override
	public Controler getControler() {
		return controler;
	}
	
	@Override
	public int getAxis() {
		return axis;
	}

	@Override
	public float getAngle() {
		return currAngle;
	}
}
