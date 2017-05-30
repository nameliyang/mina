package com.ly.mina.mynio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

import com.ly.mina.mynio.handler.IoHandler;
import com.ly.mina.mynio.services.Acceptor;

public class NioSocketAcceptor {

    private IoHandler handler;
    
    private Selector selector;
    
    public NioSocketAcceptor() throws IOException {
    	selector = Selector.open();
	}
    
    public void bind(int port) throws IOException{
    	InetSocketAddress socketAddress = new InetSocketAddress(port);
    	ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
    	serverSocketChannel.socket().bind(socketAddress);
    	serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
    	
    	Acceptor acceptor = new Acceptor(selector);
    	new Thread(acceptor).start();
    	
    }

}
