package com.ly.mina.test;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;


public class Client implements Runnable{
	
	private static final String HOST = "123.207.26.172";
//	private static final String HOST = "192.168.133.128";
	
//	private static final String HOST = "192.168.8.100"; 
	
	public static final AtomicInteger count = new AtomicInteger();
	
	private static final int PORT = 8080;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:sss");
	
	@Override
	public void run() {
		try {
			Socket socket;
			try {
				socket = new Socket(HOST, PORT);
			} catch (IOException e) {
				System.out.println("host²»¿É´ï");
				return;
			}
			
			Request request = new Request(socket);
			
			FutureTask<Response> futureTaskResponse =null;
			try {
				futureTaskResponse = request.sendRequest(sdf.format(new Date())+"\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Response response = futureTaskResponse.get();
			
			count.incrementAndGet();
			System.out.println("noewTime: "+sdf.format(new Date())+" ,"+response+ " count"+count);
			
		} catch (InterruptedException e) {
			System.out.println("InterruptedException ");
		} catch (ExecutionException e) {
			e.printStackTrace();
			System.out.println("ExecutionException");
		}
		
		
	}
	
}
