package com.neuo.glshader;

import com.neuo.globject.GlObject;

import android.content.Context;
import android.opengl.GLES20;

public class ShaderByDirect extends GlShader{
	
    protected int muMVPMatrixHandle;//总变换矩阵引用id
    protected int maPositionHandle; //顶点位置属性引用id  
    protected int muDirectColorHandle;
    protected int muAlphaHandle;

    protected static final String myVertexName = "vertex.sh";
    protected static final String myFragName = "frag.sh";
    protected static final String myPositionName = "aPosition";
    protected static final String myMVPName = "uMVPMatrix";
    protected static final String myDirectColor = "uColor";
    protected static final String myAlpha = "uAlpha";
    
    public static final int myShaderTag = 1;
    
    protected ShaderColor colorShader = new ShaderColor();
    protected ShaderMatrix matrixShader = new ShaderMatrix();
    protected ShaderVertex vertexShader = new ShaderVertex();
    
    public ShaderByDirect() {
    	super(myVertexName, myFragName);
    	this.setTag(myShaderTag);
	}
    
	@Override
	protected void reinitShader(Context context) {
		super.reinitShader(context);
        //获取程序中顶点位置属性引用id  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, myPositionName);
        //获取程序中总变换矩阵引用id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, myMVPName);  
        muDirectColorHandle = GLES20.glGetUniformLocation(mProgram, myDirectColor);
        muAlphaHandle = GLES20.glGetUniformLocation(mProgram, myAlpha);
        colorShader.setColorHandle(muDirectColorHandle);
        colorShader.setAlphaHandle(muAlphaHandle);
        matrixShader.setMVPHandle(muMVPMatrixHandle);
        vertexShader.setPositionHandle(maPositionHandle);
	}
	
	@Override
	public void drawObject(GlObject glObject) {
		if (null == glObject)return;
		GLES20.glUseProgram(this.getProgram());
		colorShader.drawColor(glObject, ShaderColor.DIRECT_COLOR);
		matrixShader.drawMatrix(glObject, ShaderMatrix.MVP);
		vertexShader.drawVertex(glObject, ShaderVertex.NONE);
	}

}
