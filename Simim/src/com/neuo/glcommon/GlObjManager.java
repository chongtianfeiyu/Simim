package com.neuo.glcommon;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;

import com.neuo.common.VectorUtil;

import android.content.res.Resources;
import android.opengl.GLES20;

public class GlObjManager {
	private static final String ObjPre = "obj/";
	public static final int NNNT = 1;
	public static final int NNHT = 2;
	public static final int HNNT = 3;
	public static final int HNHT = 4;
	public static final int Rectangle 		= 1;
	
	public static class ObjInfo{
		public SoftReference<GlPosAttribute> posAttReference;
	}
	
	private HashMap<String, ObjInfo> objAttrs = new HashMap<String, ObjInfo>();
	
	public GlObjManager() {
	}
	
	private static GlObjManager glObjManager;
	
	public static void release() {
		glObjManager = null;
	}
	
	public static GlObjManager getGlObjManager() {
		if (null == glObjManager) {
			glObjManager = new GlObjManager();
		}
		return glObjManager;
	}
	
	public GlPosAttribute getGlObjAttr(String objName, int objType, float ...arg) {
		ObjInfo objInfo;
		objInfo = objAttrs.get(objName);
		if (null == objInfo) {
			return calcuGlObjAttr(objName, objType, arg);
		}
		GlPosAttribute glPositionAttribute = objInfo.posAttReference.get();
		if (glPositionAttribute == null) {
			return calcuGlObjAttr(objName, objType, arg);
		}
		return glPositionAttribute;
	}
	
	public GlPosAttribute getGlObjAttr(String objName, int objType, Resources r,
													boolean isCalculateNormal, GlLoadObjUtil.CalcuTexFunc texFunc) {
		ObjInfo objInfo;
		objInfo = objAttrs.get(objName);
		if (null == objInfo){
			return loadGlObjAttr(objName, objType, isCalculateNormal, r, texFunc);
		}
		GlPosAttribute glPositionAttribute = objInfo.posAttReference.get();
		if (glPositionAttribute == null){
			return loadGlObjAttr(objName, objType, isCalculateNormal, r, texFunc);
		}
		return glPositionAttribute;
	//	objsAttr.put(objName, GlLoadObjUtil.loadObjNNNT(objFileName, r, isCalculateNormal));
	}

	public GlPosAttribute calcuGlObjAttr(String objName, int objType, float ...arg) {
		GlPosAttribute glPositionAttribute = new GlPosAttribute();
		ArrayList<Integer> alvIndex = new ArrayList<Integer>();
		ArrayList<Float> alv = new ArrayList<Float>();
		ArrayList<Float> alt = new ArrayList<Float>();
		switch (objType) {
		case Rectangle:
			boolean isTexRevert = false;
			if (arg.length > 0) {
				isTexRevert = arg[0] > 1.0f;
			}
			if (arg.length > 2) {
				VectorUtil.getRectangle2D(arg[1], arg[2], 0, alv, alt, alvIndex, isTexRevert, VectorUtil.TRIS);
			} else if (arg.length == 0 || arg.length == 1) {
				VectorUtil.getRectangle2D(1f, 1f, 0, alv, alt, alvIndex, isTexRevert, VectorUtil.TRIS);
			}
			glPositionAttribute.setPosModel(GLES20.GL_TRIANGLES);
			glPositionAttribute.setPosition(VectorUtil.cullVecCoor(alv, alvIndex));
			glPositionAttribute.setTexCoor(VectorUtil.cullTexCoor(alt, alvIndex));
			glPositionAttribute.setPositionNormal(VectorUtil.calcuNormal(alv, alvIndex));
			break;
		}
		ObjInfo objInfo = new ObjInfo();
		objInfo.posAttReference = new SoftReference<GlPosAttribute>(glPositionAttribute);
		objAttrs.put(objName, objInfo);
		return glPositionAttribute;
	}
	
	public GlPosAttribute loadGlObjAttr(String objName, int objType,
													boolean isCalcu, Resources r, GlLoadObjUtil.CalcuTexFunc texFunc) {
		GlPosAttribute glPositionAttribute;
		switch (objType) {
		case HNHT:
			glPositionAttribute = GlLoadObjUtil.loadObjHNHT(ObjPre + objName, r, texFunc);
			break;
		case NNHT:
			glPositionAttribute = GlLoadObjUtil.loadObjNNHT(ObjPre + objName, r, isCalcu, texFunc);
			break;
		case HNNT:
			glPositionAttribute = GlLoadObjUtil.loadObjHNNT(ObjPre + objName, r);
			break;
		case NNNT:
			glPositionAttribute = GlLoadObjUtil.loadObjNNNT(ObjPre + objName, r, isCalcu);
			break;
			default:
				glPositionAttribute = null;
		}
		if (null == glPositionAttribute)return null;
		ObjInfo objInfo = new ObjInfo();
		objInfo.posAttReference = new SoftReference<GlPosAttribute>(glPositionAttribute);
		objAttrs.put(objName, objInfo);
		return glPositionAttribute;
	}
}
