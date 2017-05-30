package com.ly.mina.designpattern.filterchain.filterchinatest;


/***
 * 
 * 责任链  将请求处理模块化 ,分割成各个小模块的过滤器. 请求按顺序经过各个filter 最终达到业务逻辑代码handler. 在filter中会把请求传递给下一个filter 
 * @author liyang
 * 
 */
public class FilterChain {
	
	private Entry head;
	
	private Entry tail;
	
	public FilterChain(){
		head = new Entry(null, null, "headerEntry", new HeadIoFilter());
		tail = new Entry(head, null, "tailEntry", new TailIoFilter());
		head.nextEntry = tail;
	}
	
	public void addLast(String name,IoFilter ioFilter){
		Entry preInsertEntry = tail.preEntry;
		Entry entry = new Entry(preInsertEntry,tail,name,ioFilter);
		
		//建立链接关系
		preInsertEntry.nextEntry = entry;
		tail.preEntry = entry;
	}
		
	public void fireSessioncreated(){
		IoFilter ioFilter = head.ioFilter;
//		Entry nextEntry = head.nextEntry;
//		ioFilter.sessionCreated(nextEntry);
		NextFilter nextFilter = head.nextFilter;
		ioFilter.sessionCreated(nextFilter);
	}
	
	
	class HeadIoFilter implements IoFilter{
		
		@Override
		public void sessionCreated(NextFilter nextFilter){
			System.out.println("head filter before");
			nextFilter.sessionCreated();
			System.out.println("head filter after");
		}
		
		
		@Override
		public void sessionCreated(Entry nextEntry) {
			System.out.println("head filter before");
			IoFilter ioFilter = nextEntry.ioFilter;
			ioFilter.sessionCreated(nextEntry.nextEntry);
			System.out.println("head filter after");
		}
	}
	
	class  TailIoFilter implements IoFilter{
		
		@Override
		public void sessionCreated(NextFilter nextFilter){
			
		}

		@Override
		public void sessionCreated(Entry nextFilter) {
			
		}
	}
	
	public static void main(String[] args) {
		
		FilterChain chain = new FilterChain();
		
		chain.addLast("filterA", new IoFilter() {
			@Override
			public void sessionCreated(Entry nextEntry) {
				System.out.println("filterA before");
				IoFilter ioFilter = nextEntry.ioFilter;
				ioFilter.sessionCreated(nextEntry.nextEntry);
				System.out.println("filterA after");
			}
			@Override
			public void sessionCreated(NextFilter nextFilter){
				System.out.println("filterA before");
				nextFilter.sessionCreated();
				System.out.println("filterA after");
			}
		});
		
		chain.addLast("filterB", new IoFilter() {
			
			@Override
			public void sessionCreated(Entry nextEntry) {
				System.out.println("filterB before");
				IoFilter ioFilter = nextEntry.ioFilter;
				ioFilter.sessionCreated(nextEntry.nextEntry);
				System.out.println("filterB after");
			}
			
			@Override
			public void sessionCreated(NextFilter nextFilter){
				System.out.println("filterB before");
				nextFilter.sessionCreated();
				System.out.println("filterB after");
			}
		});
		
		chain.fireSessioncreated();
	}
	
}
