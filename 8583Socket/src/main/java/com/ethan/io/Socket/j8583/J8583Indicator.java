package com.ethan.io.Socket.j8583;

import org.ethan.io.myIO.nettyNio.route.RouteIndicator;
import org.ethan.my8583.cnMessage;
import org.springframework.stereotype.Service;

@Service
public class J8583Indicator implements RouteIndicator {

	@Override
	public String getRouteIndicator(Object request) {
		// TODO Auto-generated method stub
		//报文中的域3为交易处理码，它标志了该报文适用于处理何种交易，
		//6位定长数字字符
		return ((cnMessage)request).getField(3).getValue()+"";
	}

}
