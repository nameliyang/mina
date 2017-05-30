package com.ly.mina.designpattern.filterchain.mina;

public interface IoFilter {

    public void sessionCreated(Entry entry ,Object dummySession);

}
