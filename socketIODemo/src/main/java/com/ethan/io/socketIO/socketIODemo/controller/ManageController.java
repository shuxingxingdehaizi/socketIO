package com.ethan.io.socketIO.socketIODemo.controller;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ManageController /*implements ApplicationContextAware*/{
	
	@RequestMapping("/manage")
	public String manage() {
		
		return "manage";
	}

//	@Override
//	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//		// TODO Auto-generated method stub
//		System.out.println("111111111111111111111111111111111111");
//	}
	
}
