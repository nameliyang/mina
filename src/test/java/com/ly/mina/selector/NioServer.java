package com.ly.mina.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NioServer {
	final Executor executor = Executors.newCachedThreadPool();
	
	final Selector selector; 
	
	boolean selectable ;
	
	ByteBuffer byteBuffer = ByteBuffer.allocate(10);
	
	public NioServer() throws IOException{
		selector = Selector.open(); 
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.bind(new InetSocketAddress(8080));
		serverSocketChannel.configureBlocking(false);
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		selectable  = true;
	}
	
	public static void main(String[] args) throws IOException {
		NioServer nioServer = new NioServer();
		nioServer.start();
	}
	
	public void start(){
		Acceptor acceptor = new Acceptor();
		executor.execute(acceptor);
	}

	
	class Acceptor implements Runnable{
		@Override
		public void run() {
			for(;;){
				try {
					//处理客户端连接请求
					int selected = selector.select();
					if(selected >0){
						process();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private void process() throws IOException {
			
			Set<SelectionKey> selectedKeys = selector.selectedKeys();
			Iterator<SelectionKey> iterator = selectedKeys.iterator();
			
			while(iterator.hasNext()){
				
				SelectionKey selectKey = iterator.next();
				
				Process process = new Process(selectKey);
				executor.execute(process);
				
				iterator.remove();
			}
			
		}
	}
	
	class Process implements Runnable{
		SelectionKey key;
		
		Selector s = Selector.open();
		
		public Process(SelectionKey key) throws IOException{
			this.key = key;
			ServerSocketChannel serverSocketChannel =  (ServerSocketChannel) key.channel();
			SocketChannel socketChannel = serverSocketChannel.accept();
			socketChannel.configureBlocking(false);
			socketChannel.register(s,SelectionKey.OP_READ);
		}

		@Override
		public void run() {
			for(;;){
				try {
					int selected = s.select(40000);
					if(selected>0){
						Set<SelectionKey> selectedKeys = s.selectedKeys();
						Iterator<SelectionKey> iterator = selectedKeys.iterator();
						while(iterator.hasNext()){
							SelectionKey selectKey = iterator.next();
							
							if(selectKey.isValid()&&selectKey.isReadable()){
								
								SocketChannel socketChannel = (SocketChannel) selectKey.channel();
								
								try{
									socketChannel.read(byteBuffer);
								}finally{
									byteBuffer.flip();
								}
								while(byteBuffer.hasRemaining()){
									System.out.print((char)byteBuffer.get());
								}
								byteBuffer.clear();
							}
							iterator.remove();
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

