package com.neuo.android;

import java.util.Iterator;
import java.util.Stack;

import android.os.Handler;
import android.os.Message;

public abstract class CommonHandler extends Handler {
	protected boolean isRunInPause;
	protected boolean isRun;
	protected boolean isRelease;
	protected Stack<Message> msgStack;
	
	public static interface HandlerOuter {
		public CommonHandler getHandler();
	}
	
	public CommonHandler(boolean isRunInPause) {
		super();
		init(isRunInPause);
	}
	
	protected void init(boolean isRunInPause) {
		this.isRunInPause = isRunInPause;
		this.isRelease = false;
		this.isRun = false;
		if (!this.isRunInPause) {
			this.msgStack = new Stack<Message>();
		}
	}
	
	public void removeMessagesSelf(int what) {
		super.removeMessages(what);
		if (!isRun) {
			for (Iterator<Message> iterator = msgStack.iterator(); iterator.hasNext();) {
				Message tmp = iterator.next();
				if (tmp.what == what) {
					iterator.remove();
					tmp.recycle();
				}
			}
		}
	}
	
	protected abstract void handleMessageSelf(Message msg);

	@Override
	public void handleMessage(Message msg) {
		if (isRelease) {
			return;
		}
		if (!isRunInPause && !isRun) {
			msgStack.push(Message.obtain(msg));
		} else {
			handleMessageSelf(msg);
		}
	}
	
	public void release() {
		isRelease = true;
		if (!isRunInPause) {
			while (!msgStack.isEmpty()) {
				msgStack.pop().recycle();
			}
		}
	}
	
	public void onPause() {
		isRun = false;
	}
	
	public void onResume() {
		if (!isRunInPause) {
			while(!msgStack.isEmpty()) {
				sendMessageAtFrontOfQueue(msgStack.pop());
			}
		}
		isRun = true;
	}
}
	