package com.ly.mina.designpattern.filterchain;

public abstract class   Filter {

	Filter preFilter;
	Filter nextFilter;

	public Filter(Filter preFilter,Filter nextFilter){
		this.nextFilter = nextFilter;
		this.preFilter = preFilter;
	}
	public Filter(){
	};

	public abstract  void fireEvent(Filter filter,Object msg);
}
