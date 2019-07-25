package org.ethan.io.myIO.nettyNio.route;

public interface RouteIndicator {
	
	/**
	 * 读取出请求报文中的某些信息，
	 * 框架将根据这些返回信息，决定将请求路由至哪一个Controller
	 * @param request
	 * @return
	 */
	public String getRouteIndicator(Object request);
}
