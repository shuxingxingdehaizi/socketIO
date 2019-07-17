package org.ethan.io.myIO.nettyNio.filter;

public interface SocketFilter {
	public void filter(SocketFilterChain filterChain);
}
