package org.ethan.io.myIO.nettyNio.context.response;

import java.io.UnsupportedEncodingException;

import io.netty.channel.Channel;

public class SocketResponse {
	private Channel channel;
	
	private String responseCharectorEncoding;
	
	
	
	public SocketResponse(Channel channel, String responseCharectorEncoding) {
		super();
		this.channel = channel;
		this.responseCharectorEncoding = responseCharectorEncoding;
	}

	public void response(String msg){
		try {
			channel.writeAndFlush(msg.getBytes(responseCharectorEncoding));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void response(byte[] msg){
		channel.writeAndFlush(msg);
	}
}
