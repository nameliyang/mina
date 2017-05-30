package com.ly.mina.thread;

import org.junit.Test;

public class DeadLockCheck {

	@Test
	public void lockTest() throws InterruptedException {
		final Object lock = new Object();
		Runnable run = new Runnable() {
			@Override
			public void run() {
				synchronized (lock){
					try {
						System.out.println("before wait");
						lock.wait(4000);
						System.out.println("after wait");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};

		Thread thread = new Thread(run);
		thread.start();
		Thread.sleep(1000);
		synchronized (lock){
			System.out.println("before sleep");
			Thread.sleep(100000);
			System.out.println("after sleep");
		}

	}


	       
}
