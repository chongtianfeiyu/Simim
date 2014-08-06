package com.neuo.glshader;

import com.neuo.globject.GlObject;

import android.opengl.GLES20;

public class ShaderMatrix {
	private int MVPHandle;
	private int MMHandle;
	private int NorMHandle;
	
	public static final int MVP 	= 1 << 0;
	public static final int MM 		= 1 << 1;
	public static final int NorM 	= 1 << 2;
	public static final int BOTH 	= MVP|MM;
	public static final int TRIPLE 	= BOTH|NorM;
	
	public void setMVPHandle(int MVPHandle) {
		this.MVPHandle = MVPHandle;
	}
	
	public void setMMHandle(int MMHandle) {
		this.MMHandle = MMHandle;
	}
	
	public void setNorMHandle(int NorMHandle) {
		this.NorMHandle = NorMHandle;
	}
	
	public void drawMVP(GlObject glObject) {
		GlMVPMaxtrixHandle glMVPMaxtrixHandle = (GlMVPMaxtrixHandle)glObject;
		GLES20.glUniformMatrix4fv(this.MVPHandle, 1, false, glMVPMaxtrixHandle.getMVPMatrix(), 0);
	}
	
	public void drawMM(GlObject glObject) {
		GlMMatrixHandle glMMatrixHandle = (GlMMatrixHandle)glObject;
		GLES20.glUniformMatrix4fv(this.MMHandle, 1, false, glMMatrixHandle.getMMatrix(), 0);
	}
	
	public void drawNorM(GlObject glObject) {
		GlNorMatrixHandle glMatrixHandle = (GlNorMatrixHandle)glObject;
		GLES20.glUniformMatrix4fv(this.NorMHandle, 1, false, glMatrixHandle.getNorMatrix(), 0);
	}

	public void drawMatrix(GlObject glObject, int model) {
		if ((model&MVP) != 0)drawMVP(glObject);
		if ((model&MM) != 0)drawMM(glObject);
		if ((model&NorM) != 0)drawNorM(glObject);
	}
}
