package com.neuo.glcommon;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.neuo.common.MathUtil;
import com.neuo.common.VectorUtil;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.util.SparseArray;

public class GlLoadObjUtil {
	public static class CalcuTexFunc {
		public float calcuY(float Y) {
			return Y;
		}
		
		public float calcuX(float X) {
			return X;
		}
 	}
	
	public static class Divide2Func extends CalcuTexFunc {
		public float calcuY(float Y) {
			return Y / 2.0f;
		}
		
		public float calcuX(float X) {
			return X / 2.0f;
		}
	}
	
	public static class RevertYFunc extends CalcuTexFunc {
		public float calcuY(float Y) {
			return 1.0f - Y;
		}
		
		public float calcuX(float X) {
			return X;
		}
	}

	public static GlPosAttribute loadObjNNNT(String objName, Resources r, boolean isCalculateNormal) {
		ArrayList<Float> alv = new ArrayList<Float>();
		ArrayList<Integer> alvIndex = new ArrayList<Integer>();
		SparseArray<ArrayList<float[]>> hmn = new SparseArray<ArrayList<float[]>>();
		GlPosAttribute result = new GlPosAttribute();
		float[] vas = new float[3];
		float[] vbs = new float[3];
		int[] index = new int[3];
		try {
			InputStream inputStream = r.getAssets().open(objName);
			InputStreamReader isReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(isReader);
			String temps = null;
			while ((temps = bufferedReader.readLine()) != null) {
				String[] tempsa = temps.split("[ ]+");
				if (tempsa[0].trim().equals("v")) {
					alv.add(Float.parseFloat(tempsa[1]));
					alv.add(Float.parseFloat(tempsa[2]));
					alv.add(Float.parseFloat(tempsa[3]));
				} else if (tempsa[0].trim().equals("f")) {
					index[0] = Integer.parseInt(tempsa[1].split("/")[0]) - 1;
					index[1] = Integer.parseInt(tempsa[2].split("/")[0]) - 1;
					index[2] = Integer.parseInt(tempsa[3].split("/")[0]) - 1;
					alvIndex.add(index[0]);
					alvIndex.add(index[1]);
					alvIndex.add(index[2]);

					if (isCalculateNormal) {
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
						for (int tempIndex : index) {
							ArrayList<float[]> hsn = hmn.get(tempIndex);
							if (hsn == null) {
								hsn = new ArrayList<float[]>();
							}
							hsn.add(vNormal);
							hmn.put(tempIndex, hsn);
						}
					}
				}
			}
			bufferedReader.close();
			isReader.close();
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("glLoad obj file fail, file name : " + objName);
		}
		result.setPosition(VectorUtil.cullVecCoor(alv, alvIndex) );
		result.setPosModel(GLES20.GL_TRIANGLES);
		if (isCalculateNormal) {
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
			result.setPositionNormal(alnResult);
		}
		return result;
	}

	public static GlPosAttribute loadObjNNHT(String objName, Resources r, boolean isCalculateNormal, CalcuTexFunc texFunc) {
		ArrayList<Float> alv = new ArrayList<Float>();
		ArrayList<Integer> alvIndex = new ArrayList<Integer>();
		ArrayList<Float> alt = new ArrayList<Float>();
		ArrayList<Integer> altIndex = new ArrayList<Integer>();
		SparseArray<ArrayList<float[]>> hmn = new SparseArray<ArrayList<float[]>>();
		GlPosAttribute result = new GlPosAttribute();
		float[] vas = new float[3];
		float[] vbs = new float[3];
		int[] index = new int[3];
		try {
			InputStream inputStream = r.getAssets().open(objName);
			InputStreamReader isReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(isReader);
			String temps = null;
			
			while ((temps = bufferedReader.readLine()) != null) {
				String[] tempsa = temps.split("[ ]+");
				if (tempsa[0].trim().equals("v")) {
					alv.add(Float.parseFloat(tempsa[1]));
					alv.add(Float.parseFloat(tempsa[2]));
					alv.add(Float.parseFloat(tempsa[3]));
				} else if (tempsa[0].trim().equals("vt")) {
					if (texFunc == null) {
						alt.add(Float.parseFloat(tempsa[1]));
						alt.add(Float.parseFloat(tempsa[2]));
					} else {
						alt.add(texFunc.calcuX(Float.parseFloat(tempsa[1])));
						alt.add(texFunc.calcuY(Float.parseFloat(tempsa[2])));
					}
				} else if (tempsa[0].trim().equals("f")) {
					index[0] = Integer.parseInt(tempsa[1].split("/")[0]) - 1;
					index[1] = Integer.parseInt(tempsa[2].split("/")[0]) - 1;
					index[2] = Integer.parseInt(tempsa[3].split("/")[0]) - 1;
					alvIndex.add(index[0]);
					alvIndex.add(index[1]);
					alvIndex.add(index[2]);

					int indexTex = Integer.parseInt(tempsa[1].split("/")[1]) - 1;
					altIndex.add(indexTex);
					indexTex = Integer.parseInt(tempsa[2].split("/")[1]) - 1;
					altIndex.add(indexTex);
					indexTex = Integer.parseInt(tempsa[3].split("/")[1]) - 1;
					altIndex.add(indexTex);
					
					if (isCalculateNormal) {
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
						for (int tempIndex : index) {
							ArrayList<float[]> hsn = hmn.get(tempIndex);
							if (hsn == null) {
								hsn = new ArrayList<float[]>();
							}
							hsn.add(vNormal);
							hmn.put(tempIndex, hsn);
						}
					}
				}
			}
			bufferedReader.close();
			isReader.close();
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("glLoad obj file fail, file name : " + objName);
		}
		result.setPosition(VectorUtil.cullVecCoor(alv, alvIndex));
		result.setTexCoor(VectorUtil.cullTexCoor(alt, altIndex));
		result.setPosModel(GLES20.GL_TRIANGLES);
		if (isCalculateNormal) {
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
			result.setPositionNormal(alnResult);
		}
		return result;
	}
	
	public static GlPosAttribute loadObjHNNT(String objName, Resources r) {
		ArrayList<Float> alv = new ArrayList<Float>();
		ArrayList<Integer> alvIndex = new ArrayList<Integer>();
		ArrayList<Float> aln = new ArrayList<Float>();
		ArrayList<Integer> alnIndex = new ArrayList<Integer>();
		GlPosAttribute result = new GlPosAttribute();
		int[] index = new int[3];
		try {
			InputStream inputStream = r.getAssets().open(objName);
			InputStreamReader isReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(isReader);
			String temps = null;
			
			while ((temps = bufferedReader.readLine()) != null) {
				String[] tempsa = temps.split("[ ]+");
				if (tempsa[0].trim().equals("v")) {
					alv.add(Float.parseFloat(tempsa[1]));
					alv.add(Float.parseFloat(tempsa[2]));
					alv.add(Float.parseFloat(tempsa[3]));
				} else if (tempsa[0].trim().equals("vn")) {
					aln.add(Float.parseFloat(tempsa[1]));
					aln.add(Float.parseFloat(tempsa[2]));
					aln.add(Float.parseFloat(tempsa[3]));
				} else if (tempsa[0].trim().equals("f")) {
					index[0] = Integer.parseInt(tempsa[1].split("/")[0]) - 1;
					index[1] = Integer.parseInt(tempsa[2].split("/")[0]) - 1;
					index[2] = Integer.parseInt(tempsa[3].split("/")[0]) - 1;
					alvIndex.add(index[0]);
					alvIndex.add(index[1]);
					alvIndex.add(index[2]);

					int indexTex = Integer.parseInt(tempsa[1].split("/")[2]) - 1;
					alnIndex.add(indexTex);
					indexTex = Integer.parseInt(tempsa[2].split("/")[2]) - 1;
					alnIndex.add(indexTex);
					indexTex = Integer.parseInt(tempsa[3].split("/")[2]) - 1;
					alnIndex.add(indexTex);
					
				}
			}
			bufferedReader.close();
			isReader.close();
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("glLoad obj file fail, file name : " + objName);
		}
		result.setPosition(VectorUtil.cullVecCoor(alv, alvIndex));
		result.setPositionNormal(VectorUtil.cullVecCoor(aln, alnIndex));
		result.setPosModel(GLES20.GL_TRIANGLES);
		return result;
	}

	public static GlPosAttribute loadObjHNHT(String objName, Resources r, CalcuTexFunc texFunc) {
		ArrayList<Float> alv = new ArrayList<Float>();
		ArrayList<Integer> alvIndex = new ArrayList<Integer>();
		ArrayList<Float> alt = new ArrayList<Float>();
		ArrayList<Integer> altIndex = new ArrayList<Integer>();
		ArrayList<Float> aln = new ArrayList<Float>();
		ArrayList<Integer> alnIndex = new ArrayList<Integer>();
		GlPosAttribute result = new GlPosAttribute();
		int[] index = new int[3];
		try {
			InputStream inputStream = r.getAssets().open(objName);
			InputStreamReader isReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(isReader);
			String temps = null;
			
			while ((temps = bufferedReader.readLine()) != null) {
				String[] tempsa = temps.split("[ ]+");
				if (tempsa[0].trim().equals("v")) {
					alv.add(Float.parseFloat(tempsa[1]));
					alv.add(Float.parseFloat(tempsa[2]));
					alv.add(Float.parseFloat(tempsa[3]));
				}
				else if (tempsa[0].trim().equals("vt")) {
					if (texFunc == null) {
						alt.add(Float.parseFloat(tempsa[1]));
						alt.add(Float.parseFloat(tempsa[2]));
					} else {
						alt.add(texFunc.calcuX(Float.parseFloat(tempsa[1])));
						alt.add(texFunc.calcuY(Float.parseFloat(tempsa[2])));
					}
				}
				else if (tempsa[0].trim().equals("vn")) {
					aln.add(Float.parseFloat(tempsa[1]));
					aln.add(Float.parseFloat(tempsa[2]));
					aln.add(Float.parseFloat(tempsa[3]));
				}
				else if (tempsa[0].trim().equals("f")) {
					index[0] = Integer.parseInt(tempsa[1].split("/")[0]) - 1;
					index[1] = Integer.parseInt(tempsa[2].split("/")[0]) - 1;
					index[2] = Integer.parseInt(tempsa[3].split("/")[0]) - 1;
					alvIndex.add(index[0]);
					alvIndex.add(index[1]);
					alvIndex.add(index[2]);

					int indexTex = Integer.parseInt(tempsa[1].split("/")[1]) - 1;
					altIndex.add(indexTex);
					indexTex = Integer.parseInt(tempsa[2].split("/")[1]) - 1;
					altIndex.add(indexTex);
					indexTex = Integer.parseInt(tempsa[3].split("/")[1]) - 1;
					altIndex.add(indexTex);
					
					int indexNor = Integer.parseInt(tempsa[1].split("/")[2]) - 1;
					alnIndex.add(indexNor);
					indexNor = Integer.parseInt(tempsa[2].split("/")[2]) - 1;
					alnIndex.add(indexNor);
					indexNor = Integer.parseInt(tempsa[3].split("/")[2]) - 1;
					alnIndex.add(indexNor);
				}
			}
			bufferedReader.close();
			isReader.close();
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("glLoad obj file fail, file name : " + objName);
		}
		result.setPosition(VectorUtil.cullVecCoor(alv, alvIndex));
		result.setTexCoor(VectorUtil.cullTexCoor(alt, altIndex));
		result.setPositionNormal(VectorUtil.cullVecCoor(aln, alnIndex));
		result.setPosModel(GLES20.GL_TRIANGLES);
		return result;
	}
}
