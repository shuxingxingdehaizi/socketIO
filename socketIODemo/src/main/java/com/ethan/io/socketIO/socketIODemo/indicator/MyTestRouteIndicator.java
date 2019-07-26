package com.ethan.io.socketIO.socketIODemo.indicator;

import org.ethan.io.myIO.nettyNio.route.RouteIndicator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.ethan.io.socketIO.socketIODemo.web.form.TestForm;


@Service
public class MyTestRouteIndicator implements RouteIndicator,InitializingBean,ApplicationContextAware{
//	@Autowired
//	private	NettyNioServer server;

	@Override
	public String getRouteIndicator(Object request) {
		// TODO Auto-generated method stub
		return ((TestForm)request).getCommand();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("MyTestRouteIndicator init");
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
//		applicationContext.getBean("nettyNioServer");
		System.out.println("MyTestRouteIndicator init 11111111");
	}

}
