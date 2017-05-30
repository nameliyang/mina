package org.apache.mina.filter;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.DummySession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.codec.textline.TextLineDecoder;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.Queue;

public class TextLineEncoderTest {
    @Test
    public void textLineEncoderTest() throws Exception {

        DummySession session = new DummySession();
        TextLineCodecFactory factory = new TextLineCodecFactory(Charset.forName("UTF-8"));
        TextLineDecoder decoder = (TextLineDecoder) factory.getDecoder(session);

        ProtocolCodecFilter.ProtocolDecoderOutputImpl output = new ProtocolCodecFilter.ProtocolDecoderOutputImpl();
        IoBuffer buffer = IoBuffer.wrap("helloworld\r\nhsdfs".getBytes());
        decoder.decode(session, buffer, output);
        Queue<Object> messageQueue = output.getMessageQueue();
        Object msg;

        for(;(msg=messageQueue.poll())!=null;){
            System.out.println(msg);
        }

    }


}
