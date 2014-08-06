package com.neuo.glshader;

import java.nio.FloatBuffer;

public interface GlPositionHandle {
	public FloatBuffer getPositionBuffer();
	public int getPositionCount();
	public int getPositionModel();
	public float[] getPosScale();
}
