package com.ly.mina.designpattern.filterchain.mina;

public class HeadFilter extends IoFilterAdapter{

    @Override
    public void sessionCreated(Entry entry, Object dummySession) {
        System.out.println("head filter do before...");
        entry.getIoFilter().sessionCreated(entry.getNextEntry(), dummySession);
        System.out.println("head filter do after...");
    }
}
