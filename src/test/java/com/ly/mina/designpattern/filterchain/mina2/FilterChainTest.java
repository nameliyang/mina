package com.ly.mina.designpattern.filterchain.mina2;

import org.junit.Test;

public class FilterChainTest {
	
    @Test
    public void filterChainTest(){
        FilterChain chain = new FilterChain();
        chain.addLast("filterA",new IoFilterA());
        chain.addLast("filterB",new IoFilterB());
        
        chain.fireSessionCreated();
    }


    class IoFilterA extends AdapterFilter{
    	
        @Override
        public void sessionCreated(NextFilter nextFilter) {
        	System.out.println("filterA filter before");
        	nextFilter.sessionCreated();
        	System.out.println("filterA filter after");
        }
    }

    class IoFilterB extends  AdapterFilter{

        @Override
        public void sessionCreated(NextFilter nextFilter) {
        	System.out.println("fitlerB filter before ");
        	nextFilter.sessionCreated();
        	System.out.println("fitlerB filter after ");
        }
    }




}
