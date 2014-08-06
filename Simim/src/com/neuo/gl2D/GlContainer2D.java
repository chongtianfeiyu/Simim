package com.neuo.gl2D;

import java.util.ArrayList;
import java.util.Iterator;

import android.opengl.GLES20;

import com.neuo.common.CommonEvent;
import com.neuo.common.MathUtil;
import com.neuo.common.VectorUtil;
import com.neuo.common.CommonEvent.Event;
import com.neuo.common.UpdateManager.CommonUpdate;
import com.neuo.common.EventHander;
import com.neuo.common.EventManager.SortEventHander;
import com.neuo.glcommon.GlCamera;
import com.neuo.glcommon.GlColorMaterialAndTex;
import com.neuo.glcommon.GlConstant;
import com.neuo.glcommon.GlControler;
import com.neuo.glcommon.GlObjManager;
import com.neuo.glcommon.GlPosAttribute;
import com.neuo.glcommon.GlPosition;
import com.neuo.glcommon.GlRotateState;
import com.neuo.glcommon.GlView;
import com.neuo.glcommon.GlControler.GlControlAlpha;
import com.neuo.glcommon.GlControler.GlControlHandle;
import com.neuo.glcommon.GlControler.GlControlRotate;
import com.neuo.glcommon.GlControler.GlControlScale;
import com.neuo.glcommon.GlControler.GlControlTrans;
import com.neuo.glcommon.GlDrawManage.CommonDraw;
import com.neuo.globject.GlObject;
import com.neuo.globject.GlObjectByLight;
import com.neuo.globject.GlObject.GlObjectInterface;
import com.neuo.glshader.GlShader;
import com.neuo.glshader.GlShaderManager;
import com.neuo.glshader.GlShaderUtil;
import com.neuo.glshader.ShaderByDirect;
import com.neuo.struct.BinarySortTree;

public abstract class GlContainer2D
implements EventHander, GlControlHandle, CommonUpdate, Comparable<Object>, GlObjectInterface {
	public static final int PositionScaleMove = 1 << 0;
	public static final int SizeScaleMove = 1 << 1;
	public static final int StencilEnable = 1 << 2;
	public static final int AlphaChange = 1 << 3;
	public static final int DrawByParent = 1 << 5;
	public static final int TouchByParent = 1 << 6;
	public static final int UpdateByParent = 1 << 7;
	
	public static final int PostionXLeft = 0;
	public static final int PostionXMid = 1;
	public static final int PostionXRight = 2;
	
	public static final int PostionYTop = 0;
	public static final int PostionYMid = 1;
	public static final int PostionYBottom = 2;

	protected String name;
	protected static final GlPosAttribute stencilPosAttr = GlObjManager.getGlObjManager().getGlObjAttr(GlConstant.rectRevertRosAttrName,
																							GlObjManager.Rectangle);

	protected final GlShader stencilShader = GlShaderManager.getShaderManager().getShader(ShaderByDirect.myShaderTag);
	protected static final GlObject stencilObject = new GlObject();
	static {
		stencilObject.setMaterialColor(new GlColorMaterialAndTex());
		stencilObject.setPosAttr(stencilPosAttr);
	}
	{
		stencilObject.setGlShader(stencilShader);
	}

	protected int model;
	protected GlContainer2D parent;
	protected BinarySortTree<GlContainer2D> viewsTree;
	protected BinarySortTree<GlContainer2D>.TreeIterator drawIterator;
	protected BinarySortTree<GlContainer2D>.TreeIterator touchIterator;
	protected BinarySortTree<GlContainer2D>.TreeIterator dealIterator;
	
	protected float centerX, centerY;
	protected int XModel, YModel;
	protected float width, height;
	protected float z;
	protected int axis;
	protected float angle;
	protected float alpha;
	protected int keyEvent;
	protected float scaleX, scaleY;
	
	protected float currCenterX, currCenterY;
	protected float currWidth, currHeight;
	protected float currZ;
	protected int currAxis;
	protected float currAngle;
	protected float currAlpha;
	protected float currScaleX, currScaleY;
	protected boolean updatePos;
	
	protected boolean isVisible;
	protected boolean canDealEvent;
	protected boolean hasPress;
	protected boolean drawNothing;
	
	protected int activeState;
	protected GlControlTrans controlTrans;
	protected GlControlRotate controlRotate;
	protected GlControlScale controlScale;
	protected GlControlAlpha controlAlpha;
	public static final int UpdateXYZ = 1 << 0;
	public static final int UpdateWH = 1 << 1;
	public static final int UpdateRotate = 1 << 2;
	public static final int UpdateAlpha = 1 << 3;
	protected int updateState;
	protected GlView glView;
	protected int interceptAction;
	protected GlObject glObject;
	protected GlPosition glPosition;
	protected float drawZ;
	protected float[] vertexPos;
	
	public void setKeyEvent(int keyEvent) {
		this.keyEvent = keyEvent & CommonEvent.KeyEvent;
	}
	
	protected boolean compareTouchByZ;
	protected void setCompareTouchByZ(boolean isByZ) {
		this.compareTouchByZ = isByZ;
	}

	public GlContainer2DToucher getToucher() {
		return toucher;
	}
	
	public static final int MaxSortValue = -1;
	public static final int MinSortValue = -2;
	protected int sortValue;
	
	@Override
	public int compareTo(Object another) {
		if (this.sortValue == MaxSortValue) {
			return 1;
		} else if (this.sortValue == MinSortValue) {
			return -1;
		} else {
			GlContainer2D tmp = (GlContainer2D)another;
			return this.sortValue - tmp.sortValue;
		}
	}

	public GlDrawer getGlDrawer() {
		return this.glDrawer;
	}
	protected GlDrawer glDrawer = new GlDrawer();
	public class GlDrawer 
	implements CommonDraw {

		@Override
		public float getSorValue() {
			return -GlContainer2D.this.drawZ;
		}

		@Override
		public void draw() {
			GlContainer2D.this.draw();
		}

		@Override
		public void onRemove(boolean isWait) {
			//do nothing
		}

		@Override
		public void onAdd() {
			//do nothing
		}

		@Override
		public void calcuMVPMatrix(GlCamera glCamera) {
			GlContainer2D.this.calcuMVPMatrix(glCamera);
		}
		
	}

	public String getName() {
		return this.name;
	}
	
	protected GlContainer2DToucher toucher = new GlContainer2DToucher();
	public class GlContainer2DToucher 
	implements SortEventHander {
		private float value = 0;
		@Override
		public EventHander isHanderEvent(Event e) {
			return GlContainer2D.this.isHanderEvent(e);
		}
		
		public String getName() {
			return GlContainer2D.this.getName();
		}

		@Override
		public int onHanderEvent(Event e) {
			return GlContainer2D.this.onHanderEvent(e);
		}

		@Override
		public void setSortValue(float value) {
			this.value = value;
		}
		
		@Override
		public float getSortValue() {
			if (compareTouchByZ) {
				return -GlContainer2D.this.drawZ;
			} else {
				return value;
			}

		}

		@Override
		public void resetTouchState() {
			GlContainer2DToucher.this.resetTouchState();
		}
		
	}
	
	@Override
	public void update() {
		updateValue();
	}
	
	public void setUpdateChange(int update) {
		updateState |= update;
	}
	
	protected void setName(String name) {
		this.name = name;
	}
	
	public GlContainer2D(String name, GlView glView,
							GlShader glShader, GlObject glObject) {
		this.name = name;
		this.glView = glView;
		init();
		initGl(glObject, glShader);
	}
	
	protected void changeModel(int model) {
		if ((model & AlphaChange) != 0) {
			setUpdateChange(UpdateAlpha);
		}
		if ((model & SizeScaleMove) != 0) {
			setUpdateChange(UpdateWH);
		}
		if ((model & PositionScaleMove) != 0) {
			setUpdateChange(UpdateXYZ);
		}
	}
	
	public void clearModel() {
		if (this.model != 0) {
			int lastModel = this.model;
			this.model = 0;
			changeModel(lastModel);
		}
	}
	
	public void setModel(int model) {
		if (this.model != model) {
			int lastMode = this.model;
			this.model = model;
			changeModel((model | lastMode) & ~(model & lastMode));
		}
	}
	
	public boolean hasModel(int model) {
		return (this.model & model) == model;
	}
	
	public void addModel(int model) {
		if ((~this.model & model) != 0) {
			int lastModel = this.model;
			this.model |= model;
			changeModel(~lastModel & model);
		}
	}
	
	public void delModel(int model) {
		if ((this.model & model) != 0) {
			int lastModel = this.model;
			this.model &= ~model;
			changeModel(lastModel & model);
		}
	}
	
	protected static void updateContain(GlContainer2D container) {
		container.setUpdateChange(UpdateAlpha | UpdateRotate | UpdateWH | UpdateXYZ); 
		container.updateValue();
	}
	
	protected void setParent(GlContainer2D parent) {
		if (this.parent != parent) {
			this.parent = parent;
			updateContain(this);
		}
	}
	
	protected void removeParent() {
		if (this.parent != null) {
			//this.parentHanderEvent(new Event(CommonEvent.Cancle));
			this.parent = null;
			updateContain(this);
		}
	}
	
	protected void onParentEvent(Event e) {
	}
	
	protected void parentHanderEvent(Event e) {
		if (this.parent != null && (this.interceptAction & e.action) == 0) {
			this.parent.onParentEvent(e);
			this.parent.parentHanderEvent(e);
		}
	}
	
	public void setSortValue(int sortValue) {
		this.sortValue = sortValue;
	}
	
	public int getSortValue() {
		return this.sortValue;
	}
	
	protected ArrayList<GlContainer2D> tmpArrayList = new ArrayList<GlContainer2D>();
	public void clearChildren() {
		tmpArrayList.clear();
		synchronized (viewsTree) {
			for (dealIterator.clear(); dealIterator.hasNext();) {
				GlContainer2D tmp = dealIterator.next();
				dealIterator.remove();
				tmpArrayList.add(tmp);
			}
		}
		for (Iterator<GlContainer2D> iterator = tmpArrayList.iterator(); iterator.hasNext();) {
			GlContainer2D tmp = iterator.next();
			tmp.removeParent();
			tmp.onDel(this);
		}
		tmpArrayList.clear();
	}

	public void addChild(GlContainer2D child, int sortValue) {
		delChild(child);
		child.setParent(this);
		child.setSortValue(sortValue);
		synchronized (viewsTree) {
			viewsTree.insert(child);
		}
		child.onAdd(this);
	}
	
	public GlContainer2D delChild(String name) {
		GlContainer2D res = null;
		synchronized (viewsTree) {
			for (dealIterator.clear(); dealIterator.hasNext();) {
				GlContainer2D tmp = dealIterator.next();
				if (tmp.name.equals(name)) {
					dealIterator.remove();
					res = tmp;
					//res.removeParent();
					break;
				}
			}
		}
		if (res != null) {
			res.removeParent();
			res.onDel(this);
		}
		return res;
	}
	
	public GlContainer2D delChild(GlContainer2D child) {
		GlContainer2D res = null;
		synchronized (viewsTree) {
			res = viewsTree.remove(child);
		}
		if (res != null) {
			res.removeParent();
			res.onDel(this);
		}
			
		return res;
	}
	
	public GlContainer2D getChild(String name) {
		for (dealIterator.clear(); dealIterator.hasNext();) {
			GlContainer2D tmp = dealIterator.next();
			if (tmp.name.equals(name)) {
				return tmp;
			}
		}
		return null;
	}
	
	public void draw() {
		draw(0);
	}

	protected void draw(int stencilDepth) {
		if (isVisible) {
			
			//calcuCache();
			boolean isSelfStencil = (this.model & StencilEnable) != 0;
			if (isSelfStencil) {
				stencilDepth ++;
				GLES20.glEnable(GLES20.GL_STENCIL_TEST);
				drawStencil(false);
				GLES20.glStencilFunc(GLES20.GL_EQUAL, stencilDepth, 0xff);
				GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_KEEP, GLES20.GL_KEEP);
			}
			if (!drawNothing) {
				drawSelf();
			}
			synchronized (viewsTree) {
				for (drawIterator.clear(); drawIterator.hasNext();) {
					GlContainer2D tmp = drawIterator.next();
					if ((tmp.model & DrawByParent) != 0) {
						tmp.draw(stencilDepth);
					}
				}
			}
			if (isSelfStencil) {
				GLES20.glEnable(GLES20.GL_STENCIL_TEST);
				stencilDepth --;
				drawStencil(true);
				if (stencilDepth == 0) {
					GLES20.glDisable(GLES20.GL_STENCIL_TEST);
				}
			}
		}
	}
	
	protected void drawStencil(boolean isHide) {
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);
		GLES20.glDepthMask(false);
		GLES20.glColorMask(false, false, false, false);
		
		GLES20.glStencilFunc(GLES20.GL_ALWAYS, 1, 0xff);
		if (!isHide) {
			GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_KEEP, GLES20.GL_INCR);
		} else {
			GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_KEEP, GLES20.GL_DECR);
		}
		
		stencilObject.setMVPMatrix(this.glObject.getMVPMatrix());
		stencilShader.initShader(glView.getReinitId(), glView.getContext());
		stencilObject.draw();
		
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glDepthMask(true);
		GLES20.glColorMask(true, true, true, true);
	}

	protected abstract void setDrawSelf();
	protected void drawSelf() {
		setDrawSelf();
		this.glObject.getMaterialColor().setAlpha(getAlpha(false), 1f);
		this.glObject.getGlShader().initShader(glView.getReinitId(), glView.getContext());
		this.glObject.draw();
	}
	
	protected void calcuAlpha() {
		if ((UpdateAlpha & updateState) != 0) {
			updateState &= ~UpdateAlpha;
			float tmpAlpha = 0;
			if ((GlControler.alphaState & activeState) == 0) {
				tmpAlpha = this.alpha;
			} else {
				tmpAlpha = this.alpha * controlAlpha.getAlpha();
			}
			if (parent != null && ((this.model & AlphaChange) != 0)) {
				tmpAlpha *= parent.getAlpha(false);
			}
			this.currAlpha = tmpAlpha;
			notifyChildrenChange(UpdateAlpha);
		}
	}
	
	protected void calcuXYZ() {
		if ((UpdateXYZ & updateState) != 0) {
			updateState &= ~UpdateXYZ;
			float tmpX = 0f, tmpY = 0f, tmpZ = 0f;
			if ((activeState & GlControler.transState) == 0) {
				tmpX = centerX;
				tmpY = centerY;
				tmpZ = z;
			} else {
				tmpX = controlTrans.getX() + centerX;
				tmpY = controlTrans.getY() + centerY;
				tmpZ = controlTrans.getZ() + z;
			}
			if (parent != null) {
				if ((PositionScaleMove & model) != 0) {
					tmpX *= parent.getScaleX(false);
					tmpY *= parent.getScaleY(false);
				}
				switch (XModel) {
				case PostionXLeft:
					tmpX += parent.getX(false) - parent.getWidth(false) / 2f;
					break;
				case PostionXRight:
					tmpX += parent.getX(false) + parent.getWidth(false) / 2f;
					break;
				case PostionXMid:
					tmpX += parent.getX(false);
					break;
				default:
					throw new RuntimeException("xmodel is error " + XModel);
				}
				switch (YModel) {
				case PostionYTop:
					tmpY += parent.getY(false) + parent.getHeight(false) / 2f;
					break;
				case PostionYBottom:
					tmpY += parent.getY(false) - parent.getHeight(false) / 2f;
					break;
				case PostionYMid:
					tmpY += parent.getY(false);
					break;
				default:
					throw new RuntimeException("ymodel is error " + YModel);
				}
				tmpZ += parent.getZ(false);
			}
			boolean isChange = false;
			if (tmpX != currCenterX) {
				currCenterX = tmpX;
				isChange = true;
			}
			if (tmpY != currCenterY) {
				currCenterY = tmpY;
				isChange = true;
			}
			if (tmpZ != currZ) {
				currZ = tmpZ;
				isChange = true;
			}
			if (isChange) {
				notifyChildrenChange(UpdateXYZ);
				updatePos = true;
			}
		}
	}

	protected void calcuRotate() {
		if ((UpdateRotate & updateState) != 0) {
			updateState &= ~UpdateRotate;
			int tmpAxis = GlRotateState.axisNone;
			float tmpAngle = 0;
			if ((activeState & GlControler.rotateState) != 0 &&
			 		(controlRotate.getAxis() == axis || GlRotateState.axisNone == axis)) {
					tmpAngle = this.angle + controlRotate.getAngle();
					tmpAxis = controlRotate.getAxis();
			} else {
				tmpAngle = this.angle;
				tmpAxis = this.axis;
			}
			if (parent != null) {
				int parentAxis = parent.getAxis(false);
				float parentAngle = parent.getAngle(false);
				if (parentAxis != GlRotateState.axisNone &&
						(parentAxis == tmpAxis || tmpAxis == GlRotateState.axisNone)) {
					tmpAngle += parentAngle;
					tmpAxis = parentAxis;
				}
			}
			boolean isChange = false;
			if (tmpAngle != currAngle) {
				currAngle = tmpAngle;
				isChange = true;
			}
			if (tmpAxis != currAxis) {
				currAxis = tmpAxis;
				isChange = true;
			}
			if (isChange) {
				notifyChildrenChange(UpdateRotate);
				updatePos = true;
			}
		}
	}
	
	protected void calcuScale() {
		if ((UpdateWH & updateState) != 0) {
			updateState &= ~UpdateWH;
			float tmpW = 0;
			float tmpH = 0;
			float tmpSX = 0;
			float tmpSY = 0;
			if ((activeState & GlControler.scaleState) == 0) {
				tmpW = width * scaleX;
				tmpH = height * scaleY;
				tmpSX = scaleX;
				tmpSY = scaleY;
			} else {
				tmpSX = controlScale.getScaleX() * scaleX;
				tmpSY = controlScale.getScaleY() * scaleY;
				tmpW = width * tmpSX * scaleX;
				tmpH = height * tmpSY * scaleY;
			}	
			if (parent != null && (model & SizeScaleMove) != 0) {
				float px = parent.getScaleX(false);
				float py = parent.getScaleY(false);
				tmpW *= px;
				tmpH *= py;
				tmpSX *= px;
				tmpSY *= py;
			}
			boolean isChange = false;
			if (tmpW != currWidth) {
				currWidth = tmpW;
				isChange = true;
			}
			if (tmpH != currHeight) {
				currHeight = tmpH;
				isChange = true;
			}
			if (tmpSX != currScaleX) {
				currScaleX = tmpSX;
				isChange = true;
			}
			if (tmpSY != currScaleY) {
				currScaleY = tmpSY;
				isChange = true;
			}
			if (isChange) {
				notifyChildrenChange(UpdateXYZ | UpdateWH);
				updatePos = true;
			}
		}
	}
	
	protected void notifyChildrenChange(int update) {
		for (dealIterator.clear(); dealIterator.hasNext();) {
			dealIterator.next().setUpdateChange(update);
		}
	}
	
	public void updateValue() {
		calcuAlpha();
		calcuXYZ();
		calcuRotate();
		calcuScale();
		//updatePos();
		
		for (dealIterator.clear(); dealIterator.hasNext();) {
			GlContainer2D tmp = dealIterator.next();
			if ((tmp.model & UpdateByParent) != 0) {
				tmp.updateValue();
			}
		}
	}
	
	public void updatePos() {
		if (updatePos) {
			updatePos = false;
			glPosition.reset();
			glPosition.translate(currCenterX, currCenterY, currZ);
			switch (currAxis) {
			case GlRotateState.axisX:
				glPosition.rotate(currAngle, 1, 0, 0);
				break;
			case GlRotateState.axisY:
				glPosition.rotate(currAngle, 0, 1, 0);
				break;
			case GlRotateState.axisZ:
				glPosition.rotate(currAngle, 0, 0, 1);
				break;
			}
			glPosition.scale(currWidth, currHeight, 1);
		}
	}
	
	public void setPosMode(int XModel, int YModel) {
		boolean isChange = false;
		if (XModel != -1 && this.XModel != XModel) {
			this.XModel = XModel;
			isChange = true;
		}
		if (YModel != -1 && this.YModel != YModel) {
			this.YModel = YModel;
			isChange = true;
		}
		if (isChange) {
			setUpdateChange(UpdateXYZ);
		}
	}
	
	public boolean setWH(float width, float height) {
		boolean isChange = false;
		if (width >= 0 && this.width != width) {
			this.width = width;
			isChange = true;
		}
		if (height >= 0 && this.height != height) {
			this.height = height;
			isChange = true;
		}
		if (isChange) {
			setUpdateChange(UpdateWH);
		}
		return isChange;
	}
	
	public boolean setScale(float scaleX, float scaleY) {
		boolean isChange = false;
		if (this.scaleX != scaleX) {
			this.scaleX = scaleX;
			isChange = true;
		}
		if (this.scaleY != scaleY) {
			this.scaleY = scaleY;
			isChange = true;
		}
		if (isChange) {
			setUpdateChange(UpdateWH);
		}
		return isChange;
	}
	
	public boolean setXY(float x, float y) {
		boolean isChange = false;
		if (this.centerX != x) {
			this.centerX = x;
			isChange = true;
		}
		if (this.centerY != y) {
			this.centerY = y;
			isChange = true;
		}
		if (isChange) {
			setUpdateChange(UpdateXYZ);
		}
		return isChange;
	}
	
	public void setWHXY(float width, float height, float x, float y) {
		setWH(width, height);
		setXY(x, y);
	}
	
	public void setWHLeftXY(float width, float height, float x, float y) {
		setWH(width, height);
		setLeftXY(x, y);
	}
	
	public void setLeftXY(float x, float y) {
		setXY(x + this.width / 2f, y - this.height / 2);
	}
	
	public void setZ(float z) {
		if (this.z != z) {
			this.z = z;
			setUpdateChange(UpdateXYZ);
		}
	}
	
	public void setRotate(float angle, int axis) {
		boolean isChange = false;
		if (this.axis != axis) {
			this.axis = axis;
			isChange = true;
		}
		if (this.angle != angle) {
			this.angle = angle;
			isChange = true;
		}
		if (isChange) {
			setUpdateChange(UpdateRotate);
		}
	}
	
	protected void initGl(GlObject glObject, GlShader glShader) {
		this.glObject = glObject;
		this.glObject.setGlShader(glShader);
		this.glObject.initMember();
	}

	@Override
	public GlObject getGlObject() {
		return this.glObject;
	}
	
	protected void init() {
		vertexPos = new float[8];
		glPosition = new GlPosition();
		drawZ = 0f;
		updatePos = true;
		glObject = null;
		sortValue = 0;
		compareTouchByZ = true;
		interceptAction = CommonEvent.None;
		this.model = PositionScaleMove | SizeScaleMove | AlphaChange | DrawByParent | TouchByParent | UpdateByParent;
		parent = null;
		viewsTree = new BinarySortTree<GlContainer2D>();
		drawIterator = (BinarySortTree<GlContainer2D>.TreeIterator)viewsTree.iterator();
		touchIterator = (BinarySortTree<GlContainer2D>.TreeIterator)viewsTree.iterator(BinarySortTree.rightFirst);
		dealIterator = (BinarySortTree<GlContainer2D>.TreeIterator)viewsTree.iterator(BinarySortTree.midFirst);
		centerX = 0;
		centerY = 0;
		XModel = PostionXMid;
		YModel = PostionYMid;
		width = 1f;
		height = 1f;
		scaleX = 1f;
		scaleY = 1f;
		z = 0f;
		axis = GlRotateState.axisNone;
		angle = 0;
		alpha = 1f;
		
		currCenterX = 0;
		currCenterY = 0;
		currWidth = 1f;
		currHeight = 1f;
		currZ = 0;
		currAxis = GlRotateState.axisNone;
		currAngle = 0;
		currAlpha = 1f;
		currScaleX = 1f;
		currScaleY = 1f;
		
		isVisible = true;
		canDealEvent = false;
		hasPress = false;
		drawNothing = false;
		
		activeState = 0;
		controlAlpha = null;
		controlRotate = null;
		controlScale = null;
		controlTrans = null;
		
		updateState = UpdateXYZ | UpdateWH |UpdateAlpha | UpdateRotate;
		
		keyEvent = CommonEvent.None;
	}
	
	public boolean getDrawNothing() {
		return this.drawNothing;
	}
	
	public void setDrawNothing(boolean drawNothing) {
		this.drawNothing = drawNothing;
	}
	
	public void initAttr(boolean isRevert) {
		float param = isRevert ? 2.0f : 0f;
		this.glObject.setPosAttr(GlObjManager.getGlObjManager().getGlObjAttr(GlConstant.rectRevertRosAttrName,
				GlObjManager.Rectangle, param));
	}
	
	protected float[] tmpRes = new float[4];
	public void calcuMVPMatrix(GlCamera glCamera) {
		updatePos();
		glCamera.getFinalMatrix(glPosition.getPostion(), this.glObject.getMVPMatrix());
		if (GlShaderUtil.isNeedLight(this.glObject.getGlShader())) {
			GlObjectByLight objectByLight = (GlObjectByLight)glObject;
			VectorUtil.copy(glCamera.getCameraPos(), 3, objectByLight.getCameraPos() );
			VectorUtil.copy(glPosition.getPostion(), 16, objectByLight.getMMatrix());
			//objectByLight.setMMatrix(glPosition.getPostion());
		}
		float tmpZ = 0;
		MathUtil.multiplyPosition(this.glObject.getMVPMatrix(), new float[]{0.5f, 0.5f, 0, 1}, tmpRes);
		vertexPos[0] = tmpRes[0]; vertexPos[1] = tmpRes[1];
		tmpZ += tmpRes[2];

		MathUtil.multiplyPosition(this.glObject.getMVPMatrix(), new float[]{0.5f, -0.5f, 0, 1}, tmpRes);
		vertexPos[2] = tmpRes[0]; vertexPos[3] = tmpRes[1];
		tmpZ += tmpRes[2];

		MathUtil.multiplyPosition(this.glObject.getMVPMatrix(), new float[]{-0.5f, -0.5f, 0, 1}, tmpRes);
		vertexPos[4] = tmpRes[0]; vertexPos[5] = tmpRes[1];
		tmpZ += tmpRes[2];

		MathUtil.multiplyPosition(this.glObject.getMVPMatrix(), new float[]{-0.5f, 0.5f, 0, 1}, tmpRes);
		vertexPos[6] = tmpRes[0]; vertexPos[7] = tmpRes[1];
		tmpZ += tmpRes[2];
		
		drawZ = tmpZ / 4;
		
		synchronized (viewsTree) {
			for (drawIterator.clear(); drawIterator.hasNext();) {
				GlContainer2D tmp = drawIterator.next();
				if ((tmp.model & DrawByParent) != 0) {
					tmp.calcuMVPMatrix(glCamera);
				}
			}
		}
	}
	
	public void setAlpha(float alpha) {
		if (this.alpha != alpha) {
			this.alpha = alpha;
			setUpdateChange(UpdateAlpha);
		}
	}
	
	public void setCanTouch(boolean isCanTouch) {
		this.canDealEvent = isCanTouch;
	}
	
	@Override
	public int onHanderEvent(Event e) {
		parentHanderEvent(e);
		return onHanderEventSelf(e);
	}
	
	public int onHanderEventSelf(Event e) {
		return CommonEvent.None;
	}

	public int handerKeyEvent(Event e) {
		return CommonEvent.None;
	}
	
	@Override
	public EventHander isHanderEvent(Event e) {
		if (isVisible && canDealEvent) {
			boolean isContainSelf = isHanderEventSelf(e);
			if ((this.model & StencilEnable) != 0 && !isContainSelf) {
				return null;
			}
			EventHander res = null;
			for (touchIterator.clear(); touchIterator.hasNext();) {
				GlContainer2D tmp = touchIterator.next();
				if ((tmp.model & TouchByParent) != 0) {
					res = tmp.isHanderEvent(e);
					if (res != null) {
						break;
					}
				}
			}
			if (res != null) {
				return res;
			} else if (isContainSelf) {
				return this;
			}
		}
		return null;
	}
	
	protected boolean isHanderEventSelf(Event e) {
		if (e.action == CommonEvent.Double || e.action == CommonEvent.Down) {
			return isContain( (e.e.getX() * 2 / glView.getWidth() - 1), (1 - 2 * e.e.getY() / glView.getHeight()));
		} else if ((e.action & CommonEvent.KeyEvent) != 0){
			if ((e.action & this.keyEvent) != 0) {
				return true;
			} else {
				return false;
			}
		} else {
			throw new RuntimeException("cant handle this event " + e.action);
		}
	}
	
	public boolean isContain(float x, float y) {
		return MathUtil.isPolygonContain(4, vertexPos, x, y);
	}

	@Override
	public void unActiveState(int state) {
		if ((activeState & state) != 0) {
			activeState &= ~state;
			setStateChange(state);
		}
	}

	@Override
	public void activeState(int state) {
		if ((activeState & state) == 0) {
			activeState |= state;
			setStateChange(state);
		}
	}

	@Override
	public void setStateChange(int state) {
		switch (state) {
		case GlControler.alphaState:
			setUpdateChange(UpdateAlpha);
			break;
		case GlControler.rotateState:
			setUpdateChange(UpdateRotate);
			break;
		case GlControler.scaleState:
			setUpdateChange(UpdateWH);
			break;
		case GlControler.transState:
			setUpdateChange(UpdateXYZ);
			break;
		}
	}

	@Override
	public void register(GlControler glControler) {
		int state = glControler.getState();
		switch (state) {
		case GlControler.alphaState:
			if (null != this.controlAlpha) {
				this.controlAlpha.unRegisterSelf(this);
			}
			this.controlAlpha = (GlControlAlpha)glControler;
			this.controlAlpha.registerSelf(this);
			break;
		case GlControler.rotateState:
			if (null != this.controlRotate) {
				this.controlRotate.unRegisterSelf(this);
			}
			this.controlRotate = (GlControlRotate)glControler;
			this.controlRotate.registerSelf(this);
			break;
		case GlControler.scaleState:
			if (null != this.controlScale) {
				this.controlScale.unRegisterSelf(this);
			}
			this.controlScale= (GlControlScale)glControler;
			this.controlScale.registerSelf(this);
			break;
		case GlControler.transState:
			if (null != this.controlTrans) {
				this.controlTrans.unRegisterSelf(this);
			}
			this.controlTrans = (GlControlTrans)glControler;
			this.controlTrans.registerSelf(this);
			break;
		default:
			throw new RuntimeException("register state is error " + state);	
		}
	}

	@Override
	public void unRegister(int state) {
		switch (state) {
		case GlControler.alphaState:
			if (null != this.controlAlpha) {
				this.controlAlpha.unRegisterSelf(this);
				this.controlAlpha = null;
			}
			break;
		case GlControler.rotateState:
			if (null != this.controlRotate) {
				this.controlRotate.unRegisterSelf(this);
				this.controlRotate = null;
			}
			break;
		case GlControler.scaleState:
			if (null != this.controlScale) {
				this.controlScale.unRegisterSelf(this);
				this.controlScale = null;
			}
			break;
		case GlControler.transState:
			if (null != this.controlTrans) {
				this.controlTrans.unRegisterSelf(this);
				this.controlTrans = null;
			}
			break;
		default:
			throw new RuntimeException("register state is error " + state);	
		}
	}
	
	public float getAlpha(boolean isPure) {
		return isPure ? alpha : currAlpha;
	}
	
	public int getAxis(boolean isPure) {
		return isPure ? axis : currAxis;
	}
	
	public float getAngle(boolean isPure) {
		return isPure ? angle : currAngle;
	}
	
	public float getScaleX(boolean isPure) {
		return isPure ? scaleX : currScaleX;
	}
	
	public float getScaleY(boolean isPure) {
		return isPure ? scaleY : currScaleY;
	}
 	
	public float getX(boolean isPure) {
		return isPure ? centerX : currCenterX;
	}
	
	public float getY(boolean isPure) {
		return isPure ? centerY : currCenterY;
	}
	
	public float getWidth(boolean isPure) {
		return isPure ? width : currWidth;
	}
	
	public float getHeight(boolean isPure) {
		return isPure ? height : currHeight;
	}
	
	public float getZ(boolean isPure) {
		return isPure ? z : currZ;
	}
	
	public boolean getIsVisible() {
		return isVisible;
	}
	
	public void setVisible(boolean isVisible, float alpha) {
		if (isVisible) {
			setAlpha(alpha);
		}
		this.isVisible = isVisible;
	}
	
	protected void onAdd(GlContainer2D parent) {
		
	}
	
	protected void onDel(GlContainer2D parent) {
		
	}
}
