package com.ly.mina.designpattern.filterchain.mina;

public class IoFilterChain {
    private Entry head ;

    private Entry tail;

    public IoFilterChain(){
        head = new Entry("headEntry",null,null,new HeadFilter());
        tail = new Entry("tailEntry",head,null,new TailFilter());
        head.nextEntry = tail;
    }

    public void addLast(Entry entry){
        Entry preEntry = tail.preEntry;

        entry.preEntry = preEntry;
        entry.nextEntry = tail;

        preEntry.nextEntry = entry;
        tail.preEntry = tail;
    }

    public void sessionCreated(Object dummySession){
        IoFilter ioFilter = head.getIoFilter();
        ioFilter.sessionCreated(head.getNextEntry(),dummySession);
    }

    public static void main(String[] args) {
        IoFilterChain chain = new IoFilterChain();
        chain.addLast(new Entry("logging", new IoFilter() {
            @Override
            public void sessionCreated(Entry entry, Object dummySession) {
                System.out.println("logging filter dosomething before...");
                entry.getIoFilter().sessionCreated(entry.getNextEntry(),dummySession);
                System.out.println("logging filter dosomething after...");
            }
        }));
        
        
        chain.sessionCreated("hello");
    }
}
