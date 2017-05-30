package org.apache.mina.core.buffer;

import org.junit.Test;

public class MyIoBufferTest {

    @Test
    public void testCapacity(){
        IoBuffer ioBuffer = IoBuffer.allocate(10);
        ioBuffer.put("123456".getBytes());
        ioBuffer.flip();

        IoBuffer newBuffer = ioBuffer.capacity(7);
        System.out.println(ioBuffer.capacity() == newBuffer.capacity());
        System.out.println(ioBuffer.equals(newBuffer));
        System.out.println(ioBuffer.equals(ioBuffer.duplicate()));

        //increase the capacity
        ioBuffer = IoBuffer.allocate(10);
        ioBuffer.put("123456".getBytes());
        ioBuffer.flip();
        ioBuffer.capacity(14);
    }
    @Test
    public void testExpand(){
        IoBuffer buffer = IoBuffer.allocate(10 );
        buffer.put("123456".getBytes());
        buffer.flip();
        //see remaining
        System.out.println(buffer.remaining());
        System.out.println("------------------------------");
        // �������Ҫexpand ��������expand����
        IoBuffer newBuffer = buffer.expand(2);
        System.out.println(newBuffer.position());
        System.out.println(newBuffer.capacity());
        System.out.println(newBuffer.limit());
        // expand buffer ������limit
        System.out.println("--------expand buffer ������limit-----------");
        buffer = IoBuffer.allocate(10);
        buffer.put("123456".getBytes());
        buffer.flip();
        buffer.expand(8);
        printBufferInfo(buffer);
        System.out.println(buffer.limit() == 8);
        System.out.println(buffer.position()==0);
        System.out.println(buffer.capacity()==10);

        System.out.println("expand the buffer above the  limit");
        buffer = IoBuffer.allocate(10);

    }

    @Test
    public void shrinkbuffertest(){
        IoBuffer buffer = IoBuffer.allocate(10);
        buffer.setAutoShrink(true);
        buffer.minimumCapacity(5);
        buffer.put("hel".getBytes());
        buffer.flip();
        printBufferInfo(buffer);
        System.out.println("buffer isautoshrink : " + buffer.isAutoShrink());
        buffer.shrink();
        printBufferInfo(buffer);
    }


    private void printBufferInfo(IoBuffer ioBuffer){
        System.out.println("IoBuffer info= "+ioBuffer.toString());
    }



}
