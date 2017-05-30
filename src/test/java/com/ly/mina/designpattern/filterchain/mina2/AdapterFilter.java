package com.ly.mina.designpattern.filterchain.mina2;

class AdapterFilter implements IoFilter{

    @Override
    public void sessionCreated(NextFilter nextFilter) {
        nextFilter.sessionCreated();
    }
    
}
