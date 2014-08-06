package com.neuo.globject;

import java.nio.FloatBuffer;

import com.neuo.glcommon.GlColorMaterialAndTex;
import com.neuo.glcommon.GlControler.GlControlAlpha;
import com.neuo.glcommon.GlControler.GlControlHandle;
import com.neuo.glcommon.GlControler;
import com.neuo.glcommon.GlPosAttribute;
import com.neuo.glshader.GlColorHandle;
import com.neuo.glshader.GlMVPMaxtrixHandle;
import com.neuo.glshader.GlPositionHandle;
import com.neuo.glshader.GlShader;

public class GlObject 
implements GlMVPMaxtrixHandle, GlPositionHandle, GlColorHandle, GlControlHandle {

	public static interface GlObjectInterface {
		public GlObject getGlObject();
	}

	protected GlObject glObject;
	protected GlPosAttribute posAttribute;
	protected GlShader glShader;
	
	protected int activeState = 0;
	protected GlColorMaterialAndTex colorMaterialAndTex;
	protected float[] MVPMatrix;

	protected GlControler.GlControlAlpha alphaControl;
	
	public GlObject() {
		setDrawObject(this);
	}

	public void initMember() {
		if (colorMaterialAndTex == null) {
			colorMaterialAndTex = new GlColorMaterialAndTex();
		}
		if (MVPMatrix == null) {
			MVPMatrix = new float[16];
		}
	}
	
	public void setGlShader(GlShader glShader) {
		this.glShader = glShader;
	}
	
	public GlShader getGlShader() {
		return this.glShader;
	}
	
	protected void setDrawObject(GlObject glObject) {
		this.glObject = glObject;
	}
	
	public void draw() {
		glShader.drawObject(this.glObject);
	}
	
	public void activeState(int state) {
		activeState |= state;
	}

	public void unActiveState(int state) {
		activeState &= ~state;
	}
	
	public void setMVPMatrix(float[] MVPMatrix) {
		this.MVPMatrix = MVPMatrix;
	}
	
	public void setPosAttr(GlPosAttribute positionAttribute) {
		this.posAttribute = positionAttribute;
	}
	
	public GlPosAttribute getPosAttr() {
		return this.posAttribute;
	}
	
	public void clearPosAttr() {
		posAttribute = null;
	}
	
	public GlColorMaterialAndTex getMaterialColor() {
		return colorMaterialAndTex;
	}
	
	public void setMaterialColor(GlColorMaterialAndTex color) {
		colorMaterialAndTex = color;
	}

	@Override
	public float[] getColor() {
		return colorMaterialAndTex.getDirectColor();
	}

	@Override
	public float getAlpha() {
		if ((activeState & GlControler.alphaState) == 0) {
			return colorMaterialAndTex.getAlpha();
		} else {
			return colorMaterialAndTex.getAlpha() * alphaControl.getAlpha();
		}
	}

	@Override
	public FloatBuffer getPositionBuffer() {
		return posAttribute.getPostionBuffer();
	}

	@Override
	public int getPositionCount() {
		return posAttribute.getPosCount();
	}

	@Override
	public float[] getPosScale() {
		return posAttribute.getPosScale();
	}

	@Override
	public int getPositionModel() {
		return posAttribute.getPosModel();
	}

	@Override
	public float[] getMVPMatrix() {
		return MVPMatrix;
	}

	@Override
	public void setStateChange(int state) {
	}

	@Override
	public void register(GlControler glControler) {
		if ((glControler.getState() & GlControler.alphaState) != 0) {
			if (null != this.alphaControl) {
				this.alphaControl.unRegisterSelf(this);
			}
			this.alphaControl = (GlControlAlpha)glControler;
			alphaControl.registerSelf(this);
		}
	}

	@Override
	public void unRegister(int state) {
		if ((state & GlControler.alphaState) != 0) {
			if (null != this.alphaControl) {
				this.alphaControl.unRegisterSelf(this);
				this.alphaControl = null;
			}
		}
		
	}
}
