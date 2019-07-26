package com.ethan.io.Socket.j8583.controller;

import java.io.IOException;

import org.ethan.io.myIO.nettyNio.annotation.Controller;
import org.ethan.io.myIO.nettyNio.annotation.RequestMapping;
import org.ethan.my8583.cnMessage;
import org.ethan.my8583.cnMessageFactory;
import org.ethan.my8583.example.Example;
import org.ethan.my8583.parse.cnConfigParser;
import org.springframework.stereotype.Service;

@Controller(name="8583Controller")
@Service
public class J8583Controller {
	
	
	
	/**
	 * 未选卡中的余额查询
	 * 交易处理码：300000
	 * @throws IOException 
	 */
	@RequestMapping(key="300000")
	public cnMessage balanceQuery1(cnMessage request) throws IOException {
		cnMessageFactory mfact = cnConfigParser.createFromStream(Example.class.getResourceAsStream("/config.xml"));
		return mfact.createResponse(request);
	}
}
