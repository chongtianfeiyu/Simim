package com.neuo.game;

import android.content.Context;
import android.os.Message;

import com.neuo.android.CommonHandler;
import com.neuo.android.CommonHandler.HandlerOuter;
import com.neuo.common.CommonEvent.Event;
import com.neuo.common.TimeMeter;
import com.neuo.glcommon.GlViewForDraw;

public abstract class GameStateManager 
implements HandlerOuter {
	protected static final int DealPoll = 0;
	protected static final long defaultSleepTime = 5;
	protected long sleepTime;
	protected static class GameHandler extends CommonHandler {
		private GameStateManager gameStateManager;
		public GameHandler(GameStateManager gameStateManager) {
			super(false);
			this.gameStateManager = gameStateManager;
		}

		@Override
		protected void handleMessageSelf(Message msg) {
			if (msg.what == DealPoll) {
				gameStateManager.run();
			} else {
				gameStateManager.onHandleMessage(msg);
			}
		}
		
		@Override
		public void release() {
			super.release();
			this.gameStateManager = null;
		}
	}

	protected GlViewForDraw vfGlDraw;
	protected GameHandler handler;
	protected boolean isRunning;
	protected TimeMeter timeMeter;
	
	public GameStateManager(Context context) {
		init(context);
	}
	
	protected abstract GameHandler initHandler();
	protected abstract void onHandleMessage(Message msg);

	public CommonHandler getHandler() {
		return handler;
	}

	public void init(Context context) {
		this.vfGlDraw = new GlViewForDraw(context);
		this.vfGlDraw.initGl();
		this.handler = initHandler();
		this.timeMeter = new TimeMeter();
		this.sleepTime = defaultSleepTime;
		this.handler.sendEmptyMessageDelayed(DealPoll, this.sleepTime);
	}
	
	public abstract void start();

	protected void run() {
		this.timeMeter.updateTime();
		this.vfGlDraw.go(this.timeMeter.getInterval(TimeMeter.activeState));
		this.handler.sendEmptyMessageDelayed(DealPoll, this.sleepTime);
	}
	
	public GlViewForDraw getViewForDraw() {
		return this.vfGlDraw;
	}

	public void release() {
		this.handler.removeMessagesSelf(DealPoll);
		this.handler.release();
		this.vfGlDraw.release();
		this.vfGlDraw = null;
	}
	
	public void onPause() {
		this.vfGlDraw.pause(false);
		this.handler.onPause();
		isRunning = false;
	}
	
	public void onResume() {
		this.timeMeter.resume();
		this.handler.onResume();
		this.vfGlDraw.start();
		isRunning = true;
	}
	
	public void onStop() {
	}
	
	public void onStart() {
		
	}
	
	public abstract boolean onKeyEvent(Event e);
}
