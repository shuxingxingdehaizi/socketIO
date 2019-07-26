package com.ethan.io.socketIO.socketIODemo.web.controller;

import org.ethan.io.myIO.nettyNio.annotation.Controller;
import org.ethan.io.myIO.nettyNio.annotation.RequestMapping;
import org.springframework.stereotype.Service;

import com.ethan.io.socketIO.socketIODemo.web.form.TestForm;
import com.ethan.io.socketIO.socketIODemo.web.vo.TestResponse;

@Controller(name="testController")
@Service
public class TestController {
	
	@RequestMapping(key="test0")
	public String test0HelloWorld(TestForm request) {
		return "test0---Hello:" + request.getContent();
	}
	
	@RequestMapping(key="test1")
	public TestResponse test1HelloWorld(TestForm request) {
		return new TestResponse(request.getContent(), 11);
	}
}
