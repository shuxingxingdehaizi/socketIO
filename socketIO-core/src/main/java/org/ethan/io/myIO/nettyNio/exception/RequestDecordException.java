package org.ethan.io.myIO.nettyNio.exception;

public class RequestDecordException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -681459703280055929L;

	private String msg;

	public RequestDecordException(String msg) {
		super(msg);
		this.msg = msg;
	}
	
	public RequestDecordException(String msg,Exception e) {
		super(msg,e);
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
