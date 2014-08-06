package com.neuo.glshader;

import com.neuo.globject.GlObject;

import android.content.Context;
import android.opengl.GLES20;

public class ShaderByTex extends GlShader {
    protected int muMVPMatrixHandle;//总变换矩阵引用id
    protected int maPositionHandle; //顶点位置属性引用id  
    protected int maTexPosHandle;
    protected int muTexIdHandle;
    protected int muDirectColorHandle;
    protected int muIsDirectHandle;
    protected int muAlphaHandle;

    protected static final String myVertexName = "vertexTex.sh";
    protected static final String myFragName = "fragTex.sh";
    
    protected static final String myPositionName = "aPosition";
    protected static final String myMVPName = "uMVPMatrix";
    protected static final String myTexPosName = "aTexCoor";
    protected static final String myDirectColor = "uColor";
    protected static final String myIsDerectColor = "isDirectColor";
    protected static final String myAlpha = "uAlpha";
    protected static final String myTexIdName = "sTexture";
    
    public static final int myShaderTag = 2;
    
    protected ShaderColor colorShader = new ShaderColor();
    protected ShaderMatrix matrixShader = new ShaderMatrix();
    protected ShaderVertex vertexShader = new ShaderVertex();
    
    public ShaderByTex() {
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
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, myMVPName);  
        muTexIdHandle = GLES20.glGetUniformLocation(mProgram, myTexIdName);
        muDirectColorHandle = GLES20.glGetUniformLocation(mProgram, myDirectColor);
        muIsDirectHandle = GLES20.glGetUniformLocation(mProgram, myIsDerectColor);
        muAlphaHandle = GLES20.glGetUniformLocation(mProgram, myAlpha);
        
        colorShader.setAlphaHandle(muAlphaHandle);
        colorShader.setColorHandle(muDirectColorHandle);
        colorShader.setModelHandle(muIsDirectHandle);
        colorShader.setTexHandle(muTexIdHandle);
        matrixShader.setMVPHandle(muMVPMatrixHandle);
        vertexShader.setPositionHandle(maPositionHandle);
        vertexShader.setTexCoorHandle(maTexPosHandle);
	}
	
	@Override
	public void drawObject(GlObject glObject) {
		if (null == glObject)return;
		GLES20.glUseProgram(this.getProgram());
		colorShader.drawColor(glObject, ShaderColor.BOTH_COLORANDTEX);
		matrixShader.drawMatrix(glObject, ShaderMatrix.MVP);
		vertexShader.drawVertex(glObject, ShaderVertex.TEX);
	}
}
