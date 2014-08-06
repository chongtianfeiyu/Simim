package com.neuo.common;

public class CommonText {
	public static interface TextInterface {
		public String getText();
	}
	
	private String text;
	private TextInterface textWrapper = new TextInterface() {
		@Override
		public String getText() {
			return CommonText.this.getText();
		}
	};
	
	public CommonText() {
		clear();
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void addText(String text) {
		this.text = this.text + text;
	}
	
	public String getText() {
		return this.text;
	}
	
	public void clear() {
		text = "";
	}
	
	public TextInterface getInterface() {
		return textWrapper;
	}
}
