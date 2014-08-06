package com.neuo.common;

public class Timer implements Controler.TriggleEventHandle {
	public static class TimeTriggleInfo extends Controler.TriggleInfo {
		public float endTime;
		
		public TimeTriggleInfo() {
			clear();
		}
		
		public void clear() {
			super.clear();
			endTime = 0;
		}

		@Override
		public Object getValue(int i) {
			return (Float)endTime;
		}
	}
	
	public class TimeControler extends Controler {
		public TimeControler() {
			super(1);
		}
		@Override
		public TriggleInfo createInfo() {
			return new TimeTriggleInfo();
		}

		@Override
		public float calcutLength() {
			return Math.abs(endTime - startTime);
		}

		@Override
		public void calcuValue() {
			float currLength = getCurrLength();
			float length = getLength();
			currTime = currLength / length * (endTime - startTime) + startTime;
		}

		@Override
		public void setValueS(int i, Object value) {
			startTime = (Float)value;
		}

		@Override
		public void setValueE(int i, Object value) {
			endTime = (Float)value;
		}

		@Override
		public void active(boolean isActive) {
		}

		@Override
		public TimeTriggleInfo getTriggleInfo() {
			return (TimeTriggleInfo)priGetTriggleInfo();
		}
		
		@Override
		public void updateValueS() {
			startTime = currTime;
		}
	}
	
	private float startTime;
	private float endTime;
	private float currTime;
	private Controler controler;
	
	public Timer() {
		startTime = 0;
		endTime = 0;
		currTime = 0;
		controler = new TimeControler();
		controler.setSpeed(1000);
		controler.setProcessMode(Controler.processRevert);
	}
	
	public void setStart(float s, int mode) {
		controler.setMode(mode);
		if (s < 0) {
			s = 0;
		}
		startTime = 0;
		endTime = s;
		controler.triggleCalcuLength();
	}

	@Override
	public Controler getControler() {
		return controler;
	}
	
	
}
