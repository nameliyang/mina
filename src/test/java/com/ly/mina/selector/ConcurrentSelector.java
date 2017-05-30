package com.ly.mina.selector;

import java.io.IOException;
import java.nio.channels.Selector;

public class ConcurrentSelector {
	
	public static void main(String[] args) throws IOException {
		
		
		Selector selector = Selector.open();
		System.out.println(selector);
		
		selector = Selector.open();
		System.out.println(selector);
		
	}
}
