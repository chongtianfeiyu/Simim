package com.neuo.android;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import com.qq.e.ads.AdListener;
import com.qq.e.ads.AdRequest;
import com.qq.e.ads.AdSize;
import com.qq.e.ads.AdView;

public class ThridAdManager {
	private static ThridAdManager thridAdManager;
	private FrameLayout bannerContain;
	private TextView extraView;
	private AdView banner;
	private FrameLayout layout;
	private boolean isShow;
	private boolean isTop;
	private LoadBannerCallback callback;
	
	public static interface LoadBannerCallback {
		public void loadBannerSucc();
	}
	
	public static ThridAdManager getThridManager() {
		return thridAdManager;
	}
	
	public static ThridAdManager initThridManager(FrameLayout layout, Context context) {
		if (null == thridAdManager) {
			thridAdManager = new ThridAdManager(context, layout);
		}
		return thridAdManager;
	}

	public static void release() {
		if (null != thridAdManager) {
			thridAdManager.uninit();
			thridAdManager = null;
		}
	}
	
	public ThridAdManager(Context context, FrameLayout layout) {
		this.bannerContain = new FrameLayout(context);
		this.extraView = new TextView(context);
		this.extraView.setText(" ");
		this.layout = layout;
		this.banner = null;
		this.isShow = false;
		this.callback = null;
	}
	
	public void uninit() {
		this.layout = null;
		this.callback = null;
	}
	
	public void addBanner(boolean isTop) {
			if (isShow && this.isTop != isTop) {
				bannerContain.removeAllViews();
				layout.removeView(bannerContain);
				isShow = false;
			}
			if (!isShow) {
				int flag;
				if (isTop) {
					flag = Gravity.TOP;
				} else {
					flag = Gravity.BOTTOM;
				}

				bannerContain.addView(banner);
				bannerContain.addView(extraView);
				FrameLayout.LayoutParams adParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,
						flag | Gravity.CENTER_HORIZONTAL);
				layout.addView(bannerContain, adParams);
				this.isTop = isTop;
				isShow = true;
			}
	}
	
	public void removeBanner() {
			if (isShow) {
				bannerContain.removeAllViews();
				layout.removeView(bannerContain);
				isShow = false;
			}
	}
	
	public void showBanner(boolean isVisible, boolean isTop) {
		if (isVisible) {
			addBanner(isTop);
			bannerContain.setVisibility(View.VISIBLE);
		} else if (isShow) {
			bannerContain.setVisibility(View.GONE);
		}
	}

	public boolean initBanner(Activity activity, String appID, String adsID, 
								LoadBannerCallback callback) {
		if (this.banner != null) {
			return true;
		} else {
			this.callback = callback;
			this.banner = new AdView(activity, AdSize.BANNER, appID, adsID);
			banner.setAdListener(new AdListener() {
				@Override
				public void onNoAd() {
					Log.d("test", "onad no");
				}
				
				@Override
				public void onBannerClosed() {
					Log.d("test", "onad closed");
				}
				
				@Override
				public void onAdReceiv() {
					Log.d("test", "onad receiv");
					if (null != ThridAdManager.this.callback) {
						ThridAdManager.this.callback.loadBannerSucc();
					}
				}

				@Override
				public void onAdExposure() {
					// TODO Auto-generated method stub
					
				}
			});
			AdRequest adr = new AdRequest();
			adr.setRefresh(120);
			banner.fetchAd(adr);
			return false;
		}
	}
}
