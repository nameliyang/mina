package com.ly.mina.test;


public class Response {
	
	private Request request;
	private String responseContent ;
	
	public Response(String responseContent) {
		this.responseContent = responseContent;
	}

	public void setRequest(Request request) {
		this.request = request;
	}
	
	
	@Override
	public String toString() {
		
		return responseContent;
		
	}
	
	
}
