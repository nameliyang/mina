package com.ly.mina.designpattern.filterchain.filterchinatest;

public class Entry {

	Entry preEntry;

	Entry nextEntry;

	IoFilter ioFilter;

	String name;

	NextFilter nextFilter;

	public Entry(Entry preEntry, Entry nextEntry, String name, IoFilter ioFilter) {
		this.preEntry = preEntry;
		this.nextEntry = nextEntry;
		this.name = name;
		this.ioFilter = ioFilter;
		this.nextFilter = new NextFilter() {
			@Override
			public void sessionCreated() {
				IoFilter filter = Entry.this.nextEntry.ioFilter;
				NextFilter nextFilter = Entry.this.nextEntry.nextFilter;
				filter.sessionCreated(nextFilter);
			}
		};
	}

	@Override
	public String toString() {
		return name;
	}
}
