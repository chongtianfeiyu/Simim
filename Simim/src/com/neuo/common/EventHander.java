package com.neuo.common;

import com.neuo.common.CommonEvent.Event;

public interface EventHander {
	public EventHander isHanderEvent(Event e);
	public int onHanderEvent(Event e);
	public void resetTouchState();
}
