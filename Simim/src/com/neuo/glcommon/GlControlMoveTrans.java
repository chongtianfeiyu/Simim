package com.neuo.glcommon;

import com.neuo.common.Controler;

public class GlControlMoveTrans extends GlControler.GlControlTrans
implements Controler.TriggleEventHandle {
	public static class MoveTransTriggleInfo extends Controler.TriggleInfo {
		public MoveTransTriggleInfo() {
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
	
	public class MoveTransControler extends Controler {
		public MoveTransControler() {
			super(1);
		}
		
		@Override
		public TriggleInfo createInfo() {
			return new MoveTransTriggleInfo();
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
			if (process == 0f) {
			} else if (process == 1f) {
			} else if (process <= tranProcess) {
				if (tranMode == upMode) {
					process = (float)Math.pow(process / tranProcess, 2);
				} else if (tranMode == downMode) {
					process = 1 - (float)Math.pow(1 - process / tranProcess, 2);
				} else {
					process = process / tranProcess;
				}
			} else if (process < tranProcess + overProcess) {
				process = 1f + (process - tranProcess) / overProcess * overMove ;
			} else {
				process = 1f + (1f - process) / overProcess * overMove;
			}
			currPos[0] = (endPos[0] - startPos[0]) * process + startPos[0];
			currPos[1] = (endPos[1] - startPos[1]) * process + startPos[1];
			currPos[2] = (endPos[2] - startPos[2]) * process + startPos[2];
			notifyChange();
		}
		
		@Override
		protected void resetCalcu() {
			currPos[0] = startPos[0];
			currPos[1] = startPos[1];
			currPos[2] = startPos[2];
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
		public MoveTransTriggleInfo getTriggleInfo() {
			return (MoveTransTriggleInfo)priGetTriggleInfo();
		}

		@Override
		public void updateValueS() {
		}

	}
	
	private float[] startPos;
	private float[] endPos;
	private float[] currPos;
	private Controler controler;
	private float tranProcess;
	private float overProcess;
	private float overMove;
	
	private static final float defaultTranProcess = 0.8f;
	private static final float defaultOverTime = 0.1f;
	private static final long baseTime = 3000;
	private int tranMode;
	
	public static final int constantMode = 0;
	public static final int upMode = 1;
	public static final int downMode = 2;
	
	private static final int defaultMode = upMode;
	
	public GlControlMoveTrans() {
		startPos = new float[] {0f, 0f, 0f};
		endPos = new float[] {0f, 0f, 0f};
		currPos = new float[] {0f, 0f, 0f};
		controler = new MoveTransControler();
		controler.setSpeed(1000);
		controler.setProcessMode(Controler.processRevert);
		tranMode = defaultMode;
		overMove = defaultOverTime;
		tranProcess = defaultTranProcess;
		overProcess = (1 - tranProcess) / 2f;
	}

	public void setTranMode(int tranMode) {
		this.tranMode = tranMode;
	}
	
	public void setOverMove(float overMove) {
		this.overMove = overMove;
	}
	
	public void setTransProcess(float process) {
		if (process < 0f) {
			process = 0f;
		} else if (process > 1f) {
			process = 1f;
		}
		this.tranProcess = process;
		this.overProcess = (1 - this.tranProcess) / 2f;
	}
	
	public void setSumTime(long sumTime) {
		controler.setSpeed((float)baseTime / (float)sumTime * 1000);
	}
	
	public void setStartAndEnd(float sx, float sy, float sz,
									float ex, float ey, float ez,
									int mode) {
		controler.setMode(mode);
		startPos[0] = sx;startPos[1] = sy;startPos[2] = sz;
		endPos[0] = ex;endPos[1] = ey;endPos[2] = ez;
		controler.triggleCalcuLength();
	}
	
	public long getBaseTime() {
		return baseTime;
	}

	public float[] getPos(boolean isEnd) {
		if (isEnd) {
			return endPos;
		} else {
			return startPos;
		}
	}
	
	@Override
	public float getX() {
		return currPos[0];
	}

	@Override
	public float getY() {
		return currPos[1];
	}

	@Override
	public float getZ() {
		return currPos[2];
	}

	@Override
	public Controler getControler() {
		return controler;
	}

}
