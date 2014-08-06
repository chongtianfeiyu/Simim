package com.neuo.common;

import com.neuo.common.CommonText.TextInterface;
import com.neuo.common.UpdateManager.CommonCalcu;
public class Count 
implements CommonCalcu, TextInterface {
	private int digit;
	private long number;
	private long leftNumber;
	
	private static final long defaultBaseSpeed = 100;
	private long baseSpeed = defaultBaseSpeed;
	
	private static final int defaultDigit = 8;
	
	public Count(int digit) {
		this.digit = digit;
		number = 0;
		leftNumber = 0;
	}
	
	public Count() {
		this.digit = defaultDigit;
		number = 0;
		leftNumber = 0;
	}
	
	public int getDigit() {
		return digit;
	}
	
	public long getNumber() {
		return number / 1000;
	}
	
	public long getLeftNumer() {
		return leftNumber / 1000;
	}
	
	public long getSum() {
		return (number + leftNumber) / 1000;
	}
	
	public boolean isOver() {
		return leftNumber == 0;
	}
	
	public long getBaseSpeed() {
		return baseSpeed;	
	}
	
	public void setBaseSpeed(long baseSpeed) {
		if (baseSpeed > 0) {
			this.baseSpeed = baseSpeed;
		} else {
			this.baseSpeed = defaultBaseSpeed;
		}
	}
	
	public void clearNum() {
		number = 0;
		leftNumber = 0;
	}
	
	public void addNumber(long add) {
		leftNumber += add * 1000;
	}
	
	public void calcuNumber(long interval) {
		if (leftNumber > 0) {
			float numInterval = interval * baseSpeed;
			if (leftNumber <= numInterval) {
				number += leftNumber;
				leftNumber = 0;
			} else {
				leftNumber -= numInterval;
				number += numInterval;
			}
		} else if (leftNumber < 0){
			float numInterval = -interval * baseSpeed;
			if (leftNumber >= numInterval) {
				number += leftNumber;
				leftNumber = 0;
			} else {
				leftNumber -= numInterval;
				number += numInterval;
			}
		}
		
	}
	
	public void setNumber(long num) {
		leftNumber = 0;
		number = num * 1000;
	}
	
	public void setDigit(int digit) {
		this.digit = digit;
	}

	@Override
	public void calcu(long interval) {
		calcuNumber(interval);
	}

	@Override
	public String getText() {
		return "" + getNumber();
	}

}
