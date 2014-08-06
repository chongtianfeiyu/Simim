package com.neuo.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class AndroidUtil {
	/*
	private static int googlePlayRequestCode = 30000;

	private static int googlePlayResult = ConnectionResult.SUCCESS;
	public static boolean isGooglePlayServerEnable(Context context) {
		googlePlayResult = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
		return ConnectionResult.SUCCESS == googlePlayResult;
	}
	
	public static boolean isGoolgePlayServerEnableResult() {
		return googlePlayResult == ConnectionResult.SUCCESS;
	}
	
	public static void showGooglePlayServerDialog(Activity activity) {
		if (!isGoolgePlayServerEnableResult()) {
			Dialog res = GooglePlayServicesUtil.getErrorDialog(googlePlayResult, activity, googlePlayRequestCode);
			if (res != null) {
				res.show();
			}
		}
	}
	*/

	private static Toast showToast = null;
	public static void showTip(Context context, String text, boolean isInter) {
		if (null == showToast) {
			showToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		} else {
			if (isInter) {
				showToast.cancel();
				showToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
			} else {
				showToast.setText(text);
			}
		}
		showToast.setGravity(Gravity.BOTTOM, 0, 0);
		showToast.show();
	}
	
	public static void showTip(Context context, int resId, boolean isInter) {
		if (null == showToast) {
			showToast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
		} else {
			if (isInter) {
				showToast.cancel();
				showToast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
			} else {
				showToast.setText(resId);
			}
		}
		showToast.setGravity(Gravity.BOTTOM, 0, 0);
		showToast.show();
	}
	
	public static void initFullScreen(Activity activity, boolean isInitRatio, boolean isKeepOn) {
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE); 
		activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
								WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		if (isInitRatio) {
			DisplayMetrics metrics = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
			ScreenRatioUtil.initScreenUtil(metrics.heightPixels, metrics.widthPixels);
		}
		
		if (isKeepOn) {
			keepScreenOnOff(activity, true);
		}
	}
	
	public static void keepScreenOnOff(Activity activity, boolean isOn) {
		if (isOn) {
			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		} else {
			activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
	}
	
	public static void requestGooglePlay(Activity activity, String packageName) {
		try {
		    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
		} catch (android.content.ActivityNotFoundException anfe) {
		    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)));
		}
	}
}
