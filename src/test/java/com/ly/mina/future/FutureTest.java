package com.ly.mina.future;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.session.IoSession;

public class FutureTest {

	public static void main(String[] args) {
		
		ReentrantLock  lock  =  new ReentrantLock();
		lock.lock();
		
		DefaultFuture future = new DefaultFuture();
		System.out.println("future awiat");
		
		new Thread(){
			public void run() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("set valul hello");
				future.setValue("hello");
			};
		}.start();
		
		try {
			future.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("await end ...");
	}
	
	static class DefaultFuture implements IoFuture {

		private static final long DEAD_LOCK_CHECK_INTERVAL = 5000L;

		private final Object lock;

		private boolean ready;

		private int waiters;

		private Object result;

		public DefaultFuture() {
			lock = this;
		}

		public void setValue(Object newValue) {
			synchronized (lock) {
				if(ready){
					return;
				}
				
				result = newValue;
				ready = true;
				if(waiters>0){
					lock.notifyAll();
				}
			}
		}

		@Override
		public IoSession getSession() {
			return null;
		}

		@Override
		public IoFuture await() throws InterruptedException {
			synchronized (lock) {
				while (!ready) {
					waiters++;
					try {
						lock.wait(DEAD_LOCK_CHECK_INTERVAL);
					} finally {
						waiters--;
					}
				}
			}
			return this;
		}
		
		
		private boolean await0(long timeoutMillis,boolean interruptable) throws InterruptedException{
			long endTime  = System.currentTimeMillis()+timeoutMillis;
			
			synchronized (lock) {
				
				waiters++;
				
				try{
					
					while(!ready){
						try {
							long timeOut = Math.min(timeoutMillis, DEAD_LOCK_CHECK_INTERVAL);
							lock.wait(timeOut);
						} catch (InterruptedException e) {
							if(interruptable){
								throw e;
							}
						}
						if(endTime  < System.currentTimeMillis()){
							return ready;
						}
					}
					
				}finally{
					waiters--;
				}
			}
			return ready;
		}
		
		@Override
		public boolean await(long timeout, TimeUnit unit)
				throws InterruptedException {
			return await(unit.toMillis(timeout));
		}

		@Override
		public boolean await(long timeoutMillis) throws InterruptedException {
			return await0(timeoutMillis, true);
		}

		@Override
		public IoFuture awaitUninterruptibly() {
			try {
				await0(Long.MAX_VALUE, false);
			} catch (InterruptedException ie) {
			}
			return this;
		}

		@Override
		public boolean awaitUninterruptibly(long timeout, TimeUnit unit) {
			 return awaitUninterruptibly(unit.toMillis(timeout));
		}

		@Override
		public boolean awaitUninterruptibly(long timeoutMillis) {
			try {
				return await0(timeoutMillis, false);
			} catch (InterruptedException e) {
				throw new InternalError();
			}
		}

		@Override
		public void join() {

		}

		@Override
		public boolean join(long timeoutMillis) {
			return false;
		}

		@Override
		public boolean isDone() {
			synchronized (lock) {
				return ready;
			}
		}

		@Override
		public IoFuture addListener(IoFutureListener<?> listener) {
			return null;
		}

		@Override
		public IoFuture removeListener(IoFutureListener<?> listener) {
			return null;
		}

	}
}
