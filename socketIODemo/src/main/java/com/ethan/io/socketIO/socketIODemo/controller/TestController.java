package com.ethan.io.socketIO.socketIODemo.controller;

import org.ethan.io.myIO.nettyNio.annotation.Controller;
import org.ethan.io.myIO.nettyNio.annotation.RequestMapping;
import org.springframework.stereotype.Service;

@Controller(name="testController")
@Service
public class TestController {
	
	@RequestMapping(key="test0")
	public String testHelloWorld(String request) {
		return "test0---Hello:" + request;
	}
}
