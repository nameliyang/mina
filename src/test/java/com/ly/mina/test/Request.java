package com.ly.mina.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


public class Request implements Callable<Response>{
	
	private Socket socket ;
	
	private String msg;
	
	public Request(Socket socket) {
		this.socket = socket;
	}

	public FutureTask<Response> sendRequest(String msg) throws IOException, InterruptedException, ExecutionException {
		
		this.msg = msg;
		
		final FutureTask<Response> response = new FutureTask<Response>(this);
		
		new Thread(){
			public void run() {
				response.run();
			};
		}.start();
		
		return response;
	}
	

	@Override
	public Response call() throws Exception {
//		socket.setSoTimeout(1000);
		
		OutputStream outputStream = socket.getOutputStream();
		outputStream.write(msg.getBytes());
		
		InputStream inputStream = socket.getInputStream();
		StringBuilder responseContent = new StringBuilder();
		int read = inputStream.read();
		
		while(read != -1)	{
			responseContent.append((char)read);
			read = inputStream.read();
		}
		Response response = new Response(responseContent.toString());
		return response;
	}
	
	

}
