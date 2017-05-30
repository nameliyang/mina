package com.ly.mina.selector;

import org.apache.mina.core.service.SimpleIoProcessorPool;
import org.apache.mina.transport.socket.nio.NioProcessor;
import org.apache.mina.transport.socket.nio.NioSession;

public class SimpleIoProcessorPoolTest {
	
	public static void main(String[] args) {
		
		SimpleIoProcessorPool<NioSession> simpleIoProcessorPool = new SimpleIoProcessorPool<NioSession>(NioProcessor.class);
		
		
		simpleIoProcessorPool.dispose();
		
		
	}
}
