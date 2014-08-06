package com.neuo.common;

public class CommonThread extends Thread {
	protected boolean isStop = true;
	protected boolean isPause = false;
	protected boolean isStopChange = false;
	protected boolean isPausechange = false;
	protected TimeMeter timeMeter = new TimeMeter();
	
	public void go(){
	}
	
	public void begin(){
		
	}
	
	public void end(){
		
	}
	
	public long getInterval(int singleState) {
		return timeMeter.getInterval(singleState);
	}
	
	public void run(){
		//time reset
		timeMeter.reset();
		begin();
		while (true) {
			while (true) {
				synchronized (this) {
					if (isStop != isStopChange) {
						isStop = isStopChange;
						this.notifyAll();
					}
					if (isStop)break;
					if (isPause != isPausechange) {
						isPause = isPausechange;
						//resume time
						if (!isPause) {
							timeMeter.resume();
						}
						this.notifyAll();
					}
					if (!isPause)break;
					try {
						this.wait();
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			}
			if (isStop)break;
			timeMeter.updateTime();
			go();
		}
		end();
	}
	
	public void onStart(boolean pause) {
		synchronized (this) {
			isStopChange = false;
			isPausechange = pause;
			start();
			while (isStop || isPause != pause){
				try {
					this.wait();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}
	
	public void goNotify(){
		
	}
	
	public void onStop(){
		synchronized (this) {
			isStopChange = true;
			this.notifyAll();
			goNotify();
			while (!isStop){
				try {
					this.wait();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
			ThreadUtil.interruptAndJoin(this, true);
		}
	}
	
	public void onResume(){
		synchronized (this) {
			isPausechange = false;
			this.notifyAll();
			goNotify();
			while (isPause && !isStop){
				try {
					this.wait();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}
	
	public void onPause(){
		
		synchronized (this) {
			isPausechange = true;
			this.notifyAll();
			goNotify();
			while (!isPause && !isStop){
				try {
					this.wait();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}
	
}
