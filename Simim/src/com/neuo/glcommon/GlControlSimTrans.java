package com.neuo.glcommon;

import com.neuo.common.Controler;
import com.neuo.common.VectorUtil;
import com.neuo.common.Controler.TriggleEventHandle;

public class GlControlSimTrans extends GlControler.GlControlTrans 
implements TriggleEventHandle {
	public static class SimTransTriggleInfo extends Controler.TriggleInfo {
		public float eX;
		public float eZ;
		public float eY;
		
		public SimTransTriggleInfo() {
			clear();
		}
		
		public void clear() {
			super.clear();
			eX = 0;
			eY = 0;
			eZ = 0;
		}

		@Override
		public Object getValue(int i) {
			if (i == 0) {
				return (Float)eX;
			} else if (i == 1) {
				return (Float)eY;
			} else {
				return (Float)eZ;
			}
		}
	}
	
	public class SimTransControler extends Controler {
		public SimTransControler() {
			super(3);
		}
		@Override
		public TriggleInfo createInfo() {
			return new SimTransTriggleInfo();
		}

		@Override
		public float calcutLength() {
			VectorUtil.delVector(startPos, endPos, 3, currPos);
			return VectorUtil.module(currPos, 3);
		}

		@Override
		public void calcuValue() {
			float currLength = getCurrLength();
			float length = getLength();
			float process;
			if (length == 0f) {
				process = 0f;
			} else if (tranMode == constantMode) {
				process = currLength / length;
			} else if (tranMode == upMode) {
				process = (float)Math.pow(currLength / length, 2);
			} else {
				process = 1f - (float)Math.pow(1 - currLength / length, 2);
			}
			
			currPos[0] = (endPos[0] - startPos[0]) * process + startPos[0];
			currPos[1] = (endPos[1] - startPos[1]) * process + startPos[1];
			currPos[2] = (endPos[2] - startPos[2]) * process + startPos[2];
			notifyChange();
		}

		@Override
		public void setValueS(int i, Object value) {
			startPos[i] = (Float)value;
		}

		@Override
		public void setValueE(int i, Object value) {
			endPos[i] = (Float)value;
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
		public SimTransTriggleInfo getTriggleInfo() {
			return (SimTransTriggleInfo)priGetTriggleInfo();
		}
		@Override
		public void updateValueS() {
			for (int i = 0; i < 2; i ++) {
				startPos[i] = currPos[i];
			}
		}
	}
	
	private float[] startPos;
	private float[] endPos;
	private float[] currPos;
	private Controler controler;
	private int tranMode;
	
	public static final int constantMode = 0;
	public static final int upMode = 1;
	public static final int downMode = 2;
	
	private static final int defaultMode = constantMode;
	
	public GlControlSimTrans() {
		startPos = new float[3];
		endPos = new float[3];
		currPos = new float[3];
		tranMode = defaultMode;
		controler = new SimTransControler();
	}
	
	public void setTranMode(int tranMode) {
		this.tranMode = tranMode;
	}
	
	public void setStartAndEnd(float sx, float sy, float sz,
									float ex, float ey, float ez, int mode) {
		controler.setMode(mode);
		startPos[0] = sx;startPos[1] = sy;startPos[2] = sz;
		endPos[0] = ex;endPos[1] = ey;endPos[2] = ez;
		controler.triggleCalcuLength();
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
