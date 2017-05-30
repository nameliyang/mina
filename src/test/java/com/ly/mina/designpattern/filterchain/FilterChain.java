package com.ly.mina.designpattern.filterchain;

public class FilterChain {
	Target target;
	HeadFilter headFilter;
	TailFilter tailFilter;

	public FilterChain(Target target){
		this.target = target;
		headFilter = new HeadFilter(null,null);
		tailFilter = new TailFilter(headFilter,null);
		headFilter.nextFilter = tailFilter;
	}

	public void fireEvent(Object msg){
		headFilter.fireEvent(headFilter.nextFilter,msg);
	}

	public void addLast(Filter filter){
		Filter preFilter = tailFilter.preFilter;
		//设置 filter 的前后接点索引
		filter.nextFilter = tailFilter;
		filter.preFilter  = preFilter;

		//
		tailFilter.preFilter = filter;
		preFilter.nextFilter = filter;
	}


	class TailFilter extends Filter{
		public TailFilter(Filter preFilter,Filter nextFilter){
			super(preFilter,nextFilter);
		}
		@Override
		public void fireEvent(Filter filter, Object msg) {
			System.out.println("tail filter ");
		}
	}

	class HeadFilter extends Filter{
		public HeadFilter(Filter preFilter,Filter nextFilter){
			super(preFilter,nextFilter);
		}

		@Override
		public void fireEvent(Filter filter, Object msg) {
			System.out.println("do header filter before");
			//filter.fireEvent(nextFilter,msg);
			nextFilter.fireEvent(null,msg);
			System.out.println("do header filter after");
		}

	}

	public static void main(String[] args) {

		FilterChain filterChain = new FilterChain(null);

		filterChain.addLast(new Filter() {
			@Override
			public void fireEvent(Filter filter, Object msg) {
				System.out.println("do logging filer before");
//				filter.fireEvent(nextFilter.nextFilter,msg);
				nextFilter.fireEvent(null,msg);
				System.out.println("do logging filer after");
			}
		});
		filterChain.fireEvent("hello");
	}

}
