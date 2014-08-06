package com.neuo.common;

import com.neuo.common.UpdateManager.CommonCalcu;
public abstract class Controler 
implements CommonCalcu {

	public static abstract class OverEvent {
		public abstract void triggle();
	}
	public static abstract class ProcessEvent {
		public abstract void triggle(float process);
	}

	public static class TriggleInfoBak {
		public OverEvent overEvent;
		public boolean isSetProcess;
		public float process;
		public ProcessEvent processEvent;

		public void clear() {
			overEvent = null;
			processEvent = null;
			isSetProcess = false;
			process = 0f;
		}
		
		public TriggleInfoBak() {
			clear();
		}
		
		public void Bak(TriggleInfo t) {
			clear();
			this.overEvent = t.overEvent;
			this.isSetProcess = t.isSetProcess;
			this.process = t.process;
			this.processEvent = t.processEvent;
		}
	}
	
	public abstract static class TriggleInfo {
		public int event;
		public boolean canStop;
		public OverEvent overEvent;
		public ProcessEvent processEvent;
		public boolean isUseActive;
		public boolean isActive;
		public boolean isSetProcess;
		public float process;
		
		public TriggleInfo() {
			clear();
		}
		
		public TriggleInfoBak createInfoBak() {
			return new TriggleInfoBak();
		}
		
		public abstract Object getValue(int i);
		
		public void clear() {
			event = 0;
			canStop = true;
			overEvent = null;
			processEvent = null;
			isUseActive = false;
			isActive = false;
			isSetProcess = false;
			process = 0f;
		}
		
		public void setProcess(float process) {
			isSetProcess = true;
			this.process = process;
			if (this.process < 0f) {
				this.process = 0f;
			} else if (this.process > 1f) {
				this.process = 1f;
			}
		}
		
		public void setActive(boolean isActive) {
			isUseActive = true;
			this.isActive = isActive;
		}

	}
	public static final int Repeat 	= 1;
	public static final int Single 	= 2;
	public static final int Double 	= 3;
	public static final int Forever = 4;

	public static final int ToDes 	= 1;
	public static final int NotDes 	= 2;
	public static final int Revert 	= 3;
	public static final int Resume  = 4;
	public static final int Stop 	= 5;
	public static final int Move 	= 6;
	public static final int Again 	= 7;
	public static final int Reset 	= 8;
	public static final int Nothing = 9;
	
	public static final int processCommon = 0;
	public static final int processRevert = 1;
	
	private float length;
	private float currLength;
	private boolean isRun;
	private boolean isDes;

	private float speed;

	private TriggleInfo triggleInfo;
	private TriggleInfoBak triggleInfoBak;
	
	private boolean isTriggle;
	private int mode;
	private int processMode;
	private OverEvent overEvent;
	private ProcessEvent processEvent;
	
	private int count;
	
	private static final int defaultMode = Single;
	private static final int defaultProcessMode = processCommon;
	private static final int defaultSpeed = 10;
	public Controler(int count) {
		length = 0f;
		currLength = 0f;
		isRun = false;
		isDes = false;
		speed = defaultSpeed;
		isTriggle = false;
		mode = defaultMode;
		processMode = defaultProcessMode;
		overEvent = null;
		processEvent = null;
		
		this.count = count;
		this.triggleInfo = createInfo();
		triggleInfoBak = this.triggleInfo.createInfoBak();
	}
	
	public abstract TriggleInfo createInfo();
	public abstract float calcutLength();
	public abstract void calcuValue();
	public abstract void setValueS(int i, Object value);
	public abstract void setValueE(int i, Object value);
	public abstract void updateValueS();
	
	public void reset(boolean isClear) {
		if (isClear) {
			resetLength();
		}
		isTriggle = false;
		overEvent = null;
		processEvent = null;
		isRun = false;
		triggleInfo.clear();;
	}

	private void resetLength() {
		currLength = 0f;
		isDes = false;
		resetCalcu();
	}
	
	protected void resetCalcu() {
		calcuValue();
	}
	
	public boolean isOver() {
		return !isRun;
	}
	
	public void setMode(int mode) {
		reset(false);
		this.mode = mode;
	}
	
	public void setProcessMode(int processMode) {
		this.processMode = processMode;
	}
	
	public void triggleCalcuLength() {
		length = calcutLength();
		resetLength();
	}
	
	public float getLength() {
		return this.length;
	}
	
	public float getCurrLength() {
		return this.currLength;
	}
	
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	private void start() {
		isRun = true;
	}
	
	private void stop() {
		isRun = false;
		triggleProcess(true);
		if (null != overEvent) {
			overEvent.triggle();
			overEvent = null;
		}
	}
	
	public boolean isDes() {
		return this.isDes;
	}
	
	public void process(long in) {
		calcuProcess(in);
		calcuTriggle();
	}
	
	@Override
	public void calcu(long interval) {
		process(interval);
	}

	public abstract void active(boolean isActive);
	
	public void calcuTriggle() {
		if (isTriggle) {
			if (!isRun && !triggleInfo.canStop) {
				isTriggle = false;
				return;
			}
			if (triggleInfo.isUseActive) {
				active(triggleInfo.isActive);
			}
			triggleInfoBak.Bak(triggleInfo);
			this.overEvent = null;
			this.processEvent = null;
			switch (triggleInfo.event) {
			case Reset:
				reset(true);
				break;
			case Nothing:
				break;
			case Again:
				reset(true);
				start();
				break;
			case Resume:
				start();
				break;
			case Stop:
				stop();
				break;
			case ToDes:
				if (!isRun && !isDes) {
					start();
				} else if (isRun && isDes) {
					isDes = false;
				}
				break;
			case NotDes:
				if (!isRun && isDes) {
					start();
				} else if (isRun && !isDes) {
					isDes = true;
				}
				break;
			case Revert:
				if (!isRun) {
					start();
				} else {
					isDes = !isDes;
				}
				break;
			case Move:
				updateValueS();
				for (int i = 0; i < count; i++) {
					setValueE(i, triggleInfo.getValue(i));
				}
				triggleCalcuLength();
				if (!isRun) {
					start();
				}
				break;
			}
			this.processEvent = triggleInfoBak.processEvent;
			this.overEvent = triggleInfoBak.overEvent;
			if (triggleInfoBak.isSetProcess) {
				setProcess(triggleInfoBak.process);
			}
			isTriggle = false;
		}
	}
	
	public void setProcess(float process) {
		if (processMode == processCommon) {
			if (!isDes) {
				currLength = length * process;
			} else {
				currLength = length * (1 - process);
			}
		} else {
			currLength = length * process;
		}
		calcuValue();
	}
	
	public void calcuProcess(long in) {
		if (isRun) {
			float interval = speed * in / 1000;
			triggleProcess(false);
			switch (mode) {
			case Forever:
				if (isDes) {
					currLength -= interval;
				} else {
					currLength += interval;
				}
				break;
			case Repeat:
				if (isDes) {
					currLength -= interval;
					if (currLength <= 0f) {
						currLength = 0f;
						isDes = false;
					}
				} else {
					currLength += interval;
					if (currLength >= length) {
						currLength = length;
						isDes = true;
					}
				}
				break;
			case Double:
				if (isDes) {
					currLength -= interval;
					if (currLength <= 0f) {
						currLength = 0f;
						stop();
						isDes = false;
					}
				} else {
					currLength += interval;
					if (currLength >= length) {
						currLength = length;
						isDes = true;
					}
				}
				break;
			case Single:
				if (isDes) {
					currLength -= interval;
					if (currLength <= 0f) {
						currLength = 0f;
						stop();
						isDes = false;
					}
				} else {
					currLength += interval;
					if (currLength >= length) {
						currLength = length;
						stop();
						isDes = true;
					}
				}
				break;
			}
			calcuValue();
		}
	}
	
	private void triggleProcess(boolean isRemove) {
		float process = 0f;
		if (length == 0f) {
			process = 1f;
		} else if (processMode == processRevert || !isDes) {
			process =  currLength / length;
		} else if (isDes) {
			process = (length - currLength) / length;
		}
		processAction(process, isRemove);
	}
	
	protected void processAction(float process, boolean isRemove) {
		if (processEvent != null) {
			processEvent.triggle(process);
			if (isRemove) {
				processEvent = null;
			}
		}
	}
	
	public boolean isCanTriggle() {
		return !isTriggle;
	}
	
	public void triggle(boolean atOnce) {
		isTriggle = true;
		if (atOnce) {
			calcuTriggle();
		}
	}
	
	public abstract TriggleInfo getTriggleInfo();
	protected TriggleInfo priGetTriggleInfo() {
		return triggleInfo;
	}
	
	public static interface TriggleEventHandle {
		public Controler getControler();
	}
}
