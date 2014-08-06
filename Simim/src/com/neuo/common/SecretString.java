package com.neuo.common;

import java.util.ArrayList;
import java.util.Iterator;

public class SecretString {
	private ArrayList<String> oriStrings;
	private String product;
	private boolean isProduct;
	
	public SecretString() {
		oriStrings = new ArrayList<String>();
		product = "";
		isProduct = false;
	}
	
	public void clear() {
		oriStrings.clear();
		product = "";
		isProduct = false;
	}
	
	public void addString(String ori) {
		oriStrings.add(ori);
		isProduct = false;
	}
	
	public String getString() {
		calcuString();
		return product;
	}
	
	private void calcuString() {
		if (!isProduct) {
			product = "";
			isProduct = true;
			for (Iterator<String> iterator = oriStrings.iterator(); iterator.hasNext();) {
				String ori = iterator.next();
				for (int j = 0; j < ori.length(); j++) {
					char c = ori.charAt(j);
					if (c >= 'a' && c <= 'z') {
						c = (char)(219 - c);
					} else if (c >= 'A' && c <= 'Z') {
						c = (char)(155 - c);
					} else if (c >= '0' && c <= '9') {
						c = (char)(105 - c);
					} else if (c == '.') {
						c = '&';
					} else if (c == '&') {
						c = '.';
					} else if (c == '=') {
						c = '(';
					} else if (c == '(') {
						c = '=';
					} else if (c == '+') {
						c = '%';
					} else if (c == '%') {
						c = '+';
					} else if (c == '/') {
						c = '@';
					} else if (c == '@') {
						c = '/';
					}
					product += c;
				}
			}
		}
	}
}
