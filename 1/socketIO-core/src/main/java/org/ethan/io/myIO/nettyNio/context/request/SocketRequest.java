package org.ethan.io.myIO.nettyNio.context.request;


public class SocketRequest {
	
	/**
	 * 原始请求数据
	 */
	private Object originRequestMsg;
	
	/**
	 * 解码后的请求数据对象
	 */
	private Object requestMessage;

	public SocketRequest(Object originRequestMsg, Object requestMessage) {
		super();
		this.originRequestMsg = originRequestMsg;
		this.requestMessage = requestMessage;
	}

	public Object getRequestMessage() {
		return requestMessage;
	}
}
