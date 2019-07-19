package org.ethan.io.myIO.nettyNio.filter;

import java.util.List;

import org.ethan.io.myIO.nettyNio.context.SocketRequestContext;

public class SocketFilterChain {
	private List<SocketFilter> filters;
	
	private int currentIndex = 0;
	
	
	
	public SocketFilterChain(List<SocketFilter> filters) {
		super();
		this.filters = filters;
	}



	public void doPreFilter(SocketFilterChain chain,SocketRequestContext context) {
		if(currentIndex < filters.size()) {
			filters.get(currentIndex++).preFilter(chain, context);
		}
	}
	
	public void doAfterFilter(SocketFilterChain chain,SocketRequestContext context) {
		if(currentIndex >= 0) {
			filters.get(currentIndex--).afterFilter(chain, context);
		}
	}
}
