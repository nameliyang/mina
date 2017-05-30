package com.ly.mina.demo;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Date;

public class ByteBufferWrapper {

	private ByteBuffer byteBuffer;

	public ByteBufferWrapper(ByteBuffer byteBuffer) {
		this.byteBuffer = byteBuffer;
	}

	public void writeBuffer(String msg) {
		byte[] bytes = msg.getBytes();
		byteBuffer.put(bytes, 0, bytes.length);
	}

	@Override
	public String toString() {
		return byteBuffer.toString();
	}

	public static void main(String[] args) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(100);
		ByteBufferWrapper byteBufferWrapper = new ByteBufferWrapper(byteBuffer);
		
		byteBufferWrapper.writeBuffer("hello");
		
//		System.out.println(byteBufferWrapper.readBufferAsString());

	}

	public String readBytesfromSocketChannel(SocketChannel socketChannel) {
		int readBytes = 0;
		int pos = 0;
		try { 
			  readBytes = socketChannel.read(byteBuffer);
			  pos = byteBuffer.position();
			  if(readBytes == -1){
				  return null;
			  }
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}finally{
			byteBuffer.clear();
			System.out.printf("%s readBytes length : %d,byteBuffer:%s\n",new Date().toString(),readBytes,byteBuffer.toString());
		}
		return new String(byteBuffer.array(),0,pos);
	}
}
