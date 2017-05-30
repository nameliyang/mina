package com.ly.mina.designpattern.filterchain.mina2;

public interface IoFilter {

    public void sessionCreated(NextFilter nextFilter);

}
