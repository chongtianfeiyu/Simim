package com.neuo.common;

public class TimeMeter {
	public static int activeState = 1 << 0;
	public static int absoluteState = 1 << 1;
	
	private long activeLastTime;
	private long absoluteLastTime;
	private long activeInterval;
	private long absoluteInterval;
	
	public TimeMeter() {
		reset();
	}
	
	public void reset() {
		updateTime();
		absoluteInterval = 0;
		activeInterval = 0;
	}
	
	public void resume() {
		activeLastTime = System.currentTimeMillis();
	}
	
	public void updateTime() {
		long currTime = System.currentTimeMillis();
		activeInterval = currTime - activeLastTime;
		activeLastTime = currTime;
		absoluteInterval = currTime - absoluteLastTime;
		absoluteLastTime = currTime;
		//time is modified
		if (activeInterval < 0)activeInterval = 0;
		if (absoluteInterval < 0)absoluteInterval = 0;
	}
	
	public long getInterval(int singleState) {
		if (singleState == activeState) {
			return activeInterval;
		} else if (singleState == absoluteLastTime) {
			return absoluteInterval;
		} else {
			throw new RuntimeException("time state error " + singleState);
		}
	}
}
