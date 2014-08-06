package com.neuo.common;

public class ThreadUtil {
	public static void sleepInterrupted(long sleepTime, boolean isInterrupted) {
		boolean isException = false;
		boolean isContinue = true;
		long currLeftTime = sleepTime;
		long currTime = System.currentTimeMillis();
		while (isContinue) {
			try {
				isContinue = false;
				Thread.sleep(currLeftTime);
			} catch (InterruptedException e) {
				isException = true;
				
				if (isInterrupted) {
					break;
				}
				long tmpLeftTime = currLeftTime - System.currentTimeMillis() + currTime;
				currLeftTime = tmpLeftTime < 0 ? 0 : tmpLeftTime;
				if (currLeftTime > 0) {
					currTime = System.currentTimeMillis();
					isContinue = true;
				} else {
					isContinue = false;
				}
			}
		}
		if (isException) {
			Thread.currentThread().interrupt();
		}
	}
	
	public static void interruptAndJoin(Thread joinThread, boolean isInterrupted) {
		boolean isException = false;
		boolean isContinue = true;
		joinThread.interrupt();
		while (isContinue) {
			try {
				isContinue = false;
				joinThread.join();
			} catch (Exception e) {
				isException = true;
				
				if (isInterrupted) {
					break;
				}
				isContinue = true;
			}
		}
		if (isException) {
			Thread.currentThread().interrupt();
		}
	}
}
