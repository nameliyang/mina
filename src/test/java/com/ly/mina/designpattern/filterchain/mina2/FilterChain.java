package com.ly.mina.designpattern.filterchain.mina2;

import java.util.ArrayList;
import java.util.List;

public class FilterChain {
	
    private EntryImpl headEntry;

    private EntryImpl tailEntry;

    public FilterChain(){
        headEntry = new EntryImpl(null,null,"headEntry",new HeadFilter());
        tailEntry = new EntryImpl(headEntry,null,"tailEntry",new TailFilter());
        headEntry.nextEntry = tailEntry;
    }

    public void addLast(String name,IoFilter ioFilter){
        //将entry引用指向tailEntry.preEntry tailEntry
        EntryImpl entry = new EntryImpl(tailEntry.preEntry,tailEntry,name,ioFilter);
        tailEntry.preEntry.nextEntry = entry;
        tailEntry.preEntry = entry;
    }

    public void fireSessionCreated(){
        IoFilter ioFilter = headEntry.ioFilter;
        NextFilter nextFilter = headEntry.nextFilter;
        ioFilter.sessionCreated(nextFilter);
    }

    @Override
    public String toString() {
        List<EntryImpl> entrys = new ArrayList<>();
        EntryImpl entry = headEntry;
        do{
            entrys.add(entry);
            entry = entry.nextEntry;
        }while(entry!=null);
        return entrys.toString();
    }

    class HeadFilter extends AdapterFilter{
        @Override
        public void sessionCreated(NextFilter nextFilter) {
            //委托给下一个filter 去执行
            nextFilter.sessionCreated();
        }
    }

    class TailFilter extends AdapterFilter{
        @Override
        public void sessionCreated(NextFilter nextFilter) {
        	//TODO
        }
    }

    class EntryImpl {
    	
        EntryImpl preEntry;
        EntryImpl nextEntry;
        IoFilter ioFilter;
        
        NextFilter nextFilter;
        
        String name;

        public EntryImpl(EntryImpl preEntry,EntryImpl nextEntry_,String name,IoFilter ioFilter){
            this.preEntry = preEntry;
            this.nextEntry = nextEntry_;
            this.name = name;
            this.ioFilter = ioFilter;
            
            nextFilter = new NextFilter() {
                @Override
                public void sessionCreated() {
                    IoFilter filter = nextEntry.ioFilter;
                    filter.sessionCreated(nextEntry.nextFilter);
                }
            };
        }

        @Override
        public String toString() {
            return this.name;
        }
        
    }
}
