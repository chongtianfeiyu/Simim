package com.neuo.common;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class SimCrypto {
	
	public static byte[] encodeFile(byte[] fileData, int start, int end) {
		for (int i = start; i < fileData.length && i < end; i++) {
			fileData[i] = (byte) ~(fileData[i]);
		}
		return fileData;
	}
	
	public static byte[] decodeFile(byte[] fileData, int start, int end) {
		return encodeFile(fileData, start, end);
	}
	
	public static byte[] generateKey(String passwd) {
		byte[] keyStart = null;
		KeyGenerator kgen = null;
		SecureRandom sr = null;
		try {
			keyStart = passwd.getBytes("UTF-8");
			kgen = KeyGenerator.getInstance("AES");
			sr = SecureRandom.getInstance("SHA1PRNG");
		} catch (Exception e) {
			throw new RuntimeException("generate keyStart fail.");
		}
		sr.setSeed(keyStart);
		kgen.init(128, sr);
		SecretKey skey = kgen.generateKey();
		return skey.getEncoded();
	}
	
	public static byte[] encodeFile(byte[] key, byte[] fileData) {
		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = null;
        byte[] encrypted = null;
		try {
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			encrypted = cipher.doFinal(fileData);
		} catch (Exception e) {
			throw new RuntimeException("encode fail");
		}
        return encrypted;
	}

	public static byte[] decodeFile(byte[] key, byte[] fileData) {
		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
	    Cipher cipher = null;;
	    byte[] decrypted = null;
		try {
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			decrypted = cipher.doFinal(fileData);
		} catch (Exception e) {
			throw new RuntimeException("decode fail");
		}
	    return decrypted;
	}
}
