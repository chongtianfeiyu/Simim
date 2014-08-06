package com.neuo.common;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import android.util.SparseArray;

public class VectorUtil {
	public static float[] multiVectors(float[] v, float a0, int n) {
		float[] vector = new float[n];
		
		for (int i = 0; i < n; i++) {
			vector[i] = v[i] * a0;
		}
		return vector;
	}
	
	
	public static void multiVectors(float[] v, float a0, int n, float[] res) {
		for (int i = 0; i < n; i++) {
			res[i] = v[i] * a0;
		}
	}

	public static float[] accumulationVectors(ArrayList<float[]> vectors, int n) {
		float[] vector = new float[n];
		
		for (int i = 0; i < n; i++) {
			vector[i] = 0;
		}
		for (int i = 0; i < vectors.size(); i++) {
			for (int j = 0; j < n; j++) {
				vector[j] += vectors.get(i)[j];
			}
		}
		return vector;
	}
	
	public static void copy(float[] v, int n, float[] res) {
		for (int i = 0; i < n; i++) {
			res[i] = v[i];
		}
	}
	
	public static void set(float[] v, int n, float value) {
		for (int i = 0; i < n; i++) {
			v[i] = value;
		}
	}
	
	public static float[] addVector(float[] v1, float[] v2, int n) {
		float[] result = new float[n];
		for (int i = 0; i < n; i++) {
			result[i] = v2[i] + v1[i];
		}
		return result;
	}
	
	public static void addVector(float[] v1, float[] v2, int n, float[] res) {
		for (int i = 0; i < n; i++) {
			res[i] = v2[i] + v1[i];
		}
	}
	
	public static float[] delVector(float[] v1, float[] v2, int n) {
		float[] result = new float[n];
		for (int i = 0; i < n; i++) {
			result[i] = v2[i] - v1[i];
		}
		return result;
	}
	
	public static void delVector(float[] v1, float[] v2, int n, float[] res) {
		for (int i = 0; i < n; i++) {
			res[i] = v2[i] - v1[i];
		}
	}
	
	
	public static float module(float[] vec, int n) {
		double m = 0;
		for (int i = 0; i < n; i++) {
			m += vec[i] * vec[i];
		}
		return (float)Math.sqrt(m);
	}
	
	public static float[] calVerticalVec(float[] v1, float[] v2)
	{
		float[] result = crossVector(v2, v1);
		result = crossVector(v1, result);
		return result;
	}
	
	public static void normalizeVector(float[] vec, float[] res, int n)
	{
		float m = module(vec, n);
		if (0 == m)
		{
			m = 1;
		}
		
		for (int i = 0; i < n; i++)
		{
			res[i] = vec[i] / m;
		}
	}
	
	public static float[] normalizeVector(float[] vec, int n)
	{
		float m = module(vec, n);
		if (0 == m)
		{
			m = 1;
		}
		float[] norVec = new float[n];
		
		for (int i = 0; i < n; i++)
		{
			norVec[i] = vec[i] / m;
		}
		return norVec;
	}

	public static void normalizePosition(float[] vec, float[] res, int n, float module) {
		for (int i = 0; i < n; i++) {
			if (module == 0) {
				res[i] = Float.NEGATIVE_INFINITY;
			} else {
				res[i] = vec[i] / module;
			}
		}
	}

	public static float[] normalizePosition(float[] vec, int n, float module) {
		float[] result = new float[n];
		for (int i = 0; i < n; i++) {
			if (module == 0) {
				result[i] = Float.NEGATIVE_INFINITY;
			} else {
				result[i] = vec[i] / module;
			}
		}
		return result;
	}

	public static void crossVector(float[] vec1, float[] vec2, float[] res) {
		res[0] = vec1[1] * vec2[2] - vec1[2] * vec2[1];
		res[1] = vec1[2] * vec2[0] - vec1[0] * vec2[2];
		res[2] =  vec1[0] * vec2[1] - vec1[1] * vec2[0];
		normalizeVector(res, res, 3);
	}
	
	public static float[] crossVector(float[] vec1, float[] vec2) {
		float x = vec1[1] * vec2[2] - vec1[2] * vec2[1];
		float y = vec1[2] * vec2[0] - vec1[0] * vec2[2];
		float z = vec1[0] * vec2[1] - vec1[1] * vec2[0];
		return normalizeVector(new float[]{x, y, z}, 3);
	}
	
	public static float dotVector(float[] vec1, float[] vec2, int n)
	{
		float s = 0;
		for (int i = 0; i < n; i++)
		{
			s+= vec1[i] * vec2[i];
		}
		return s;
	}
	
	public static float[] cullTexCoor(ArrayList<Float> texOriCoor, ArrayList<Integer> texIndex)
	{
		float[] texCoor = new float[texIndex.size() * 2];
		int count = 0;
		for (int i : texIndex)
		{
			texCoor[count++] = texOriCoor.get(2 * i);
			texCoor[count++] = texOriCoor.get(2 * i + 1);
		}
		return texCoor;
	}
	
	public static float[] cullVecCoor(ArrayList<Float> vecOriCoor, ArrayList<Integer> vecIndex)
	{
		float[] vecCoor = new float[vecIndex.size() * 3];
		int count = 0;
		for (int i : vecIndex)
		{
			vecCoor[count++] = vecOriCoor.get(3 * i);
			vecCoor[count++] = vecOriCoor.get(3 * i + 1);
			vecCoor[count++] = vecOriCoor.get(3 * i + 2);
		}
		return vecCoor;
	}
	
	public static float[] getRetangle3DDirect(float width, float height, float depth, int drawModel)
	{
		ArrayList<Float> oriCoor = new ArrayList<Float>();
		ArrayList<Integer> index = new ArrayList<Integer>();
		getRectangle3D(width, height, depth, oriCoor, index, drawModel);
		return cullVecCoor(oriCoor, index);
	}
	
	public static float[] calcuNormal(ArrayList<Float> alv, ArrayList<Integer> alvIndex) {
		SparseArray<ArrayList<float[]>> hmn = new SparseArray<ArrayList<float[]>>();
		int[] index = new int[3];
		float[] vas = new float[3];
		float[] vbs = new float[3];
		for (int i = 0; i < alvIndex.size(); i += 3) {
			index[0] = alvIndex.get(i);index[1] = alvIndex.get(i + 1);index[2] = alvIndex.get(i + 2);
			float x0 = alv.get(index[0] * 3);
			float y0 = alv.get(index[0] * 3 + 1);
			float z0 = alv.get(index[0] * 3 + 2);
			
			float x1 = alv.get(index[1] * 3);
			float y1 = alv.get(index[1] * 3 + 1);
			float z1 = alv.get(index[1] * 3 + 2);
	
			float x2 = alv.get(index[2] * 3);
			float y2 = alv.get(index[2] * 3 + 1);
			float z2 = alv.get(index[2] * 3 + 2);
			
			vas[0] = x1 - x0;
			vas[1] = y1 - y0;
			vas[2] = z1 - z0;
			
			vbs[0] = x2 - x0;
			vbs[1] = y2 - y0;
			vbs[2] = z2 - z0;
			
			float[] vNormal = VectorUtil.crossVector(vas, vbs);
			for (int tempIndex : index)
			{
				ArrayList<float[]> hsn = hmn.get(tempIndex);
				if (hsn == null)
				{
					hsn = new ArrayList<float[]>();
				}
				hsn.add(vNormal);
				hmn.put(tempIndex, hsn);
			}
		}
		
		float[] alnResult = new float[alvIndex.size() * 3];
		int alnIndex = 0;
		SparseArray<float[]> normalsArray = new SparseArray<float[]>();
		for (Integer i:alvIndex) {
			float[] avergerFloat = normalsArray.get(i);
			if (null == avergerFloat) {
				avergerFloat = MathUtil.calculateAverage(hmn.get(i), 3);
				normalsArray.put(i, avergerFloat);
			}
			alnResult[alnIndex++] = avergerFloat[0];
			alnResult[alnIndex++] = avergerFloat[1];
			alnResult[alnIndex++] = avergerFloat[2];
		}
		return alnResult;
	}
	
	public static final int LINES 	= 0;
	public static final int TRIS 	= 1;
	public static void getRectangle3D(float width, float height, float depth,
										ArrayList<Float> rectOriCoor,
										ArrayList<Integer> rectIndex,
										int drawModel)
	{
		rectIndex.clear();
		rectOriCoor.clear();
		
		rectOriCoor.add(-width / 2);rectOriCoor.add(-height / 2);rectOriCoor.add(depth / 2);
		rectOriCoor.add(-width / 2);rectOriCoor.add(height / 2);rectOriCoor.add(depth / 2);
		rectOriCoor.add(width / 2);rectOriCoor.add(height / 2);rectOriCoor.add(depth / 2);
		rectOriCoor.add(width / 2);rectOriCoor.add(-height / 2);rectOriCoor.add(depth / 2);
		
		rectOriCoor.add(-width / 2);rectOriCoor.add(-height / 2);rectOriCoor.add(-depth / 2);
		rectOriCoor.add(-width / 2);rectOriCoor.add(height / 2);rectOriCoor.add(-depth / 2);
		rectOriCoor.add(width / 2);rectOriCoor.add(height / 2);rectOriCoor.add(-depth / 2);
		rectOriCoor.add(width / 2);rectOriCoor.add(-height / 2);rectOriCoor.add(-depth / 2);
		
		switch (drawModel)
		{
		case LINES:
			rectIndex.add(0);rectIndex.add(1);rectIndex.add(1);rectIndex.add(2);
			rectIndex.add(2);rectIndex.add(3);rectIndex.add(3);rectIndex.add(0);
			rectIndex.add(4);rectIndex.add(5);rectIndex.add(5);rectIndex.add(6);
			rectIndex.add(6);rectIndex.add(7);rectIndex.add(7);rectIndex.add(4);
			rectIndex.add(0);rectIndex.add(4);rectIndex.add(1);rectIndex.add(5);
			rectIndex.add(2);rectIndex.add(6);rectIndex.add(3);rectIndex.add(7);
			break;
			default:
		}
	}
	
	public static void getRectTexCoor(int drawModel, float left, float top, float right, float bottom, float[] texCoor) {
		switch (drawModel) {
		case TRIS:
			texCoor[0] = left;texCoor[1] = top;
			texCoor[2] = left;texCoor[3] = bottom;
			texCoor[4] = right;texCoor[5] = bottom;
			texCoor[6] = left;texCoor[7] = top;
			texCoor[8] = right;texCoor[9] = bottom;
			texCoor[10] = right;texCoor[11] = top;
			break;

		default:
			break;
		}
	}
	
	public static float[] getRectTexArr(int drawModel) {
		switch (drawModel) {
		case TRIS:
			return new float[12];
		default:
			break;
		}
		return null;
	}
	
	public static void getRectangle2D(float width, float height, float z,
										ArrayList<Float> rectOriCoor, 
										ArrayList<Float> rectTexCoor,
										ArrayList<Integer> rectIndex,
										boolean texRevert,
										int drawModel)
	{
		rectIndex.clear();
		rectOriCoor.clear();
		rectTexCoor.clear();
		
		rectOriCoor.add(-width / 2);rectOriCoor.add(-height / 2);rectOriCoor.add(z);
		rectOriCoor.add(-width / 2);rectOriCoor.add(height / 2);rectOriCoor.add(z);
		rectOriCoor.add(width / 2);rectOriCoor.add(height / 2);rectOriCoor.add(z);
		rectOriCoor.add(width / 2);rectOriCoor.add(-height / 2);rectOriCoor.add(z);
		if (!texRevert) {
			rectTexCoor.add(0f);rectTexCoor.add(1f);
			rectTexCoor.add(0f);rectTexCoor.add(0f);
			rectTexCoor.add(1f);rectTexCoor.add(0f);
			rectTexCoor.add(1f);rectTexCoor.add(1f);
		} else {
			rectTexCoor.add(0f);rectTexCoor.add(0f);
			rectTexCoor.add(0f);rectTexCoor.add(1f);
			rectTexCoor.add(1f);rectTexCoor.add(1f);
			rectTexCoor.add(1f);rectTexCoor.add(0f);
		}
		
		switch (drawModel)
		{
		case LINES:
			rectIndex.add(0);rectIndex.add(1);
			rectIndex.add(1);rectIndex.add(2);
			rectIndex.add(2);rectIndex.add(3);
			rectIndex.add(3);rectIndex.add(0);
			break;
		case TRIS:
			rectIndex.add(1);rectIndex.add(0);rectIndex.add(3);
			rectIndex.add(1);rectIndex.add(3);rectIndex.add(2);
			default:
		}
	}
	
	public static float[] getRegularFaceNDirect(int N, float radius, int drawModel)
	{
		ArrayList<Float> oriCoor = new ArrayList<Float>();
		ArrayList<Integer> index = new ArrayList<Integer>();
		getRegularFaceN(N, radius, oriCoor, index, drawModel);
		return cullVecCoor(oriCoor, index);
	}

	public static float getRegularFaceN(int N, float radius,
												ArrayList<Float> regularOriCoor,
												ArrayList<Integer> regularIndex,
												int drawModel)
	{
		float length;
		switch (N) {
		case 20: 
			length = getRegularFace20(radius, regularOriCoor, regularIndex, drawModel);
			break;
			default:
				length = getRegularFace20(radius, regularOriCoor, regularIndex, drawModel);
		}
		return length;
	}
	
	public static float[] getRegularFace20Direct(float radius, int drawModel)
	{
		ArrayList<Float> oriCoor = new ArrayList<Float>();
		ArrayList<Integer> index = new ArrayList<Integer>();
		getRegularFace20(radius, oriCoor, index, drawModel);
		return cullVecCoor(oriCoor, index);
	}

	public static float getRegularFace20(float radius,
												ArrayList<Float> regularOriCoor,
												ArrayList<Integer> regularIndex,
												int drawModel)
	{
		regularIndex.clear();
		regularOriCoor.clear();
		
		float m = (float)Math.sqrt(50 - 10 * Math.sqrt(5)) / 10 * radius;
		float n = (float)Math.sqrt(50 + 10 * Math.sqrt(5)) / 10 * radius;

		float length = 2 * m;
		
		regularOriCoor.add(-m);regularOriCoor.add(0f);regularOriCoor.add(n);
		regularOriCoor.add(m);regularOriCoor.add(0f);regularOriCoor.add(n);
		regularOriCoor.add(-m);regularOriCoor.add(0f);regularOriCoor.add(-n);
		regularOriCoor.add(m);regularOriCoor.add(0f);regularOriCoor.add(-n);
		regularOriCoor.add(0f);regularOriCoor.add(n);regularOriCoor.add(m);
		regularOriCoor.add(0f);regularOriCoor.add(n);regularOriCoor.add(-m);
		regularOriCoor.add(0f);regularOriCoor.add(-n);regularOriCoor.add(m);
		regularOriCoor.add(0f);regularOriCoor.add(-n);regularOriCoor.add(-m);
		regularOriCoor.add(n);regularOriCoor.add(m);regularOriCoor.add(0f);
		regularOriCoor.add(-n);regularOriCoor.add(m);regularOriCoor.add(0f);
		regularOriCoor.add(n);regularOriCoor.add(-m);regularOriCoor.add(0f);
		regularOriCoor.add(-n);regularOriCoor.add(-m);regularOriCoor.add(0f);
		
		switch(drawModel)
		{
		case LINES:
			regularIndex.add(1);regularIndex.add(4);
			regularIndex.add(4);regularIndex.add(0);
			regularIndex.add(0);regularIndex.add(1);
			regularIndex.add(4);regularIndex.add(9);
			regularIndex.add(9);regularIndex.add(0);
			regularIndex.add(4);regularIndex.add(5);
			regularIndex.add(5);regularIndex.add(9);
			regularIndex.add(8);regularIndex.add(5);
			regularIndex.add(4);regularIndex.add(8);
			regularIndex.add(1);regularIndex.add(8);
			regularIndex.add(1);regularIndex.add(10);
			regularIndex.add(10);regularIndex.add(8);
			regularIndex.add(10);regularIndex.add(3);
			regularIndex.add(3);regularIndex.add(8);
			regularIndex.add(3);regularIndex.add(5);
			regularIndex.add(3);regularIndex.add(2);
			regularIndex.add(2);regularIndex.add(5);
			regularIndex.add(3);regularIndex.add(7);
			regularIndex.add(7);regularIndex.add(2);
			regularIndex.add(10);regularIndex.add(7);
			regularIndex.add(10);regularIndex.add(6);
			regularIndex.add(6);regularIndex.add(7);
			regularIndex.add(6);regularIndex.add(11);
			regularIndex.add(11);regularIndex.add(7);
			regularIndex.add(6);regularIndex.add(0);
			regularIndex.add(0);regularIndex.add(11);
			regularIndex.add(6);regularIndex.add(1);
			regularIndex.add(9);regularIndex.add(11);
			regularIndex.add(2);regularIndex.add(11);
			regularIndex.add(9);regularIndex.add(2);
			break;
			default:
		}
		
		return length;
	}
	
	/*
	public static void getGeometricSphereBy20(float radius, int n, 
												ArrayList<Float> sphereOriCoor,
												ArrayList<Integer> sphereIndex,
												int drawModel)
	{
		sphereIndex.clear();
		sphereOriCoor.clear();
		
		ArrayList<Float> regularOriCoor = new ArrayList<Float>();
		ArrayList<Integer> regularIndex = new ArrayList<Integer>();
		
		VectorUtil.getRegularFace20(radius, regularOriCoor, regularIndex, GLES20.GL_TRIANGLES);
		
		for (int i = 0; i < 20; i++)
		{
			for (int j = 0; j <= n; j ++) 
			{
				float a0[] = {regularOriCoor.get(regularIndex.get(i * 3)), regularOriCoor.get(regularIndex.get(i * 3)) +1, regularOriCoor.get(regularIndex.get(i * 3)) + 2};
				float a1[] = {regularOriCoor.get(regularIndex.get(i * 3 + 1)), regularOriCoor.get(regularIndex.get(i * 3 + 1)) +1, regularOriCoor.get(regularIndex.get(i * 3 + 1)) + 2};
				float a2[] = {regularOriCoor.get(regularIndex.get(i * 3 + 2)), regularOriCoor.get(regularIndex.get(i * 3 + 2)) +1, regularOriCoor.get(regularIndex.get(i * 3 + 2)) + 2};
	
				float[] n1 = getArc(a0, a1, n, j);
				float[] n2 = getArc(a0, a2, n, j);
				
				for (int l = 0; l <= n; l++)
				{
					float[] n3 = getArc(n1, n2, n, l);
					sphereOriCoor.add(n3[0]);
					sphereOriCoor.add(n3[1]);
					sphereOriCoor.add(n3[2]);
				}
			}
		}
		switch (drawModel)
		{
		case GLES20.GL_LINES:
			int num = sphereOriCoor.size() / (3 * 20);
			for (int i = 0; i < 20; i++)
			{
				int startIndex = num * i;
				for (int j = 0; j < n; j++)
				{
					for (int l = 0; l < n; l++)
					{
						sphereIndex.add(startIndex + j *);
					}
				}
			}
			break;
		case GLES20.GL_TRIANGLES:
			default:
		}

	}
	
	public static float[] getArc(float[] a0, float[] a1, int N, int n)
	{
		float[] pos = new float[3];
		if (0 == n)
		{
			pos = a0.clone();
		}
		else if (N == n)
		{
			pos = a1.clone();
		}
		else
		{
			float[] delVec = delVector(a0, a1, 3);
			float length = module(delVec, 3);
			float radius = module(a0, 3);
			float[] lenVec = normalizeVector(delVec, 3);
			float lengthInterval = length * n / N;
			lenVec = multiVectors(normalizeVector(lenVec, 3), lengthInterval, 3);
			pos = addVector(a0, lenVec, 3);
			pos = multiVectors(normalizeVector(pos, 3), radius, 3);
		}
		
		return pos;
	}
//	public static float[] get
	
	*/
	
	public static float[] getSphereDirect(float radius, int n,
									float left, float right, float bottom, float top,
									boolean isLink, int drawModel)
	{
		ArrayList<Float> oriCoor = new ArrayList<Float>();
		ArrayList<Integer> index = new ArrayList<Integer>();
		getSphere(radius, n, left, right, bottom, top, isLink, oriCoor, index, drawModel);
		return cullVecCoor(oriCoor, index);
	}

	public static void getSphere(float radius, int n,
									float left, float right, float bottom, float top,
									boolean isLink,
									ArrayList<Float> sphereOriCoor,
									ArrayList<Integer> sphereIndex,
									int drawModel)
	{
		sphereIndex.clear();
		sphereOriCoor.clear();
		
		float angleInterval = 360f / n;
		
		int countS = 0;
		int countT = 0;
		for (float angleS = bottom; angleS <= top; angleS += angleInterval) {
			countS ++;
			countT = 0;
			float zb = radius * (float)(Math.sin(Math.toRadians(angleS)));
			float xyb = radius * (float)(Math.cos(Math.toRadians(angleS)));
			for (float angleT = left; angleT < right; angleT += angleInterval) {
				countT++;
				
				float xbp = xyb * (float)(Math.cos(Math.toRadians(angleT)));
				float ybp = xyb * (float)(Math.sin(Math.toRadians(angleT)));
				
				sphereOriCoor.add(xbp);
				sphereOriCoor.add(ybp);
				sphereOriCoor.add(zb);
			}
			
		}
		
		if (0 == countS || 0 == countT) {
			return;
		}
		
		switch (drawModel) {
		case LINES:
			for (int i = 0; i < countS; i++) {
				for (int j = 1; j < countT; j++) {
					sphereIndex.add(i * countT + j - 1);
					sphereIndex.add(i * countT + j);
				}
				
				if (countT > 1 && isLink) {
					sphereIndex.add(i * countT + countT - 1);
					sphereIndex.add(i * countT);
				}
				
			}
			
			for (int j = 0; j < countT; j++) {
				for (int i = 1; i < countS; i++) {
					sphereIndex.add((i - 1) * countT + j);
					sphereIndex.add(i * countT + j);
				}
			}
			break;
		case TRIS:
			for (int i = 0; i < countS - 1; i++) {
				for (int j = 1; j < countT; j++) {
					sphereIndex.add((i + 1) * countT + j - 1);
					sphereIndex.add(i * countT + j - 1);
					sphereIndex.add(i * countT + j);
					
					sphereIndex.add((i + 1) * countT + j - 1);
					sphereIndex.add(i * countT + j);
					sphereIndex.add((i + 1) * countT + j);
				}
				
				if (countT > 1 && isLink) {
					sphereIndex.add((i  + 1)* countT + countT - 1);
					sphereIndex.add(i * countT + countT - 1);
					sphereIndex.add(i * countT);

					sphereIndex.add((i  + 1)* countT + countT - 1);
					sphereIndex.add(i * countT);
					sphereIndex.add((i + 1) * countT);
				}
			}
			
			break;
		default:
		}
	}
	
	public static float[] getCylinderDirect(float bottom, float bRadius,
										float top, float tRadius, int n,
										float left, float right, boolean isLink,
										boolean isCenter, int drawModel) {
		ArrayList<Float> oriCoor = new ArrayList<Float>();
		ArrayList<Integer> index = new ArrayList<Integer>();
		getCylinder(bottom, bRadius, top, tRadius, n, left, right, isLink, isCenter, oriCoor, index, drawModel);
		return cullVecCoor(oriCoor, index);
	}

	public static void getCylinder(float bottom, float bRadius,
										float top, float tRadius, int n,
										float left, float right, boolean isLink,
										boolean isCenter,
										ArrayList<Float> cylinderOriCoor,
										ArrayList<Integer> cylinderIndex,
										int drawModel) {
		cylinderIndex.clear();
		cylinderOriCoor.clear();
		
		ArrayList<Float> topCircleOriCoor = new ArrayList<Float>();
		ArrayList<Integer> topCircleIndex = new ArrayList<Integer>();
		
		getCircle(tRadius, top, top, n, left, right, isLink, isCenter, topCircleOriCoor, topCircleIndex, drawModel);
		
		ArrayList<Float> bottomCircleOriCoor = new ArrayList<Float>();
		ArrayList<Integer> bottomCircleIndex = new ArrayList<Integer>();
		
		getCircle(bRadius, bottom, bottom, n, left, right, isLink, isCenter, bottomCircleOriCoor, bottomCircleIndex, drawModel);
		
		cylinderOriCoor.addAll(topCircleOriCoor);
		cylinderOriCoor.addAll(bottomCircleOriCoor);
		
		int vCount = (topCircleOriCoor.size() + 1) / 3;
		cylinderIndex.addAll(topCircleIndex);
		
		for (int i = 0; i < bottomCircleIndex.size(); i++) {
			int tmpIndex = bottomCircleIndex.get(i);
			cylinderIndex.add(tmpIndex + vCount);
		}
		
		switch (drawModel) {
		case LINES:
			for (int i = 1; i < vCount; i++) {
				cylinderIndex.add(i);
				cylinderIndex.add(i + vCount);
			}
			break;
		default:
		}
		
	}
	
	public static float[] getHyperbolaDirect(float left, float right, float z, int n,
										float e, float p, boolean isLeft,
										float[] focalPoint, boolean isLink,
										boolean isCenter, int drawModel) {
		ArrayList<Float> oriCoor = new ArrayList<Float>();
		ArrayList<Integer> index = new ArrayList<Integer>();
		getHyperbola(left, right, z, n, e, p, isLeft, focalPoint, isLink, isCenter, oriCoor, index, drawModel);
		return cullVecCoor(oriCoor, index);
		
	}
	
	public static void getHyperbola(float left, float right, float z, int n, 
										float e, float p, boolean isLeft,
										float[] focalPoint, boolean isLink,
										boolean isCenter,
										ArrayList<Float> HyperbolaOriCoor,
										ArrayList<Integer> HyperbolaIndex,
										int drawModel) {
		HyperbolaIndex.clear();
		HyperbolaOriCoor.clear();
		
		HyperbolaOriCoor.add(focalPoint[0]);
		HyperbolaOriCoor.add(focalPoint[1]);
		HyperbolaOriCoor.add(focalPoint[2]);
		
		float angleInterval = 360f / n;
		for (float angle = left; angle < right; angle += angleInterval) {
			float[] XYR = MathUtil.getConicXYR(angle, e, p, isLeft);
			if (XYR[2] < 0) {
				continue;
			}
			HyperbolaOriCoor.add(XYR[0] + focalPoint[0]);
			HyperbolaOriCoor.add(XYR[1] + focalPoint[1]);
			HyperbolaOriCoor.add(z);
		}
		
		switch (drawModel) {
		case LINES:
			for (int i = 2; i < (HyperbolaOriCoor.size() + 1) / 3; i++) {
				HyperbolaIndex.add(i - 1);
				HyperbolaIndex.add(i);
				if (isCenter) {
					HyperbolaIndex.add(0);
					HyperbolaIndex.add(i);
				}
			}
			if (HyperbolaIndex.size() > 0) {
				if (isLink) {
					HyperbolaIndex.add((HyperbolaOriCoor.size() + 1) / 3 -1);
					HyperbolaIndex.add(1);
				}
				if (isCenter) {
					HyperbolaIndex.add(0);
					HyperbolaIndex.add(1);
				}
			}
			break;
		case TRIS:
			for (int i = 2; i < (HyperbolaOriCoor.size() + 1) / 3; i++) {
				HyperbolaIndex.add(0);
				HyperbolaIndex.add(i - 1);
				HyperbolaIndex.add(i);
			}
			if (HyperbolaIndex.size() > 0) {
				HyperbolaIndex.add(0);
				HyperbolaIndex.add((HyperbolaOriCoor.size() + 1) / 3 -1);
				HyperbolaIndex.add(1);
			}
		default:
		}
	}
	

	public static float[] getCirCleDirect(float radius, float z, float z0, int n,
									float left, float right, boolean isLink,
									boolean isCenter, int drawModel)
	{
		ArrayList<Float> oriCoor = new ArrayList<Float>();
		ArrayList<Integer> index = new ArrayList<Integer>();
		getCircle(radius, z, z0, n, left, right, isLink, isCenter, oriCoor, index, drawModel);
		return cullVecCoor(oriCoor, index);
	}
							
	public static void getCircle(float radius, float z, float z0, int n,
									float left, float right, boolean isLink,
									boolean isCenter,
									ArrayList<Float> circleOriCoor,
									ArrayList<Integer> circleIndex,
									int drawModel)
	{
		circleIndex.clear();
		circleOriCoor.clear();
		
		circleOriCoor.add(0f);circleOriCoor.add(0f);circleOriCoor.add(z0);
		
		float angleInterval = 360f / n;
		for (float angle = left; angle < right; angle += angleInterval)
		{
			float x = (float) (radius * Math.cos(Math.toRadians(angle)));
			float y = (float) (radius * Math.sin(Math.toRadians(angle)));
			circleOriCoor.add(x);circleOriCoor.add(y);circleOriCoor.add(z);
		}
		
		switch (drawModel) {
		case LINES:
			for (int i = 2; i < (circleOriCoor.size() + 1) / 3; i ++)
			{
				circleIndex.add(i - 1);
				circleIndex.add(i);
				if (isCenter)
				{
					circleIndex.add(0);
					circleIndex.add(i);
				}
			}
			if (circleIndex.size() > 0)
			{
				if (isLink)
				{
					circleIndex.add((circleOriCoor.size() + 1) / 3 - 1);
					circleIndex.add(1);
				}
				if (isCenter)
				{
					circleIndex.add(0);
					circleIndex.add(1);
				}
			}
			
			break;
		case TRIS:
			for (int i = 2; i < (circleOriCoor.size() + 1) / 3; i ++)
			{
				circleIndex.add(0);
				circleIndex.add(i - 1);
				circleIndex.add(i);
			}
			if (circleIndex.size() > 0)
			{
				circleIndex.add(0);
				circleIndex.add((circleOriCoor.size() + 1) / 3 - 1);
				circleIndex.add(1);
			}
			break;
		default:
		}
	}
	
	public static FloatBuffer convertToBuffer(float[] floats)
	{
		FloatBuffer floatBuffer;
	    ByteBuffer vbb = ByteBuffer.allocateDirect(floats.length * 4);
	    vbb.order(ByteOrder.nativeOrder());
	    floatBuffer = vbb.asFloatBuffer();
	    floatBuffer.put(floats);
	    floatBuffer.position(0);
	    return floatBuffer;
	}
	
	public static FloatBuffer convertToBuffer(FloatBuffer floatsBuffer)
	{
		ByteBuffer vbb = ByteBuffer.allocateDirect(floatsBuffer.capacity());
    	FloatBuffer resultBuffer = vbb.asFloatBuffer();
    	resultBuffer.put(floatsBuffer);
    	resultBuffer.position(0);
    	return resultBuffer;
	}
}
