package com.ly.mina.buffer;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Random;


public abstract class IoBuffer {
	
	private static IoBufferAllocator allocator = new  SimpleBufferAllocator();
	
	private static boolean useDirectBuffer = false;
	
	public static IoBufferAllocator getAllocator(){
		return allocator;
	}
	
	public void setAllocator(IoBufferAllocator allocator){
		if(allocator==null){
			throw new IllegalArgumentException("allocator is null");
		}
		
		IoBuffer.allocator = allocator;
	}
	
	public static IoBuffer allocate(int capacity){
		if(capacity<0){
			throw new IllegalArgumentException("capacity: " + capacity);
		}
		return allocator.allocate(capacity, useDirectBuffer);
	}
	
	public  static int normalizeCapacity(int requestCapacity){
		if(requestCapacity< 0){
			return Integer.MAX_VALUE;
		}
		int newCapacity = Integer.highestOneBit(requestCapacity);
		newCapacity<<=(newCapacity<requestCapacity)?1:0;
        return newCapacity < 0 ? Integer.MAX_VALUE : newCapacity;
	}
	
	
	public abstract void free();
	
	
	public abstract ByteBuffer buf();
	
	
	public abstract boolean isDirect();
	
	
	public abstract boolean isDerived();
	
	
	public abstract boolean isReadOnly();
	
	public abstract int minimumCapacity();
	
	public abstract IoBuffer minimumCapacity(int miniumCapacity);
	
	public abstract int capacity();
	
	public abstract IoBuffer capacity(int newCapacity);
	
	public abstract boolean isAutoExpand();
	
	public abstract IoBuffer setAutoExpand(boolean autoExpand);
	
	public abstract boolean isAutoShrink();
	
	public abstract IoBuffer setAutoShrink(boolean autoShrink);
	
	public abstract IoBuffer expand(int expectedRemaining);
	
	public abstract IoBuffer expand(int postion,int expectedRemaining);
	
	public abstract IoBuffer shrink();
	
	public abstract int postion();
	
	public abstract IoBuffer postion(int newPostion);
	
	public abstract int limit();
	
	public abstract IoBuffer limit(int newLimit);
	
	public abstract IoBuffer mark();
	
	public abstract int markValue();
	
	public abstract IoBuffer reset();
	
	public abstract IoBuffer clear();
	
	public abstract IoBuffer sweep();
	
	public abstract IoBuffer sweep(byte value);
	
	public abstract IoBuffer flip();
	
	public abstract IoBuffer rewind();
	
	public abstract int remaining();
	
	public abstract boolean hasRemaining();
	
	public abstract IoBuffer duplicate();
	
	public abstract IoBuffer slice();
	
	public abstract IoBuffer asReadOnlyBuffer();
	
	public abstract boolean hasArray();
	
	public abstract byte[]	 array();
	
	public abstract int arrayOffset();
	
	public abstract byte get();
	
	public abstract short getUnsigned();
	
	public abstract IoBuffer put(byte b);
	
	public abstract byte get(int index);
	
	public abstract short getUnsigned(int index);
	
	public abstract IoBuffer put(int index,byte b);
	
	public abstract IoBuffer get(byte[] dst,int offset,int length);
	
	
	public abstract IoBuffer get(byte[] dst);
	
	public abstract IoBuffer getSlice(int index,int length);
	
	
	public abstract IoBuffer getSlice(int length);
	
	public abstract IoBuffer put(ByteBuffer src);
	
	public abstract IoBuffer put(IoBuffer src);
	
	public abstract IoBuffer put(byte[] src,int offset,int length);
	
	public abstract IoBuffer put(byte[] src);
	
	public abstract IoBuffer compact();
	
	public abstract ByteOrder order();
	
	public abstract IoBuffer order(ByteBuffer bo);
	
	public abstract char getChar();
	
	
	public abstract IoBuffer putChar(char value);
	
	public abstract char getChar(int index);
	
	public abstract IoBuffer putChar(int index,char value);
	
	public abstract CharBuffer asCharBuffer();
	
	public abstract short getShort();
	
	public abstract int getUnsignedShort();
	
	public abstract IoBuffer putShort(short value);
	
	public abstract short getShort(int index);
	
	
	public abstract int getUnsignedShort(int index);
	
//	public abstract IoBuffer putShort(int index,short value);
	
	public abstract int getInt();
	
	public abstract long getUnsignedInt();
	
	public abstract int getMediumInt();
	
	public abstract InputStream asInputStream();
	
	public abstract OutputStream asOutputStream();
	
	public abstract String getString(CharsetDecoder decoder)throws CharacterCodingException;
	
	
	public abstract String getString(int fieldSize, CharsetDecoder decoder) throws CharacterCodingException;
	
	
	public abstract IoBuffer putString(CharSequence val,CharsetEncoder encoder ) throws CharacterCodingException;
	
	
	public abstract IoBuffer putString(CharSequence val, int fieldSize, CharsetEncoder encoder)
	            throws CharacterCodingException;
	
	
	public abstract String getPrefixedString(CharsetDecoder decoder) throws CharacterCodingException;
	
	
	

	static class SimpleBufferAllocator implements IoBufferAllocator{

		@Override
		public IoBuffer allocate(int capacity, boolean direct) {
			return null;
		}

		@Override
		public ByteBuffer allocateNioBuffer(int capcaity, boolean direct) {
			return null;
		}

		@Override
		public IoBuffer wrap(ByteBuffer nioBuffer) {
			return null;
		}

		@Override
		public void dispose() {
			
		}
		
	}
	
	
	public static void main(String[] args) {
		for(int i=0;i<10;i++){
			Random random = new Random();
			int nextInt = random.nextInt(1000);
			System.out.println(nextInt+"--->"+IoBuffer.normalizeCapacity(nextInt));
			System.out.println("0"+Integer.toBinaryString(nextInt));
			System.out.println(Integer.toBinaryString(IoBuffer.normalizeCapacity(nextInt)));
		}
	}
}


interface IoBufferAllocator{
	
	
	IoBuffer allocate(int capacity,boolean direct);
	
	ByteBuffer allocateNioBuffer(int capcaity,boolean  direct);
	
	
	IoBuffer wrap(ByteBuffer nioBuffer);
	
	
	void dispose();
	
}