package com.neuo.common;

public class ColorUtil {
	public static final int White 	= 0xffffff;
	public static final int Red 	= 0xff0000;
	public static final int Green 	= 0x00ff00;
	//public static final int Blue 	= 0x0000ff;
	public static final int Peony 	= 0xff00ff;
	public static final int Ching 	= 0x00ffff;
	public static final int Yellow 	= 0xffff00;
	public static final int Black 	= 0x000000;
	public static final int Navy 	= 0x70db93;
	public static final int Cholco 	= 0x5c3317;
	public static final int Bluish 	= 0x9f5f9f;
	public static final int Brass 	= 0xb5a642;
	public static final int Coral 	= 0xff7f00;
	public static final int Olive 	= 0x4f4f2f;
	public static final int Forest 	= 0x238e23;
	public static final int Gold 	= 0xcd7f23;
	public static final int Wathet 	= 0xc0d9d9;
	public static final int Maroon 	= 0x8e236b;
	public static final int Slivery = 0xe6e8fa;
	public static final int Violet 	= 0x4f2f4f;

	public static void getRGBAColor(int tag, float A, float[] res) {
		res[3] = A;
		for (int i = 0; i < 3; i++) {
			res[2 - i] = tag & 0xff;
			res[2 - i] = res[2 - i] / 0xff;
			tag = tag >> 8;
		}
	}

	public static float[] getRGBAColor(int tag, float A) {
		float[] color = new float[4];
		color[3] = A;
		for (int i = 0; i < 3; i++) {
			color[2 - i] = tag & 0xff;
			color[2 - i] = color[2 - i] / 0xff;
			tag = tag >> 8;
		}
		return color;
	}
	
	public static float[] getRGBAColor(float R, float G, float B, float A) {
		float[] color = new float[]{R, G, B, A};
		return color;
	}
}
