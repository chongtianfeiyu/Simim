package com.neuo.glshader;

import com.neuo.globject.GlObject;

import android.content.Context;

public abstract class GlShader {
	private static final String ShaderPre = "shader/";
	protected int shaderTag;
	
	protected int mProgram;//�Զ�����Ⱦ���߳���id
    protected String mVertexShader;//������ɫ��    	 
    protected String mFragmentShader;//ƬԪ��ɫ��
	
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
		//���ض�����ɫ���Ľű�����
        mVertexShader = GlShaderUtil.loadFromAssetsFile(ShaderPre + vertexName, context.getResources());
        //����ƬԪ��ɫ���Ľű�����
        mFragmentShader = GlShaderUtil.loadFromAssetsFile(ShaderPre + fragName, context.getResources());  
        //���ڶ�����ɫ����ƬԪ��ɫ����������
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
