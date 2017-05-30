package com.ly.mina.charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class EncoderTest {

	IoBuffer encoderBuffer = IoBuffer.allocate(10).setAutoExpand(true);

	@Before
	public void fillBuffer() throws IOException {
		FileChannel fileChannel = FileChannel.open(Paths.get("D:/test.txt"), StandardOpenOption.READ);
		ByteBuffer buffer  = ByteBuffer.allocate(10);
		while( fileChannel.read(buffer)!=-1){
			buffer.flip();
			encoderBuffer.put(buffer);
			buffer.clear();
		}
	}

	@Test
	public void encodeTest() throws CharacterCodingException {
		encoderBuffer.clear();
		Charset charset = Charset.forName("UTF-8");
		CharsetDecoder charsetDecoder = charset.newDecoder();
		CharBuffer charBuffer = CharBuffer.allocate(10);
		ByteBuffer buf = encoderBuffer.buf();

		CoderResult result = charsetDecoder.decode(buf, charBuffer, true);
		System.out.println("buf:"+buf);

		if(result.isError()){
			System.out.println("error");
		}
		if(result.isMalformed()){
			System.out.println("malformed");
		}
		if(result.isOverflow()){
			System.out.println("overflow");
		}
		if(result.isUnderflow()){
			System.out.println("underflow");
		}
		if(result.isUnmappable()) {
			System.out.println("isunmappable");
		}
	}



}
