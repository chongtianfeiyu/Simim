package com.neuo.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import android.opengl.Matrix;

public class MathUtil {

	//private static final int randomArrayRange = 10;
	//public static final int[] randomArray;
	
	public static final Random random = new Random(); 
	static {
		random.setSeed(System.currentTimeMillis());
	}
	/*
	static {
		boolean[] tmp = new boolean[randomArrayRange];
		for (int i = 0; i < randomArrayRange; i++) {
			tmp[i] = false;
		}
		random.setSeed(System.currentTimeMillis());
		randomArray = new int[randomArrayRange];
		for (int i = 0; i < randomArrayRange; i++) {
			int t = Math.abs(random.nextInt()) % randomArrayRange;
			while (tmp[t]) {
				t = Math.abs(random.nextInt()) % randomArrayRange;
			}
			tmp[t] = true;
			randomArray[i] = t; 
		}
	}
	*/
	
	private static final int randomFactor = 101;
	private static final int randomExtra = 59;
	//private static final int randomRange = 65535;
	public static int getRandomInteger(int seed) {
		int newSeed = seed;
		int index = 0;
		do {
			newSeed += index;
			newSeed =  newSeed * randomFactor + randomExtra;
			if (newSeed < 0)newSeed = -newSeed;
			index++;
		} while (seed == newSeed);
		return newSeed;
	}


	public static MessageDigest md5;
	private static boolean isInitMD5 = false;
	private static final char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'}; 
	
	private static void initMD5() {
		if (!isInitMD5) {
			synchronized (hexDigits) {
				if(!isInitMD5) {
					isInitMD5 = true;
					try {
						md5 = MessageDigest.getInstance("MD5");
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
						throw(new RuntimeException("new md5 fail"));
					}
				}
			}
		}
	}
	
	public static int calcuSum(int[] array) {
		int sum = 0;
		for (int i = 0; i < array.length; i++) {
			sum += array[i];
		}
		return sum;
	}
	
	public static String getMD5(byte[] chars) {
		initMD5();
		md5.reset();
		md5.update(chars);
		byte[] md = md5.digest();
		int j = md.length;
        char str[] = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++) {
            byte byte0 = md[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
		return new String(str);
	}
	
	public static float calcuCenter(float b, float l) {
		return b + l / 2f;
	}
	
	public static int toColorHex(int r, int g, int b) {
		int res = 0;
		res |= r << 16;
		res |= g << 8;
		res |= b;
		return res;
	}
	
	public static int pow(int a, int b) {
		if (b == 0) {
			return 1;
		}
		int res = a;
		for (int i = 0; i < b - 1; i ++) {
			res *= a;
		}
		return res;
	}
	
	public static float[] getConicXYR(float angle, float e, float p, boolean isLeft) {
		float tmp = (float)Math.cos(Math.toRadians(angle));
		float R;
		if (isLeft) {
			R = 1 - e * tmp;
		} else {
			R = 1 + e * tmp;
		}
		if (0 == R) {
			R = Float.MIN_VALUE;
		}
		
		R = e * p / R;
		float[] XY = new float[3];
		XY[0] = R * tmp;
		XY[1] = R * (float)Math.sin(Math.toRadians(angle));
		XY[2] = R;
		return XY;
	}
	
	public static float getMin(float[] arr, int a0, int an, int interval) {
		float min = Float.NEGATIVE_INFINITY;
		if (an - a0 > 2 * interval - 1) {
			float min1 = Float.NEGATIVE_INFINITY, min2 = Float.NEGATIVE_INFINITY;
			int mid = (an - a0) / interval / 2;
			min1 = getMin(arr, a0, a0 + interval * mid, interval);
			min2 = getMin(arr, a0 + (mid + 1) * interval, an, interval);
			if (min1 < min2) {
				min = min1;
			} else {
				min = min2;
			}
		} else if (interval <= an - a0) {
			min = arr[a0] > arr[a0 + interval] ? arr[a0 + interval] : arr[a0];
		} else if (an >= a0) {
			min = arr[a0];
		}
		return min;
	}
	
	public static float getMax(float[] arr, int a0, int an, int interval) {
	 	float max = Float.POSITIVE_INFINITY;
		if (an - a0 > 2 * interval - 1) {
			float max1 = Float.POSITIVE_INFINITY, max2 = Float.POSITIVE_INFINITY;
			int mid = (an - a0) / interval / 2;
			max1 = getMax(arr, a0, a0 + mid * interval, interval);
			max2 = getMax(arr, a0 + (mid + 1) * interval, an, interval);
			if (max1 < max2) {
				max = max2;
			} else {
				max = max1;
			}
		} else if (interval <= an - a0) {
			max = arr[a0] > arr[a0 + interval] ? arr[a0] : arr[a0 + interval];
		} else if (an >= a0) {
			max = arr[a0];
		}
		return max;
	}
	
	public static float[] getMinAndMax(float[] arr, int a0, int an, int interval) {
		float[] result = new float[]{Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY};
		if (an - a0 > 2 * interval - 1) {
			int mid = (an - a0) / interval / 2;
			float[] result1 = getMinAndMax(arr, a0, a0 + mid * interval, interval);
			float[] result2 = getMinAndMax(arr, a0 + (mid + 1) * interval, an, interval);
			if (result1[0] < result2[0]) {
				result[0] = result1[0];
			} else {
				result[0] = result2[0];
			} if (result1[1] < result2[1]) {
				result[1] = result2[1];
			} else {
				result[1] = result1[1];
			}
		} else if (interval <= an - a0) {
			if (arr[a0] > arr[a0 + interval]) {
				result[0] = arr[a0 + interval];
				result[1] = arr[a0];
			} else {
				result[1] = arr[a0 + interval];
				result[0] = arr[a0];
			}
		} else if (an >= a0) {
			result[0] = arr[a0];
			result[1] = arr[a0];
		}
		return result;
	}
	
	public static float[] getRectangleByPoints2D(float[] arr, int a0, int a1, int an, int interval) {
		float[] resultX = getMinAndMax(arr, a0, an, interval);
		float[] resultY = getMinAndMax(arr, a1, an, interval);
		return new float[]{resultX[0], resultY[0], resultX[1], resultY[1]};
	}

	public static float[] getRectangleByPoints2D(float[] x, float[] y, int a0, int an) {
		float[] resultX = getMinAndMax(x, a0, an, 1);
		float[] resultY = getMinAndMax(y, a0, an, 1);
		return new float[]{resultX[0], resultY[0], resultX[1], resultY[1]};
	}

	public static float[] getRectangleByPoints3D(float[] arr, int a0, int a1, int a2, int an, int interval) {
		float[] resultX = getMinAndMax(arr, a0, an, interval);
		float[] resultY = getMinAndMax(arr, a1, an, interval);
		float[] resultZ = getMinAndMax(arr, a2, an, interval);
		return new float[]{resultX[0], resultY[0], resultZ[0], resultX[1], resultY[1], resultZ[1]};
	}

	public static float[] getRectangleByPoints3D(float[] x, float[] y, float[] z, int a0, int an) {
		float[] resultX = getMinAndMax(x, a0, an, 1);
		float[] resultY = getMinAndMax(y, a0, an, 1);
		float[] resultZ = getMinAndMax(z, a0, an, 1);
		return new float[]{resultX[0], resultY[0], resultZ[0], resultX[1], resultY[1], resultZ[1]};
	}
	
	public static float[] multiplyPosition3D(float[] matrix, float[] position) {
		float[] result = new float[4];
		Matrix.multiplyMV(result, 0, matrix, 0, new float[]{position[0], position[1], position[2], 1}, 0);
		result = VectorUtil.normalizePosition(result, 3,  result[3]);
		return result;
	}
	  
	public static float[] multiplyPosition(float[] matrix, float[] position) {
		float[] result = new float[4];
		Matrix.multiplyMV(result, 0, matrix, 0, position, 0);
		VectorUtil.normalizePosition(result, result, 3,  result[3]);
		return result;
	}
	
	public static void multiplyPosition(float[] matrix, float[] position, float[] res) {
		Matrix.multiplyMV(res, 0, matrix, 0, position, 0);
		VectorUtil.normalizePosition(res, res, 3,  res[3]);
	}
	  
	public static float[] multiplyMatrix(float[] matrix1, float[] matrix2) {
		float[] result = new float[16];
		Matrix.multiplyMM(result, 0, matrix1, 0, matrix2, 0);
		return result;
	}
			  
	public static void setInitStack(float[] currMatrix) {
		Matrix.setIdentityM(currMatrix, 0);
	}
  
	public static float[] getInitStack() {
		float[] currMatrix = new float[16];
		setInitStack(currMatrix);
		return currMatrix;
	}

	public static float[] get2DCameraMatrix() {
		return MathUtil.getInitStack();
	}
  
	public static void rotate2D(float[] currMatrix, float angle) {
		Matrix.rotateM(currMatrix, 0, angle, 0, 0, 1);
	}
  
	public static void translate2D(float[] currMatrix, float x, float y) {
		Matrix.translateM(currMatrix, 0, x, y, 0);
	}

	public static void normalMatrix(float[] vecMatrix, float[] resMatrx, float[] tmpMatrix) {
		if (Matrix.invertM(tmpMatrix, 0, vecMatrix, 0)) {
			Matrix.transposeM(resMatrx, 0, tmpMatrix, 0);
		}
	}

	public static float[] normalMatrix(float[] vecMatrix) {
		float[] result = new float[16];
		if (!Matrix.invertM(result, 0, vecMatrix, 0)) {
			result = vecMatrix.clone();
			return result;
		}
		
		Matrix.transposeM(result, 0, result, 0);
		return result;
	}
	
	public static float[] calculateAverage(ArrayList<float[]> floats, int n) {
		float[] result = VectorUtil.accumulationVectors(floats, n);
		result = VectorUtil.normalizePosition(result, n, floats.size());
		return result;
	}
	
	public static int getMax(ArrayList<Integer> list) {
		if (list.size() == 0) {
			return -1;
		}
		int max = list.get(0);
		int index = 0;
		
		int i = 0;
		for (Iterator<Integer> iterator = list.iterator(); iterator.hasNext();) {
			Integer tmp = iterator.next();
			if (i > 0 && tmp > max) {
				max = tmp;
				index = i;
			}
			i++;
		}
		return index;
	}
	
	public static int getMin(ArrayList<Integer> list) {
		if (list.size() == 0) {
			return -1;
		}
		int min = list.get(0);
		int index = 0;
		
		int i = 0;
		for (Iterator<Integer> iterator = list.iterator(); iterator.hasNext();) {
			Integer tmp = iterator.next();
			if (i > 0 && tmp < min) {
				min = tmp;
				index = i;
			}
			i++;
		}
		return index;
	}
	
	public static boolean isPolygonContain(int s, int e, float[] posArray, float x, float y) {
		int i, j;
		boolean isContain = false;
		for (i = s, j = e; i < e + 1; j = i++) {
			if ((posArray[i * 2 + 1] > y != posArray[j * 2 + 1] > y) && 
						(x < (posArray[j * 2] - posArray[i * 2]) * (y - posArray[i * 2 + 1]) 
								/ (posArray[j * 2 + 1] - posArray[i * 2 + 1]) + posArray[i * 2])) {
				isContain = !isContain;
			}
		}
		return isContain;
	}

	public static boolean isPolygonContain(int n, float[] posArray, float x, float y) {
		return isPolygonContain(0, n - 1, posArray, x, y);
	}
	
	//public void sort()
}
