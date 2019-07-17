package org.ethan.io.myIO.nettyNio.context;

import org.ethan.io.myIO.nettyNio.context.request.SocketRequest;
import org.ethan.io.myIO.nettyNio.context.response.SocketResponse;

import io.netty.channel.ChannelHandlerContext;

public class SocketRequestContext {
	private ChannelHandlerContext channelContext;
	
	private SocketRequest request;
	
	private SocketResponse response;

	public SocketRequest getRequest() {
		return request;
	}

	public SocketResponse getResponse() {
		return response;
	}

	public ChannelHandlerContext getChannelContext() {
		return channelContext;
	}
	
}
