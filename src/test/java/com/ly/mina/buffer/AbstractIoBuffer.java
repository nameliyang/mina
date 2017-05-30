package com.ly.mina.buffer;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public abstract class AbstractIoBuffer extends IoBuffer{
	
	private final boolean derived ;
	
	private boolean autoExpand ; 
	
	private int minimumCapacity;
	
	private int mark = -1;
	
	/**
	 * capacity是否允许expand
	 */
	private boolean recapacityAllowed = true;
	
	public AbstractIoBuffer(IoBufferAllocator allocator,int initCapacity){
		setAllocator(allocator);
		this.derived = false;
		this.minimumCapacity = initCapacity;
		this.recapacityAllowed = true;
	}
	
	@Override
	public boolean isDirect() {
		return buf().isDirect();
	}
	
	@Override
	public boolean isReadOnly() {
		return buf().isReadOnly();
	}
	
	public abstract void buf(ByteBuffer byteBuffer);
	
	@Override
	public final int minimumCapacity() {
		return minimumCapacity;
	}
	
	@Override
	public IoBuffer minimumCapacity(int miniumCapacity) {
		if (minimumCapacity < 0) {
			throw new IllegalArgumentException("minimumCapacity: "
					+ minimumCapacity);
		}
		this.minimumCapacity = miniumCapacity;
		return this;
	}
	
	@Override
	public int capacity() {
		return buf().capacity();
	}
	
	
	@Override
	public IoBuffer capacity(int newCapacity) {
		
		if(!recapacityAllowed){
		      throw new IllegalStateException("Derived buffers and their parent can't be expanded.");
		}
		ByteBuffer oldBuffer =  buf();
		int oldCapacity = capacity();
		if(newCapacity > oldCapacity ){
			
			int oldPos = oldBuffer.position();
			int oldLimit = oldBuffer.limit();
			ByteOrder order = oldBuffer.order();
			
			ByteBuffer newBuffer = getAllocator().allocateNioBuffer(newCapacity, isDirect());
			oldBuffer.clear();
			
			newBuffer.put(oldBuffer);
			
			if(mark > -1){
				newBuffer.position(mark);
				newBuffer.mark();
			}
			newBuffer.position(oldPos);
			newBuffer.limit(oldLimit);
			newBuffer.order(order);
			
			buf(newBuffer);
		}
		return this;
	}
	
	@Override
	public boolean isAutoExpand() {
		return false;
	}
	
	private IoBuffer expand(int pos,int expectedRemaining,boolean autoExpand){
		
		if(!recapacityAllowed){
	         throw new IllegalStateException("Derived buffers and their parent can't be expanded.");
		}
		
		int end = pos + expectedRemaining;
		int newCapacity;
		if(autoExpand){
			newCapacity = normalizeCapacity(end);
		}else{
			newCapacity = end;
		}
		
		if(newCapacity > capacity()){
			capacity(newCapacity);
		}
		if(end > limit()){
			buf().limit(end);
		}
		return this;
	}
	
	/**
	 * 收缩      limit<= miniCapacity <= capacity
	 */
	@Override
	public final IoBuffer shrink() {
		if(!recapacityAllowed){
			 throw new IllegalStateException("Derived buffers and their parent can't be expanded.");
		}
		
		int pos = postion();
		int limit = limit();
		int capacity = capacity();
		if(limit==capacity){
			return this;
		}
		
		int minCapacity = Math.max(limit, minimumCapacity);
		
		int newCapacity = capacity;
		for(;;){
			
			if(newCapacity>>1 < minCapacity){
				break;
			}
			newCapacity>>=1;
		}
		newCapacity = Math.max(minCapacity, newCapacity);
		if(newCapacity==capacity){
			return this;
		}
		//shrink and compact
		ByteOrder order = buf().order();
		
		ByteBuffer oldBuf = buf();
		oldBuf.position(0);
		oldBuf.limit(limit);
		
		ByteBuffer newBuffer = getAllocator().allocateNioBuffer(newCapacity, isDirect());
		newBuffer.put(buf());
		
		buf(newBuffer);
		buf().position(pos);
		buf().limit(limit);
		buf().order(order);
		mark = -1;
		return this;
	}
	
	
	@Override
	public final int postion() {
		return buf().position();
	}
	
	@Override
	public int limit() {
		return buf().limit();
	}
	
	@Override
	public IoBuffer mark() {
		int pos = buf().position();
		buf().mark();
		mark = pos;
		return this;
	}
	
	@Override
	public final int markValue() {
		return mark;
	}
	
	@Override
	public IoBuffer reset() {
		buf().reset();
		return this;
	}
	
	@Override
	public final IoBuffer clear() {
		buf().clear();
		mark = -1;
		return null;
	}
}




















