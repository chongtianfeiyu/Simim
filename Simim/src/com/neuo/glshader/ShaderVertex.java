package com.neuo.glshader;

import java.nio.FloatBuffer;

import com.neuo.globject.GlObject;

import android.opengl.GLES20;

public class ShaderVertex {
	
	protected int maPositionHandle;
	protected int maNormalHandle;
	protected int maTexCoorHandle;
	protected int muPosScaleHandle;
	
	public static final int NONE 	= 0;
	public static final int NORMAL 	= 1 << 0;
	public static final int TEX 	= 1 << 1;
	public static final int VSCALE = 1 << 2;
	public static final int BOTH_NORANDTEX = NORMAL | TEX;
	
	public void setPositionHandle(int positionHandle) {
		this.maPositionHandle = positionHandle;
	}
	
	public void setPosScaleHandle(int posScaleHandle) {
		this.muPosScaleHandle = posScaleHandle;
	}
	
	public void setNormalHandle(int normalHandle) {
		this.maNormalHandle = normalHandle;
	}
	
	public void setTexCoorHandle(int texCoorHandle) {
		this.maTexCoorHandle = texCoorHandle;
	}
	
	protected void drawNor(GlObject glObject) {
		GlPositionNormalHandle glPositionNormalHandle = (GlPositionNormalHandle)glObject;
		FloatBuffer mNormalBuffer = glPositionNormalHandle.getPositionNormalBuffer();
		GLES20.glVertexAttribPointer(
				maNormalHandle,
				3,
				GLES20.GL_FLOAT,
				false,
				3 * 4,
				mNormalBuffer);
		GLES20.glEnableVertexAttribArray(maNormalHandle);
	}
	
	protected void drawTex(GlObject glObject) {
		GlPositionTexHandle glPositionTexHandle = (GlPositionTexHandle)glObject;
		FloatBuffer mTexCoorBuffer = glPositionTexHandle.getPositionTexBuffer();
        GLES20.glVertexAttribPointer  
        (
        		maTexCoorHandle, 
        		2, 
        		GLES20.GL_FLOAT, 
        		false,
        		2 * 4,   
        		mTexCoorBuffer);
        GLES20.glEnableVertexAttribArray(maTexCoorHandle); 
	}
	
	protected void drawPosScale(GlObject glObject) {
		GlPositionHandle glPositionHandle = (GlPositionHandle)glObject;
        GLES20.glUniform3fv(muPosScaleHandle, 1, glPositionHandle.getPosScale(), 0);
	}
	
	public void drawVertex(GlObject glObject, int model) {
		GlPositionHandle glPositionHandle = (GlPositionHandle)glObject;
		int vCount = glPositionHandle.getPositionCount();
		int drawModel = glPositionHandle.getPositionModel();
		FloatBuffer mVertexBuffer = glPositionHandle.getPositionBuffer();
		GLES20.glVertexAttribPointer(
				maPositionHandle,
         		3, 
         		GLES20.GL_FLOAT, 
         		false,
                3 * 4,   
                mVertexBuffer
         );
        //为画笔指定顶点纹理坐标数据
		GLES20.glEnableVertexAttribArray(maPositionHandle);
		if ((model & NORMAL) != 0)drawNor(glObject);
		if ((model & TEX) != 0)drawTex(glObject);
		if ((model& VSCALE) != 0)drawPosScale(glObject);
		GLES20.glDrawArrays(drawModel, 0, vCount);
	}
}
