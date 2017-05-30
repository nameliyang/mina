package com.ly.mina.future;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.service.AbstractIoAcceptor.AcceptorOperationFuture;

public class AcceptorOperationFutureTest {
	
	public static void main(String[] args) throws InterruptedException {
		
		InetSocketAddress address = new InetSocketAddress(8080);
		List<SocketAddress> localAddresses = new ArrayList<SocketAddress>();
		localAddresses.add(address);
		
		AcceptorOperationFuture request = new AcceptorOperationFuture(
                localAddresses  );
		System.out.println(request.isDone());
		request.await();
	}
	
	public static  void acceptorOperationFutureTest(List<? extends SocketAddress> localAddresses ){
			
	}
}
