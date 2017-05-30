package com.ly.mina.quickstart;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class MockClient {

    public static void main(String[] args) throws IOException, InterruptedException {
    	
    	
        Socket socket = new Socket("127.0.0.1", 9123);
        InputStream inputStream = socket.getInputStream();
        byte[] buffer = new byte[1];
        int length = 0;
        while ((length = inputStream.read(buffer)) != -1) {
            System.out.println(new String(buffer, 0, length));
            TimeUnit.SECONDS.sleep(1);
        }
        inputStream.close();
        socket.close();


    }

}
