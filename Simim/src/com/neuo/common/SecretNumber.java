package com.neuo.common;

public class SecretNumber {
	private long initNum = 0;
	private long number = 0;
	public SecretNumber() {
		getInitNum();
	}
	
	private void getInitNum() {
		while (initNum == 0) {
			initNum = MathUtil.random.nextLong();
		}
	}
	
	public SecretNumber(long num) {
		getInitNum();
		setNumber(num);
	}

	public void setNumber(long num) {
		this.number = initNum + num;
	}
	
	public long getNumber() {
		return this.number - initNum;
	}
	
	public void addNumber(long add) {
		this.number += add;
	}
}
