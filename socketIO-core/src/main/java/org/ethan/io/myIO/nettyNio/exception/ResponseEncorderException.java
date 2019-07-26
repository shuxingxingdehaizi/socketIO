package org.ethan.io.myIO.nettyNio.exception;

public class ResponseEncorderException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -457880392610464206L;
	
	private String msg;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public ResponseEncorderException(String msg) {
		super(msg);
		this.msg = msg;
	}
	
	public ResponseEncorderException(String msg,Exception e) {
		super(msg,e);
		this.msg = msg;
	}
}
