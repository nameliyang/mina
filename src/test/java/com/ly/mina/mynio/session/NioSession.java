package com.ly.mina.mynio.session;

import java.nio.channels.SocketChannel;

import com.ly.mina.mynio.Processor;
import com.ly.mina.mynio.services.Acceptor;

public class NioSession {
	
	Acceptor acceptor;
	
	Processor processor;
	
	SocketChannel socketChnanel;
	
	public NioSession(Acceptor acceptor, Processor processor,
			SocketChannel socketChannel) {
	}

}
