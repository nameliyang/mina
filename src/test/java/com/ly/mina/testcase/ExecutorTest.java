package com.ly.mina.testcase;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

public class ExecutorTest {
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		
		for(int i = 0;i<1000;i++){
			Random random = new Random();
			int j = random.nextInt(1000);
			int hignOneBit = Integer.highestOneBit(j);
			
			if(j <= hignOneBit){
				String str1 = format(Integer.toBinaryString(j));
				String str2 = format(Integer.toBinaryString(hignOneBit));
				System.out.println(str1+"----->"+str2);
			}
		}
	}
	
	public static String format(String zeroStr){
		int zeroStrLength = zeroStr.length();
		int length = 32 - zeroStrLength;
		StringBuilder sb = new StringBuilder();
		for(int i = 0;i<length;i++){
			sb.append("0");
		}
		return sb.toString()+zeroStr;
	}
}
