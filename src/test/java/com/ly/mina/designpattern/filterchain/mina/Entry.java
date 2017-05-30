package com.ly.mina.designpattern.filterchain.mina;

public class Entry {

    Entry preEntry;

    Entry nextEntry;

    IoFilter ioFilter;

    String name;

    public Entry(String name,Entry preEntry, Entry nextEntry,IoFilter ioFiler) {
        this.name = name;
        this.preEntry = preEntry;
        this.nextEntry = nextEntry;
        this.ioFilter = ioFiler;
    }

    public Entry( String name,IoFilter ioFilter){
        this(name,null,null,ioFilter);
    }

    public Entry getPreEntry() {
        return preEntry;
    }

    public Entry getNextEntry() {
        return nextEntry;
    }

    public IoFilter getIoFilter() {
        return ioFilter;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "name: "+name+",preEntry: "+preEntry.getName()+" ,nextEntry: ";
    }
}
