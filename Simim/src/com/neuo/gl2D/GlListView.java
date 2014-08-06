package com.neuo.gl2D;

import com.neuo.common.CommonEvent;
import com.neuo.common.Controler;
import com.neuo.common.CommonEvent.Event;
import com.neuo.common.UpdateManager.CommonCalcu;
import com.neuo.glcommon.GlControlMoveTrans;
import com.neuo.glcommon.GlView;
import com.neuo.globject.GlObject;
import com.neuo.glshader.GlShader;
import com.neuo.glshader.GlShaderManager;
import com.neuo.glshader.ShaderByDirect;

public class GlListView extends GlEventView {
	public static final int vertical = 0;
	public static final int horizontal = 1;
	
	protected int listMode;
	protected float cursor;
	
	protected int lineCount;
	protected int lineLength;

	protected static final float defaultLineInterval = 0.01f;
	protected float lineInterval = defaultLineInterval;
	
	protected static final float defaultLineExtend = 0.1f;
	protected float lineExtend = defaultLineExtend;
	protected static final float defaultFlingExtend = 0.1f;
	protected float flingExtend = defaultFlingExtend;
	
	protected static final float defaultScrollRatio = 1f;
	protected static final float defaultFlingRatio = 0.2f;
	protected static final float defaultSpeedRatio = 15f;
	protected float scrollRatio = defaultScrollRatio;
	protected float flingRatio = defaultFlingRatio;
	protected float speedRatio = defaultSpeedRatio;
	
	protected GlControlMoveTrans cursorControler;
	protected class CursorCalcuWrapper 
	implements CommonCalcu {
		@Override
		public void calcu(long interval) {
			if (!cursorControler.getControler().isOver()) {
				cursorControler.getControler().calcu(interval);
				float lastCursor = cursor;
				cursor = cursorControler.getX();
				if (lastCursor != cursor) {
					notifyChildrenChange(GlContainer2D.UpdateXYZ);
				}
			}
		}
	}
	
	protected CursorCalcuWrapper calcuWrapper;
	public GlListView(String name, GlView glView, GlShader glShader,
			GlObject glObject, int listMode) {
		super(name, glView, glShader, glObject);
		initSelf(listMode);
	}

	public GlListView(String name, GlView glView, int listMode) {
		super(name, glView,
					GlShaderManager.getShaderManager().getShader(ShaderByDirect.myShaderTag),
					new GlObject());
		initSelf(listMode);
	}
	
	protected void initSelf(int listMode) {
		 this.listMode = listMode;
		 initAttr(false);
		 this.cursorControler = new GlControlMoveTrans();
		 this.calcuWrapper = new CursorCalcuWrapper();
		 this.setMoveListener(new MoveListener() {
			
			@Override
			public void onMove(GlEventView view, Event e, boolean isScroll) {
				scroll(e.x, e.y, !isScroll);
			}
		});
	}
	
	@Override
	protected void init() {
		super.init();
		setDrawNothing(true);
		addModel(GlContainer2D.StencilEnable);
		this.interceptAction = CommonEvent.All;
		
		cursor = 0f;
		lineCount = 0;
		lineLength = 0;
	}
	
	public CursorCalcuWrapper getCalcuWrapper() {
		return this.calcuWrapper;
	}
	
	public void setFlingExtend(float flingExtend) {
		this.flingExtend = flingExtend;
	}
	
	public float getFlingExtend() {
		return this.flingExtend;
	}
	
	public void setScrollRatio(float scrollRatio) {
		this.scrollRatio = scrollRatio;
	}
	
	public float getScrollRatio() {
		return this.scrollRatio;
	}
	
	public void setFlingRatio(float flingRatio) {
		this.flingRatio = flingRatio;
	}
	
	public float getFlingRatio() {
		return this.flingRatio;
	}
	
	public void setSpeedRatio(float speedRatio) {
		this.speedRatio = speedRatio;
	}
	
	public float getSpeedRatio() {
		return this.speedRatio;
	}
	
	public void setLineExtend(float extend) {
		this.lineExtend = extend;
	}
	
	public float getLineExtend() {
		return this.lineExtend;
	}
	
	public void setLineInterval(float interval) {
		this.lineInterval = interval;
	}
	
	public float getLineInterval() {
		return this.lineInterval;
	}
	
	@Override
	public boolean setWH(float width, float height) {
		if (super.setWH(width, height)) {
			clearChildren();
			return true;
		} else {
			return false;
		}
	}
	
	protected float getLineIntLenght(boolean lineDir) {
		if (lineDir) {
			return lineInterval * (this.listMode == vertical ? this.height : this.width);
		} else {
			return lineInterval * (this.listMode == vertical ? this.height : this.width) / 2f;
		}
	}
	
	public float getUnitLength(boolean lineDir, float count) {
		if (lineDir) {
			if (this.listMode == vertical) {
				return this.height / count - lineInterval * this.height;
			} else {
				return this.width / count - lineInterval * this.width;
			}
		} else {
			if (this.listMode == vertical) {
				return this.width - lineInterval * this.height;
			} else {
				return this.height - lineInterval * this.width;
			}
		}
	}

	public int getMode() {
		return this.listMode;
	}

	@Override
	public void clearChildren() {
		super.clearChildren();
		cursor = 0f;
		lineCount = 0;
		lineLength = 0;
	}

	public void addChild(GlContainer2D child) {
		addChild(child, -1);
	}

	@Override
	public void addChild(GlContainer2D child, int sortValue) {
		delChild(child);
		child.setParent(this);
		if (sortValue < 0 || sortValue >= lineCount) {
			child.setSortValue(lineCount);
			float changeLength = getLineIntLenght(true);
			if (this.listMode == vertical) {
				child.setPosMode(GlContainer2D.PostionXMid, GlContainer2D.PostionYTop);
				child.setXY(0, - changeLength - child.getHeight(true) / 2f - lineLength);
				changeLength += child.getHeight(true);
			} else {
				child.setPosMode(GlContainer2D.PostionXLeft, GlContainer2D.PostionYMid);
				child.setXY(changeLength + child.getWidth(true) / 2 + lineLength, 0);
				changeLength += child.getWidth(true);
			}
			lineCount ++;
			lineLength += changeLength;
		} else {
			float changeLength = getLineIntLenght(true);
			updateChildrenValue(sortValue, 1, changeLength + (this.listMode == vertical ? child.getHeight(true) : child.getWidth(true)));
			
			child.setSortValue(sortValue);
			float preLength = calcuLength(sortValue);
			if (this.listMode == vertical) {
				child.setPosMode(GlContainer2D.PostionXMid, GlContainer2D.PostionYTop);
				child.setXY(0, - changeLength - child.getHeight(true) / 2f - preLength);
			} else {
				child.setPosMode(GlContainer2D.PostionXLeft, GlContainer2D.PostionYMid);
				child.setXY(changeLength + child.getWidth(true) / 2 + preLength, 0);
			}
		}
		synchronized (viewsTree) {
			viewsTree.insert(child);
		}
		child.onAdd(this);
	}
	
	protected float calcuLength(int sortValue) {
		float res = 0;
		for (dealIterator.clear(); dealIterator.hasNext();) {
			GlContainer2D tmp = dealIterator.next();
			if (tmp.getSortValue() < sortValue) {
				res += getLineIntLenght(true);
				res += this.listMode == vertical ? tmp.getHeight(true) : tmp.getWidth(true);
			} else {
				break;
			}
		}
		return res;
	}
	
	@Override
	public GlContainer2D delChild(String name) {
		return null;
	}
	
	public GlContainer2D delChild(int sortValue) {
		GlContainer2D res = null;
		for (dealIterator.clear(); dealIterator.hasNext();) {
			GlContainer2D tmp = dealIterator.next();
			if (tmp.getSortValue() == sortValue) {
				res = tmp;
				synchronized (viewsTree) {
					dealIterator.remove();
				}
				break;
			}
		}
		if (res != null) {
			res.removeParent();
			res.onDel(this);
			updateChildrenValue(res.getSortValue(), -1, -getLineIntLenght(true) - (this.listMode == vertical ?
					res.getHeight(true) : res.getWidth(true)));
		}
		return res;
	}
	
	@Override
	public GlContainer2D delChild(GlContainer2D child) {
		GlContainer2D res = super.delChild(child);
		if (res != null) {
			updateChildrenValue(res.getSortValue(), -1, -getLineIntLenght(true) - (this.listMode == vertical ?
					res.getHeight(true) : res.getWidth(true)));
		}
		return res;
	}
	
	protected void updateChildrenValue(int sortValue, int changeValue, float changeLength) {
		for (dealIterator.clear(); dealIterator.hasNext();) {
			GlContainer2D tmp = dealIterator.next();
			if (tmp.getSortValue() >= sortValue) {
				tmp.setSortValue(tmp.getSortValue() + changeValue);
				if (this.listMode == vertical) {
					tmp.setXY(tmp.getX(true), tmp.getY(true) - changeLength);
				} else {
					tmp.setXY(tmp.getX(true) + changeLength, tmp.getY(true));
				}
			}
		}
		lineCount += changeValue;
		lineLength += changeLength;
	}
	
	@Override
	public void resetTouchState() {
		super.resetTouchState();
		//stopMove();
	}
	
	@Override
	protected void onParentEvent(Event e) {
		this.onHanderEvent(e);
	}
	
	protected void scroll(float ex, float ey, boolean isFling) {
		if (!isDown) {
			return;
		}
		if (isFling) {
			float extend = 0f;
			float def = 0f;
			float move = 0f;
			float goal = 0f;
			float overMove = 0f;
			boolean isRevert = false;
			boolean isDouble = false;
			if (this.listMode == vertical) {
				extend = this.flingExtend * this.height;
				def = this.lineLength - this.height;
				move = ey * flingRatio;
			} else {
				extend = this.flingExtend * this.width;
				def = this.lineLength - this.width;
				move = -ex * flingRatio;
			}
			if (def < 0) {
				def = 0;
			}
			goal = cursor + move;
			if (goal < -extend) {
				goal = -extend;
			} else if (goal > def + extend) {
				goal = def + extend;
			}
			
			if (cursor < 0f) {
				if (goal > def) {
					overMove = (goal - def) / (-cursor + def);
					goal = def;
				} else if (goal > 0f || cursor < goal) {
					if (goal < 0f) {
						goal = 0f;
					}
					overMove = 0f;
				} else {
					isRevert = true;
					overMove = (goal - cursor) / cursor;
					goal = 0f;
				}
			} else if (cursor > def) {
				if (goal < 0f) {
					overMove = -goal / cursor;
					goal = 0f;
				} else if (goal < def || cursor > goal) {
					if (goal > def) {
						goal = def;
					}
					overMove = 0f;
				} else {
					isRevert = true;
					overMove = (goal - cursor) / (cursor - def);
					goal = def;
				}
			} else {
				if (goal < 0f) {
					if (cursor != 0f) {
						overMove = -goal / cursor;
						goal = 0f;
					} else {
						overMove = 0f;
						isDouble = true;
					}
				} else if (goal > def) {
					if (def != cursor) {
						overMove = (goal - def) / (def - cursor);
						goal = def;
					} else {
						overMove = 0f;
						isDouble = true;
					}
				} else {
					overMove = 0f;
				}
			}
			cursorControler.setOverMove(overMove);
			cursorControler.setTransProcess(0.5f / (overMove * 2 + 1));
			cursorControler.getControler().setSpeed(speedRatio * (this.listMode == vertical ? this.height : this.width));
			cursorControler.getControler().getTriggleInfo().clear();
			if (isRevert) {
				cursorControler.setTranMode(GlControlMoveTrans.upMode);
				cursorControler.setStartAndEnd(goal, 0, 0, cursor, 0, 0, Controler.Double);
				cursorControler.getControler().getTriggleInfo().setProcess(1f);
			} else if (isDouble) {
				cursorControler.setTranMode(GlControlMoveTrans.downMode);
				cursorControler.setStartAndEnd(cursor, 0, 0, goal, 0, 0, Controler.Double);
			} else {
				cursorControler.setTranMode(GlControlMoveTrans.downMode);
				cursorControler.setStartAndEnd(cursor, 0, 0, goal, 0, 0, Controler.Single);
			}
			cursorControler.getControler().getTriggleInfo().event = Controler.Resume;
			cursorControler.getControler().triggle(true);
		} else {
			float extend = 0;
			float def = 0;
			float move = 0;
			if (this.listMode == vertical) {
				extend = this.lineExtend * this.height;
				def = this.lineLength - this.height;
				move = ey * scrollRatio;
			} else {
				extend = this.lineExtend * this.width;
				def = this.lineLength - this.width;
				move = -ex * scrollRatio;
			}
			if (def < 0) {
				def = 0;
			}
			def += extend;
			float lastCursor = this.cursor;
			
			cursor += move;
			if (cursor < -extend && lastCursor >= -extend) {
				cursor = -extend;
			} else if (cursor > def && lastCursor <= def) {
				cursor = def;
			}

			if (lastCursor != this.cursor) {
				notifyChildrenChange(GlContainer2D.UpdateXYZ);
			}
		}
	}
	
	protected void stopMove() {
		if (!cursorControler.getControler().isOver()) {
			cursorControler.getControler().reset(false);
		}
	}
	
	public void triggleKickBack() {
		if (!cursorControler.getControler().isOver()) {
			cursorControler.getControler().reset(false);
		}
		isDown = false;
		kickBack();
	}
	
	protected void kickBack() {
		if (!cursorControler.getControler().isOver())return;
		float def = 0;
		if (this.listMode == vertical) {
			def = this.lineLength - this.height;
		} else {
			def = this.lineLength - this.width;
		}
		if (def < 0) {
			def = 0;
		}
		boolean isTriggle = false;
		float goal = 0;
		if (cursor < 0) {
			isTriggle = true;
			goal = 0;
		} else if (cursor > def) {
			isTriggle = true;
			goal = def;
		}
		if (isTriggle) {
			cursorControler.setOverMove(0f);
			cursorControler.setTransProcess(1f);
			cursorControler.setTranMode(GlControlMoveTrans.downMode);
			cursorControler.setStartAndEnd(cursor, 0, 0, goal, 0, 0, Controler.Single);
			cursorControler.getControler().setSpeed(speedRatio * (this.listMode == vertical ? this.height : this.width));
			cursorControler.getControler().getTriggleInfo().clear();
			cursorControler.getControler().getTriggleInfo().event = Controler.Resume;
			cursorControler.getControler().triggle(true);
		}
	}
	
	public float getCursor() {
		return this.cursor;
	}
	
	public float getSumLength() {
		return this.lineLength;
	}
	
	public float getCursorRatio() {
		return this.cursor / this.lineLength;
	}
	
	@Override
	protected void onDownState() {
		super.onDownState();
		stopMove();
	}
	
	@Override
	protected void onUnDownState() {
		super.onUnDownState();
		kickBack();
	}
	
	@Override
	protected void setDrawSelf() {
		//do nothing
	}

	@Override
	public float getX(boolean isPure) {
		if (isPure) {
			return centerX;
		} else if (this.listMode == vertical) {
			return currCenterX;
		} else {
			return -cursor * currScaleX + currCenterX;
		}
	}
	
	@Override
	public float getY(boolean isPure) {
		if (isPure) {
			return centerY;
		} else if (this.listMode == horizontal) {
			return currCenterY;
		} else {
			return currCenterY + cursor * currScaleY;
		}
	}
}
