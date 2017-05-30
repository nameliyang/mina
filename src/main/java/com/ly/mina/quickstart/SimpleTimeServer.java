package com.ly.mina.quickstart;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SimpleTimeServer {
	
	public static void main(String[] args) throws IOException {
		
		Logger log = LoggerFactory.getLogger(SimpleTimeServer.class);
		
		NioSocketAcceptor acceptor = new NioSocketAcceptor();
		
		acceptor.getFilterChain().addLast("log", new LoggingFilter());
		acceptor.getFilterChain().addLast(
				"protocol",
				new ProtocolCodecFilter(new TextLineCodecFactory(Charset
						.forName("utf-8"))));
		acceptor.setHandler(new IoHandler() {
			
			@Override
			public void sessionOpened(IoSession session) throws Exception {
				log.debug("session open...");
			}
			
			@Override
			public void sessionIdle(IoSession session, IdleStatus status)
					throws Exception {
				log.debug("session idle...");
			}
			
			@Override
			public void sessionCreated(IoSession session) throws Exception {
				log.debug("session created...");
			}
			
			@Override
			public void sessionClosed(IoSession session) throws Exception {
				log.debug("session closed...");
			}
			
			@Override
			public void messageSent(IoSession session, Object message) throws Exception {
				log.debug("msg send="+message);
			}
			
			@Override
			public void messageReceived(IoSession session, Object message)
					throws Exception {
				log.debug("msg received="+(String)message);
				
				session.write(message.toString().toUpperCase());
			}
			
			@Override
			public void inputClosed(IoSession session) throws Exception {
			}
			
			@Override
			public void exceptionCaught(IoSession session, Throwable cause)
					throws Exception {
				cause.printStackTrace();
			}
		});
		
		acceptor.bind(new InetSocketAddress(8080));
	}
}
