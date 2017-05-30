package com.ly.mina.buffer;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;
import org.junit.Test;

public class IoBufferTest {

    @Test
    public void  simpletest(){

    	IoBuffer ioBuffer = IoBuffer.allocate(100);
    	ioBuffer.minimumCapacity(49);
    	ioBuffer.put("hello".getBytes());
    	ioBuffer.limit(10);
    	System.out.println(ioBuffer);
    	ioBuffer.shrink();
    	System.out.println(ioBuffer);
    	
    	ByteBuffer buf = ByteBuffer.allocate(10);
    	buf.put((byte)'A');
    	buf.get();
    	
    }


}
