package com.neuo.glshader;

import com.neuo.globject.GlObject;

import android.content.Context;

public abstract class GlShader {
	private static final String ShaderPre = "shader/";
	protected int shaderTag;
	
	protected int mProgram;//自定义渲染管线程序id
    protected String mVertexShader;//顶点着色器    	 
    protected String mFragmentShader;//片元着色器
	
    protected String vertexName;
    protected String fragName;
    protected long reinitId = -1;
    
    public long getReinitId() {
    	return reinitId;
    }
    
    protected void setReinitId(long reinitId) {
    	this.reinitId = reinitId;
    }
    
    protected int getProgram() {
    	return mProgram;
    }
    
	public void initShader(long reinitId, Context context) {
		if (this.reinitId >= reinitId)return;
		this.reinitId = reinitId;
		reinitShader(context);
	}
	
	public GlShader(String vertexName, String fragName) {
		this.vertexName = vertexName;
		this.fragName = fragName;
	}
	
	protected void reinitShader(Context context) {
		//加载顶点着色器的脚本内容
        mVertexShader = GlShaderUtil.loadFromAssetsFile(ShaderPre + vertexName, context.getResources());
        //加载片元着色器的脚本内容
        mFragmentShader = GlShaderUtil.loadFromAssetsFile(ShaderPre + fragName, context.getResources());  
        //基于顶点着色器与片元着色器创建程序
        mProgram = GlShaderUtil.createProgram(mVertexShader, mFragmentShader);
	}
	
	public int getTag() {
		return shaderTag;
	}
	
	public void setTag(int shaderTag) {
		this.shaderTag = shaderTag;
	}
	
	public abstract void drawObject(GlObject glObject);
}
