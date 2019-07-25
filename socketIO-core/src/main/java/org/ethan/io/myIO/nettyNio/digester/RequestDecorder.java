package org.ethan.io.myIO.nettyNio.digester;

public interface RequestDecorder<T> {
	/**
	 * 将请求报文格式化为对象
	 * request可能是byte[]  也可能是String,取决于ChannelPipeline的配置
	 * @param request
	 * @return
	 */
	public T decord(Object request);
	
}
