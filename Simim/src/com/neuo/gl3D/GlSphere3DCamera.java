package com.neuo.gl3D;

public class GlSphere3DCamera extends GlCamera3D {
	public static final int constantUp = 0;
	public static final int revertUp = 1;
	protected float radius = 1000;
	protected int upModel = revertUp;
	protected float rA = 0f, yA = 90f;
	
	public GlSphere3DCamera() {
		super();
		setDesPos(0f, 0f, 0f, false);
		calcuEyePos(false);
		calcuUpModel(false);
	}
	
	protected void calcuEyePos(boolean isCalcu) {
		float x, y, z;
		y = radius * (float)Math.cos(Math.toRadians(yA));
		float xz = radius * (float)Math.sin(Math.toRadians(yA));
		z = xz * (float)Math.cos(Math.toRadians(rA));
		x = xz * (float)Math.sin(Math.toRadians(rA));
		
		calcuUpModel(false);
		setEyePos(x, y, z, false, isCalcu);
	}
	
	public void setRadius(float radius,
					boolean isCalcu) {
		if (radius > 0f && this.radius != radius) {
			this.radius = radius;
			calcuEyePos(isCalcu);
		}
	}
	
	public void changeRadius(float r, boolean isCalcu) {
		setRadius(this.radius + r, isCalcu);
	}
	
	public void setUpModel(int upModel, boolean isCalcu) {
		if (this.upModel != upModel) {
			this.upModel = upModel;
			calcuUpModel(isCalcu);
		}
	}
	
	public void calcuUpModel(boolean isCalcu) {
		if (this.upModel == constantUp ||
				yA <= 90f) {
			setUpDir(0f, 1f, 0f, isCalcu);
		} else {
			setUpDir(0f, -1f, 0f, isCalcu);
		}
	}
	
	public float getRadius() {
		return radius;
	}
	
	private static final float tY = 179.9f;
	private static final float bY = 0.1f;
	public void rotateEye(float r, float y, boolean isCalcu) {
		yA += y;
		if (yA > tY) {
			yA = tY;
		} else if (yA < bY) {
			yA = bY;
		}
		rA += r;
		calcuEyePos(isCalcu);
	}
	
}
