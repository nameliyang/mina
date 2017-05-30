package com.ly.mina.demo;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IOSession {
	
	private static Logger log =  LoggerFactory.getLogger(IoSession.class);
	
	private static final AtomicLong SESSIONIDSEQ = new AtomicLong();
	
	private  final String sessionId;
	
	private AtomicLong accessTimes = new AtomicLong(0);
	
	private StringBuilder msg = new StringBuilder();
	
	private ByteBufferWrapper byteBufferWrapper;
	
	public IOSession(ByteBufferWrapper byteBufferWrapper){
		this.byteBufferWrapper = byteBufferWrapper;
		this.sessionId = String.valueOf(SESSIONIDSEQ.incrementAndGet());
		log.debug("create session,sessionId = {}", sessionId);
	}
	
	public ByteBufferWrapper getByteBufferWrapper(){
		return this.byteBufferWrapper;
	}
	
	public String appendContent(String msg){
		return this.msg.append(msg).toString();
	}
	
	public StringBuilder getContent(){
		return this.msg;
	}
	
	public void setContent(StringBuilder msg ){
		this.msg = msg;
	}
	
	public void readAccess(){
		accessTimes.incrementAndGet();
	}
	
	
	
}
