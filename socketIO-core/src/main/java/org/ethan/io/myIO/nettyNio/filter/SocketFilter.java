package org.ethan.io.myIO.nettyNio.filter;

import org.ethan.io.myIO.nettyNio.context.SocketRequestContext;
import org.springframework.core.Ordered;

public interface SocketFilter extends Ordered{
	public void preFilter(SocketFilterChain chain,SocketRequestContext context);
	
	public void afterFilter(SocketFilterChain chain,SocketRequestContext context);
}
