package com.ly.mina.mynio.services;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import com.ly.mina.mynio.Processor;
import com.ly.mina.mynio.session.NioSession;

public class Acceptor implements Runnable {
	
	Selector selector;
	
	Processor processor = new Processor();

	public Acceptor(Selector selector) {
		this.selector = selector;
	}

	@Override
	public void run() {
		
		try {
			selector.select();
			Set<SelectionKey> selectedKeys = selector.selectedKeys();
			Iterator<SelectionKey> iterator = selectedKeys.iterator();
			
			while(iterator.hasNext()){
				SelectionKey key = iterator.next();
				iterator.remove();
				if(!key.isValid()){
					key.channel().close();
				}
				NioSession session = newSession(key);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public NioSession newSession(SelectionKey key) throws IOException{
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
		SocketChannel socketChannel = serverSocketChannel.accept();
		return new NioSession(this,processor,socketChannel);
	}
}
