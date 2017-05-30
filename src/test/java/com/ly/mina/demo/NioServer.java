package com.ly.mina.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class NioServer {

	public static void main(String[] args) throws Exception {
		
		NIOConnector connector = new NIOConnector(8080);
		
		connector.start();
		
	}
}

class NIOConnector implements Runnable {

	private static   AtomicLong count = new AtomicLong();
	
	private static AtomicLong writeCount = new AtomicLong();
	
	private static final long TIMEOUT = 1000L;

	private final int port;

	Selector selector = null;

	public NIOConnector(int port) {
		this.port = port;
	}

	@Override
	public void run() {
		
		ServerSocketChannel serverSocketChannel = null;
		
		try {
			selector = Selector.open();
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.socket().bind(new InetSocketAddress(port));
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (;;) {
			int selecteNum = select();
			if (selecteNum <= 0) {
				continue;
			}
			
			Set<SelectionKey> selectedKeysSet = selector.selectedKeys();
			Iterator<SelectionKey> selectKeys = selectedKeysSet.iterator();
			
			while (selectKeys.hasNext()) {
				SelectionKey key = selectKeys.next();
				selectKeys.remove();
				if (key.isAcceptable()) {
					serverSocketChannel = (ServerSocketChannel) key.channel();
					try {
						SocketChannel socketChannel = serverSocketChannel.accept();
						socketChannel.configureBlocking(false);
						IOSession session = new IOSession(new ByteBufferWrapper(ByteBuffer.allocate(100)));
						socketChannel.register(selector, SelectionKey.OP_READ,session);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (key.isWritable()) {
					SocketChannel socketChannel = (SocketChannel) key.channel();
					IOSession ioSession = (IOSession) key.attachment();
					StringBuilder content = ioSession.getContent();
					String responseMsg = "response :"+content;
					ByteBuffer byteBuffer = ByteBuffer.wrap(responseMsg.getBytes());
					try {
						int count = 0;
						while(byteBuffer.hasRemaining()){
							try {
								int startPos = byteBuffer.position();
								socketChannel.write(byteBuffer);
								int endPos = byteBuffer.position();
								System.out.println("write response :"+ new String(byteBuffer.array(),startPos,endPos-startPos));
								count++;
								if(count>1000){
									throw new RuntimeException("too many loop....");
								}
							} catch (IOException e) {
								e.printStackTrace();
								break;
							}
						}
					}finally{
						key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
						key.cancel();
						try {
							socketChannel.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} else if(key.isReadable()){
					SocketChannel socketChannel = (SocketChannel) key.channel();
					IOSession ioSession = (IOSession) key.attachment();
					ByteBufferWrapper wrapper = ioSession.getByteBufferWrapper();
					String readBytes = wrapper.readBytesfromSocketChannel(socketChannel);
					System.out.println("count :"+ count.incrementAndGet() +" readbytes :"+readBytes);
					
					if(readBytes == null){
						System.out.println("Connection reset by peer");
						key.cancel();
						try {
							socketChannel.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					StringBuilder content =  ioSession.getContent().append(readBytes);
					if(content.toString().endsWith("\n")){
						content = new StringBuilder( content.substring(0,content.lastIndexOf("\n")));
						key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
					}
					ioSession.setContent(content);
				}
			}

		}
	}
	private int select() {
		try {
			return selector.select();
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("selector.select()  happpnen error");
	}

	public void start() {

		Thread thread = new Thread(this);
		thread.start();
		System.out.println("------------------------startServer-------------------");
		
	}
	
}
