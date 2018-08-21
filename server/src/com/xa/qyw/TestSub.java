package com.xa.qyw;

public class TestSub {

	
	public static void main(String[] args) {
		String str = "180922933357";
		
		String first = str.substring(0,3);
		
		String second = str.substring(8);
		
		String phone = first+"****"+second;
		
		System.out.println(phone);
		
	}
	
}
