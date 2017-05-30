/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.apache.mina.filter.codec.textline;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.DummySession;
import org.apache.mina.filter.codec.AbstractProtocolEncoderOutput;
import org.apache.mina.filter.codec.ProtocolCodecSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import static org.junit.Assert.assertEquals;

/**
 * Tests {@link TextLineEncoder}.
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public class TextLineEncoderTest {

	@Test
	public void testEncode() throws Exception {

		TextLineEncoder encoder = new TextLineEncoder(Charset.forName("UTF-8"),
				LineDelimiter.WINDOWS);
		ProtocolCodecSession session = new ProtocolCodecSession();
		ProtocolEncoderOutput out = session.getEncoderOutput();

		encoder.encode(session, "ABC", out);
		assertEquals(1, session.getEncoderOutputQueue().size());
		IoBuffer buf = (IoBuffer) session.getEncoderOutputQueue().poll();
		assertEquals(5, buf.remaining());
		assertEquals('A', buf.get());
		assertEquals('B', buf.get());
		assertEquals('C', buf.get());
		assertEquals('\r', buf.get());
		assertEquals('\n', buf.get());
	}

	@Test
	public void mytest() throws Exception {
		
		DummySession session = new DummySession();
		
		AbstractProtocolEncoderOutput output = new AbstractProtocolEncoderOutput() {
			@Override
			public WriteFuture flush() {
				return null;
			}
		};
		
		TextLineEncoder encoder = new TextLineEncoder(Charset.forName("UTF-8"),
				LineDelimiter.DEFAULT);
		encoder.encode(session, "hello\r\nliyang", output);
		Queue<Object> messageQueue = output.getMessageQueue();
		Object obj = null;
		while ((obj = messageQueue.poll()) != null) {
			System.out.println(obj.toString() + "|" + obj.getClass().getName());
		}
	}
	
	@Test
	public void encodeTest(){
		String msg = "李阳这是一个测试";
		CharBuffer charBuffer = CharBuffer.wrap(msg);
		System.out.println("char buffer position :　" + charBuffer.position());
		CharsetEncoder encoder =  Charset.forName("utf-8").newEncoder();
		ByteBuffer outBuffer = ByteBuffer.allocate(10);
		CoderResult result = encoder.encode(charBuffer, outBuffer, false);
		
		System.out.println(result.isOverflow());
		System.out.println(result.isUnderflow());
		System.out.println("char buffer position :　" + charBuffer.position());
		System.out.println("-------------------------------------------------");
		
		charBuffer = CharBuffer.wrap("sdfds");
		ByteBuffer outBuffer2 = ByteBuffer.allocate(20);
		CoderResult result2 = encoder.encode(charBuffer, outBuffer2, true);
		System.out.println(result2.isOverflow());
		System.out.println(result2.isUnderflow());
		System.out.println("outbuffer2 position :"+outBuffer2.position());
	}
	
	@Test
	public void test() throws CharacterCodingException, UnsupportedEncodingException {
		String str = "\u674E";
		System.out.println("str = "+str);
		System.out.println("code point : " + str.codePointAt(0));
		byte[] bytes = str.getBytes("UTF-8");

		for(byte b: bytes){
			System.out.println(Integer.toHexString(b&0xFF).toUpperCase());
		}


		char c = str.charAt(0);
		System.out.println("hex string : "+Integer.toHexString(c));
		
		byte[] inttoBytes = inttoBytes(c);
		StringBuilder sb = new StringBuilder();
		for(byte b:inttoBytes){
			sb.append(Integer.toHexString(b)).append(",");
		}
		System.out.println("bytes  "+sb);
		
		Charset charset = Charset.forName("utf-8");
		CharsetEncoder encoder = charset.newEncoder();
		ByteBuffer encode = encoder.encode(CharBuffer.wrap(new char[]{c}));
		
		//reset sb
		sb = new StringBuilder();
		while(encode.hasRemaining()){
			byte s = encode.get();
			System.out.println(s +"---->"+ (s&0xFF));
			System.out.println(Integer.toBinaryString(s)+ "------->"+Integer.toBinaryString(s&0xFF));
			sb.append(Integer.toHexString(s&0xFF)).append(",");
		}
		System.out.println(sb);
		
	}
	
	private byte[] inttoBytes(int a){
		byte[] bytes = new byte[4];
		bytes[0] = (byte) (a&0xFF);
		bytes[1] = (byte)(a>>8&0xFF);
		bytes[2] = (byte)(a>>16&0xFF);
		bytes[3] = (byte)(a>>24&0xFF);
		return bytes;
	}
	
	@Test
	public void bytetoIntegerTest(){
		byte b = 123;
		
		int a = b;
		System.out.println(Integer.toHexString(a));
		System.out.println(IoBuffer.allocate(100).getClass().getName());
	}
	@Test
	public void writeUTFFileTest(){
		String str = "李";
		try {
			FileOutputStream fos = new FileOutputStream(new File("D:GBK.txt"));
			byte[] bytes = str.getBytes("GBK");
			fos.write(bytes);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void bytesdecoder() throws IOException {
		String codec = "utf-8";
		String filePath = codec.equals("utf-8")?"D:/utf8.txt":"D:/GBK.txt";
		FileInputStream fileInputStream = new FileInputStream(filePath);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(10);
		byte[] buffer = new byte[10];
		int length = 0;

		while((length = fileInputStream.read(buffer))!=-1){
			byteArrayOutputStream.write(buffer,0,length);
		}
		fileInputStream.close();
		byte[] contentBytes = byteArrayOutputStream.toByteArray();
		System.out.println(bytes2Hex(contentBytes));

		ByteBuffer byteBuffer = ByteBuffer.wrap(contentBytes);
		CharsetDecoder decoder = Charset.forName(codec).newDecoder();

		CharBuffer charBuffer = CharBuffer.allocate(10);
		CoderResult coderResult = decoder.decode(byteBuffer, charBuffer, true);

		charBuffer.flip();
		while(charBuffer.hasRemaining()){
			System.out.println(charBuffer.get());
		}


	}

	private List<String> bytes2Hex(byte[] bytes){
		List<String> hexList = new ArrayList<>();
		for(byte b: bytes){
			hexList.add(Integer.toHexString(b&0xFF).toUpperCase());
		}
		return hexList;
	}

	@Test
	public void endofInputTest(){
		try{
			CharBuffer charBuffer = CharBuffer.allocate(3);
			charBuffer.put('李');
			charBuffer.flip();
			CharsetEncoder encoder = Charset.forName("GBK").newEncoder();
			ByteBuffer byteBuffer = ByteBuffer.allocate(5);


			CoderResult result = encoder.encode(charBuffer, byteBuffer, true);
			System.out.println(byteBufferToString(byteBuffer));

			byteBuffer.flip();
			CoderResult result1 = encoder.encode(charBuffer, byteBuffer, false);
			System.out.println(byteBufferToString(byteBuffer));
		}catch (Exception e){
			e.printStackTrace();
		}


//		charBuffer.flip();
//		encoder.encode(charBuffer, byteBuffer, true);
//		System.out.println(byteBufferToString(byteBuffer));


	}

	public String byteBufferToString(ByteBuffer byteBuffer){
		byte[] array = byteBuffer.array();
		List<String> hexList = new ArrayList<>();
		for(byte b:array){
			hexList.add(Integer.toHexString(b&0xFF));
		}
		return byteBuffer.toString() + "|"+ hexList;
	}

}
