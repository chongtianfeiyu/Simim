package com.neuo.common;

public class ActionTriggle {
	private long count = 0;
	private long currCount = 0;
	
	public ActionTriggle(long count) {
		currCount = 0;
		if (count < 0)count = 1;
		this.count = count;
	}
	
	public ActionTriggle() {
		currCount = 0;
		count = Long.MAX_VALUE;
	}
	
	public void reset() {
		reback(0);
	}
	
	public void reback(long triggleCount) { 
		if (triggleCount < 0)triggleCount = 0;
		synchronized (this) {
			if (triggleCount < currCount) {
				currCount = triggleCount;
			}
		}
	}
	
	public void triggle(long triggleCount, boolean isForce) {
		if (triggleCount < 0)triggleCount = 0;
		synchronized (this) {
			if (!isForce && currCount == triggleCount - 1) {
				currCount ++;
				
				if (currCount > count) {
					currCount = count;
				}
				this.notifyAll();
			} else if (isForce && currCount < triggleCount) {
				currCount = triggleCount;
				
				if (currCount > count) {
					currCount = count;
				}
				this.notifyAll();
			}	
		}
		
	}
	
	public void triggle() {
		synchronized (this) {
			currCount ++;
			this.notifyAll();
			if (currCount > count) {
				currCount = count;
			}	
		}
		
	}
	
	public boolean waitTriggle(){
		synchronized (this) {
			while (currCount != count) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					return false;
				}
			}
		}
		return true;
	}

	public boolean isTriggle() {
		synchronized (this) {
			if (currCount == count) {
				return true;
			}
			return false;	
		}
		
	}
	
	public boolean waitTriggle(long triggleCount) {
		if (triggleCount < 0)triggleCount = 0;
		synchronized (this) {
			while (triggleCount <= count && triggleCount > currCount) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					return false;
				}
			}
		}
		return true;
	}

	public boolean isTriggle(long triggleCount) {
		if (triggleCount < 0)triggleCount = 0;
		synchronized (this) {
			if (triggleCount > count || triggleCount <= currCount) {
				return true;
			}
			return false;	
		}
		
	}
}
