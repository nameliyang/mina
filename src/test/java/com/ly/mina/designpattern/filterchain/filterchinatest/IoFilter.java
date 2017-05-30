package com.ly.mina.designpattern.filterchain.filterchinatest;

public interface IoFilter {
	
	public void sessionCreated(NextFilter nextFilter);
	
	public void sessionCreated(Entry nextEntry);
	
	
}
