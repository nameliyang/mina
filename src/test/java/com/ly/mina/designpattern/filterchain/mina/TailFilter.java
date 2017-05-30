package com.ly.mina.designpattern.filterchain.mina;

public class TailFilter extends IoFilterAdapter{
    @Override
    public void sessionCreated(Entry entry, Object dummySession) {

        System.out.println("tail filter do something ...");
    }
}
