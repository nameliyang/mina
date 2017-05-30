package com.ly.mina.test;

public class IntegerTest {
	
	public static void main(String[] args) {
		Integer a = 1;
		Integer b = 2;
		swap(a, b);
		
		System.out.println(a +"|"+b);
	}
	
	
	public static void swap(Integer a,Integer b){
		Integer temp = a;
		a = b;
		b = temp;
		System.out.println(a +"|"+b);
	}

}
