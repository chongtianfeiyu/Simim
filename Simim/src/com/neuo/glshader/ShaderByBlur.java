package com.neuo.glshader;

import android.content.Context;
import android.opengl.GLES20;

import com.neuo.globject.GlObject;

public class ShaderByBlur extends GlShader {
    protected int maPositionHandle; 
    protected int maTexPosHandle;
    protected int muTexIdHandle;
    protected int muOriantHandle;
    protected int muBlurNumHandle;
    protected int muBlurScaleHandle;
    protected int muBlurForceHandle;
    protected int muDirectColorHandle;
    protected int muIsDirectHandle;
    protected int muAlphaHandle;
    protected int muBlurTexSizeHandle;
    protected int muPosScaleHandle;

    protected static final String myVertexName = "vertexImage.sh";
    protected static final String myFragName = "fragBlur.sh";
    
    protected static final String myPositionName = "aPosition";
    protected static final String myTexPosName = "aTexCoor";
    protected static final String myTexIdName = "Sample0";
    protected static final String myOrientName = "Orientation";
    protected static final String myBlurNumName = "BlurAmount";
    protected static final String myBlurScaleName = "BlurScale";
    protected static final String myBlurForceName = "BlurStrength";
    protected static final String myBlurTexSizeName = "uTexSize";
    protected static final String myPosScaleName = "uPosScale";
    protected static final String myDirectColor = "uColor";
    protected static final String myIsDerectColor = "isDirectColor";
    protected static final String myAlpha = "uAlpha";

    public static final int myShaderTag = 5;
    
    protected ShaderColor colorShader = new ShaderColor();
    protected ShaderVertex vertexShader = new ShaderVertex();
    protected ShaderBlur blurShader = new ShaderBlur();
    
    public ShaderByBlur() {
    	super(myVertexName, myFragName);
    	this.setTag(myShaderTag);
	}
    
	@Override
	protected void reinitShader(Context context) {
		super.reinitShader(context);
        //获取程序中顶点位置属性引用id  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, myPositionName);
        maTexPosHandle = GLES20.glGetAttribLocation(mProgram, myTexPosName);
        //获取程序中总变换矩阵引用id
        muOriantHandle = GLES20.glGetUniformLocation(mProgram, myOrientName);
        muBlurForceHandle = GLES20.glGetUniformLocation(mProgram, myBlurForceName);
        muBlurNumHandle = GLES20.glGetUniformLocation(mProgram, myBlurNumName);
        muBlurScaleHandle = GLES20.glGetUniformLocation(mProgram, myBlurScaleName);
        muBlurTexSizeHandle = GLES20.glGetUniformLocation(mProgram, myBlurTexSizeName);
        muTexIdHandle = GLES20.glGetUniformLocation(mProgram, myTexIdName);
        muDirectColorHandle = GLES20.glGetUniformLocation(mProgram, myDirectColor);
        muIsDirectHandle = GLES20.glGetUniformLocation(mProgram, myIsDerectColor);
        muAlphaHandle = GLES20.glGetUniformLocation(mProgram, myAlpha);
        muPosScaleHandle = GLES20.glGetUniformLocation(mProgram, myPosScaleName);
        
        colorShader.setAlphaHandle(muAlphaHandle);
        colorShader.setColorHandle(muDirectColorHandle);
        colorShader.setModelHandle(muIsDirectHandle);
        colorShader.setTexHandle(muTexIdHandle);
        vertexShader.setPositionHandle(maPositionHandle);
        vertexShader.setTexCoorHandle(maTexPosHandle);
        vertexShader.setPosScaleHandle(muPosScaleHandle);
        blurShader.setBlurForceHandle(muBlurForceHandle);
        blurShader.setBlurNumHandle(muBlurNumHandle);
        blurShader.setBlurScaleHandle(muBlurScaleHandle);
        blurShader.setOriantHandle(muOriantHandle);
        blurShader.setBlurTexSizeHandle(muBlurTexSizeHandle);
	}
	
	@Override
	public void drawObject(GlObject glObject) {
		if (null == glObject)return;
		GLES20.glUseProgram(this.getProgram());
		colorShader.drawColor(glObject, ShaderColor.BOTH_COLORANDTEX);
		blurShader.drawBlur(glObject);
		vertexShader.drawVertex(glObject, ShaderVertex.TEX | ShaderVertex.VSCALE);
	}
}
