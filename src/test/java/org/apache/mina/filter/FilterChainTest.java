package org.apache.mina.filter;

import org.apache.mina.core.filterchain.DefaultIoFilterChain;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.DummySession;
import org.apache.mina.core.session.IoSession;
import org.junit.Test;

public class FilterChainTest {

	@Test
	public void defaultIoFilterChainTest(){
		DummySession session = new DummySession();
		DefaultIoFilterChain chain = new DefaultIoFilterChain(session);
//		chain.addLast("logger", new LoggingFilter());

		chain.addLast("filterA",new FilterA());
		chain.addLast("filterB",new FilterB());
		chain.fireSessionCreated();
//		chain.addLast(
//						"codec",
//						new ProtocolCodecFilter(new TextLineCodecFactory(
//								Charset.forName("UTF-8") )));
//		chain.fireMessageReceived(buffer);
	}
	static class FilterA extends IoFilterAdapter{
		@Override
		public void sessionCreated(NextFilter nextFilter, IoSession session) throws Exception {
			System.out.println("filterA sessionCreated...");
			nextFilter.sessionCreated(session);
		}
	}
	static class FilterB extends IoFilterAdapter{
		@Override
		public void sessionCreated(NextFilter nextFilter, IoSession session) throws Exception {
			System.out.println("filterB sessioncreated...");
		}
	}
}
