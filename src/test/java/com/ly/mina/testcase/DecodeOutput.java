package com.ly.mina.testcase;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DecodeOutput {

    ConcurrentLinkedQueue<Object> queue =
            new ConcurrentLinkedQueue<>();

    public void write(Object msg){
        queue.add(msg);
    }

    public Queue<Object> getQueue(){
        return queue;
    }
}
