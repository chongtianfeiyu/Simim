package com.neuo.glshader;

import com.neuo.globject.GlObject;

import android.content.Context;
import android.opengl.GLES20;

public class ShaderByLightTex extends GlShader {
	
    protected int muMVPMatrixHandle;//总变换矩阵引用id
    protected int muMMatrixHandle;
    protected int muNorMatrixHandle;
    
    protected int muDirectColorHandle;
    protected int muAmbientHandle;
    protected int muDiffuseHandle;
    protected int muSpecularHandle;
    protected int muShininessHandle;
    protected int muSelectColorHandle;
    protected int muIsLightHandle;
    
    protected int muLightLocationHandle;
    protected int muCameraHandle;
    protected int muLightAmbientHandle;
    protected int muLightDiffuseHandle;
    protected int muLightSpecularHandle;
    
    protected int muTexIdHandle;
    
    protected int maNormalHandle;
    protected int maPositionHandle; //顶点位置属性引用id  
    protected int maTexPosHandle;
    protected int muAlphaHandle;

    protected static final String myVertexName = "vertexLightTex.sh";
    protected static final String myFragName = "fragLightTex.sh";

    protected static final String myNormalName = "aNormal";
    protected static final String myPositionName = "aPosition";
    protected static final String myMMName = "uMMatrix";
    protected static final String myMVPName = "uMVPMatrix";
    protected static final String myNMName = "uNorMatrix";
    protected static final String myLightLocation = "uLightLocation";
    protected static final String myCamera = "uCamera";
    protected static final String myShininess = "uShininess";
    protected static final String myLightAmbient = "uLightAmbient";
    protected static final String myLightDiffuse = "uLightDiffuse";
    protected static final String myLightSpecular = "uLightSpecular";
    protected static final String myDirectColor = "uColor";
    protected static final String mySpecular = "uSpecular";
    protected static final String myDiffuse = "uDiffuse";
    protected static final String myAmbient = "uAmbient";
    protected static final String mySelectColor = "uColorSelect";
    protected static final String myIsLightOpen = "uIsLightOpen";
    protected static final String myTexIdName = "sTexture";
    protected static final String myTexPosName = "aTexCoor";
    protected static final String myAlpha = "uAlpha";

    public static final int myShaderTag = 4;
    
    protected ShaderColor colorShader = new ShaderColor();
    protected ShaderMatrix matrixShader = new ShaderMatrix();
    protected ShaderLight lightShader = new ShaderLight();
    protected ShaderVertex vertexShader = new ShaderVertex();
    
    public ShaderByLightTex() {
    	super(myVertexName, myFragName);
    	this.setTag(myShaderTag);
	}
    
    protected int getCameraHandle()
    {
    	return muCameraHandle;
    }
 
	@Override
	protected void reinitShader(Context context) {
		super.reinitShader(context);
        
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, myMVPName);//总变换矩阵引用id
        muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, myMMName);
        muNorMatrixHandle = GLES20.glGetUniformLocation(mProgram, myNMName);
        
        muDirectColorHandle = GLES20.glGetUniformLocation(mProgram, myDirectColor);
        muAmbientHandle = GLES20.glGetUniformLocation(mProgram, myAmbient);
        muDiffuseHandle = GLES20.glGetUniformLocation(mProgram, myDiffuse);
        muSpecularHandle = GLES20.glGetUniformLocation(mProgram, mySpecular);
        muSelectColorHandle = GLES20.glGetUniformLocation(mProgram, mySelectColor);
        muIsLightHandle = GLES20.glGetUniformLocation(mProgram, myIsLightOpen);
        muLightLocationHandle = GLES20.glGetUniformLocation(mProgram, myLightLocation);
        muCameraHandle = GLES20.glGetUniformLocation(mProgram, myCamera);
        muShininessHandle = GLES20.glGetUniformLocation(mProgram, myShininess);
        muLightAmbientHandle = GLES20.glGetUniformLocation(mProgram, myLightAmbient);
        muLightDiffuseHandle = GLES20.glGetUniformLocation(mProgram, myLightDiffuse);
        muLightSpecularHandle = GLES20.glGetUniformLocation(mProgram, myLightSpecular);
        muTexIdHandle = GLES20.glGetUniformLocation(mProgram, myTexIdName);
        
        maNormalHandle = GLES20.glGetAttribLocation(mProgram, myNormalName);
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, myPositionName); //顶点位置属性引用id 
        maTexPosHandle = GLES20.glGetAttribLocation(mProgram, myTexPosName);
        
        muAlphaHandle = GLES20.glGetUniformLocation(mProgram, myAlpha);

        colorShader.setAlphaHandle(muAlphaHandle);
        colorShader.setColorHandle(muDirectColorHandle);
        colorShader.setAmbientHandle(muAmbientHandle);
        colorShader.setDiffuseHandle(muDiffuseHandle);
        colorShader.setSpecularHandle(muSpecularHandle);
        colorShader.setShininess(muShininessHandle);
        colorShader.setModelHandle(muSelectColorHandle);
        colorShader.setTexHandle(muTexIdHandle);
        
        matrixShader.setMVPHandle(muMVPMatrixHandle);
        matrixShader.setMMHandle(muMMatrixHandle);
        matrixShader.setNorMHandle(muNorMatrixHandle);
        
        lightShader.setIsLightOpen(muIsLightHandle);
        lightShader.setLightAmbient(muLightAmbientHandle);
        lightShader.setLightDiffuse(muLightDiffuseHandle);
        lightShader.setLightLocation(muLightLocationHandle);
        lightShader.setLightSpecular(muLightSpecularHandle);
        
        vertexShader.setNormalHandle(maNormalHandle);
        vertexShader.setPositionHandle(maPositionHandle);
        vertexShader.setTexCoorHandle(maTexPosHandle);
	}
	
	@Override
	public void drawObject(GlObject glObject) {
		if (null == glObject)return;
		GLES20.glUseProgram(this.getProgram());
		matrixShader.drawMatrix(glObject, ShaderMatrix.TRIPLE);
		colorShader.drawColor(glObject, ShaderColor.TRIPLE);
		GlCameraHandle cameraHandle = (GlCameraHandle)glObject;
		GLES20.glUniform3fv(this.getCameraHandle(), 1, cameraHandle.getCamera(), 0);
		lightShader.drawLight(glObject);
		vertexShader.drawVertex(glObject, ShaderVertex.BOTH_NORANDTEX);
	}
}
