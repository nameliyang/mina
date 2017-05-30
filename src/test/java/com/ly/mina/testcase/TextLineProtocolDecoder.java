package com.ly.mina.testcase;


import org.apache.mina.core.buffer.IoBuffer;

public class TextLineProtocolDecoder implements ProtocolDecoder{

    @Override
    public void decode(Object inputMsg, DecodeOutput output) {
        IoBuffer msg = (IoBuffer) inputMsg;
        int oldPostion = msg.position();
        int matchCount = 0;
        while(msg.hasRemaining()) {
            byte b = msg.get();
            switch (b) {
                case '\r':
                    matchCount++;
                    break;
                case '\n':
                    matchCount++;
                    break;
                default:
                    ;
            }
        }
        if(matchCount>0){
            int newPostion = msg.position();
            msg.position(oldPostion);
            msg.limit(newPostion-matchCount);
            IoBuffer buffer = IoBuffer.allocate(newPostion-matchCount);
            while(msg.hasRemaining()){
                buffer.put(msg.get());
            }
            buffer.flip();
            output.write(buffer);
        }

    }

}
