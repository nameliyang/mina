package com.ly.mina.test;

import java.nio.ByteBuffer;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		
		int count = 0;
//		for (;;) {
			long start = System.currentTimeMillis();
			Thread[] thread = new Thread[5];
			for (int i = 0; i < thread.length; i++) {
				Client client = new Client();
				thread[i] = new Thread(client);
			}

			for (int i = 0; i < thread.length; i++) {
				thread[i].start();
			}
			for (int i = 0; i < thread.length; i++) {
				thread[i].join();
			}
			
			long end = System.currentTimeMillis();
			System.out.printf("count= %d �ܹ�����%s������ ,����ʱ%d��", count++,
					Client.count, (end - start) / 1000);

//			Thread.sleep(2000);
//		}
	}
}
