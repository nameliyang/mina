package com.ly.mina.test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.CopyOnWriteArrayList;

public class Test {
	
	public static final Context context = new Context();

	public static void main(String[] args) throws IOException {
		
		String str = "he\r\r\nhello\rdsfsd\r\n";
		ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
		int matchCount = 0;
		boolean matched = false;
		int oldPos = buffer.position();
		int oldLimit = buffer.limit();
		
		while (buffer.hasRemaining()) {
			
			byte b = buffer.get();
			System.out.println((char)b);
			switch (b) {
				case '\r':
					matchCount++;
					break;
				case '\n':
					matchCount++;
					matched = true;
					break;
				default:
					matched = false;
					break;
			}
			
			if (matched) {
				int pos = buffer.position();
				buffer.limit(pos);
				buffer.position(oldPos);
				
				context.append(buffer);
				
				buffer.position(pos);
				buffer.limit(oldLimit);
				oldPos = buffer.position();
				
				ByteBuffer appendBuf = context.appendBuffer;
				appendBuf.flip(); // limit = pos;postion =0
				appendBuf.limit(appendBuf.limit()-matchCount);
				
				context.write(appendBuf);
				appendBuf.clear();
				matchCount = 0;
				oldPos = pos;
			}
		}
		
		buffer.position(oldPos);
		context.append(buffer);
		
		CopyOnWriteArrayList<ByteBuffer> buffers = context.getBuffers();
		
		for(ByteBuffer buf:buffers){
			System.out.println("textLineMsg :|"+printBuffer(buf)+"|");
		}
		
		context.appendBuffer.flip();
		System.out.println("tempBuffer:|"+ printBuffer(context.appendBuffer)+"|");
		
	}
	
	public static String printBuffer(ByteBuffer byteBuffer){
		StringBuilder sb = new StringBuilder();
		while(byteBuffer.hasRemaining()){
			sb.append((char)byteBuffer.get());
		}
		return sb.toString();
	}
}

class Context {
	
	CopyOnWriteArrayList<ByteBuffer> bufs = new CopyOnWriteArrayList<ByteBuffer>();
	
	ByteBuffer appendBuffer = null;

	public void write(ByteBuffer byteBuffer) {
		ByteBuffer buf = ByteBuffer.allocate(byteBuffer.remaining());
		buf.put(byteBuffer);
		buf.flip();
		bufs.add(buf);
		
	}

	public CopyOnWriteArrayList<ByteBuffer> getBuffers() {
		return bufs;
	}
	
	public Context append(ByteBuffer byteBuffer){
		if(appendBuffer==null){
			appendBuffer = ByteBuffer.allocate(byteBuffer.remaining());
		}
		
		if(appendBuffer.remaining() < byteBuffer.remaining()){
			//最小需要的newCapacity 以后再改
			int newCapacity = appendBuffer.position()  + byteBuffer.remaining();
			
			ByteBuffer newBuffer = ByteBuffer.allocate(newCapacity);
			newBuffer.put(byteBuffer);
			buf(newBuffer);
		}else{
			appendBuffer.put(byteBuffer);
		}
		return this;
	}
	
	
	public void buf(ByteBuffer byteBuffer){
		this.appendBuffer = byteBuffer;
	}
	
	public ByteBuffer buf(){
		return appendBuffer;
	}
	
}
