package org.ethan.io.myIO.nettyNio.route;

import org.springframework.stereotype.Component;

@Component("defaultIndicator")
public class DefaultIndicator implements RouteIndicator{

	@Override
	public String getRouteIndicator(Object request) {
		// TODO Auto-generated method stub
		return (String)request;
	}

}
