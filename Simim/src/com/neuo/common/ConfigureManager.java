package com.neuo.common;

import java.util.Locale;

import android.content.res.Resources;

public class ConfigureManager {
	//private static final String configureName = "config.ini";
	
	private static ConfigureManager configureManager;
	
	public static ConfigureManager initConfigureManager(Resources r) {
		if (null == configureManager) {
			configureManager = new ConfigureManager(r);
		}
		return configureManager;
	}
	
	public static ConfigureManager getConfigureManager() {
		return configureManager;
	}
	
	public static void release() {
		configureManager = null;
	}
	
	//private byte[] contents;
	
	private String laugauge;
	
	private ConfigureManager(Resources r) {
		setlaugauge(Locale.getDefault().getLanguage());
		/*
		try {
			InputStream inputStream = r.getAssets().open(configureName);
			contents = new byte[inputStream.available()];
			inputStream.read(contents);
		} catch (Exception e) {
			throw new RuntimeException("init configure manager fail!");
		}*/
	}

	public void setlaugauge(String laugauge) {
		this.laugauge = laugauge;
	}
	
	public String getLaugauge() {
		return laugauge;
	}
	
	public boolean isChiness() {
		if (laugauge.equals(Locale.CHINESE.getLanguage())) {
			return true;
		} else {
			//Log.d("test", "laugauge " + laugauge + " " + Locale.CHINESE.getLanguage());
			return false;
		}
	}
	
}
