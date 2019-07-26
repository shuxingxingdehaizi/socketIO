package com.ethan.io.Socket.j8583;

import java.text.ParseException;

import org.ethan.io.myIO.nettyNio.digester.RequestDecorder;
import org.ethan.io.myIO.nettyNio.exception.RequestDecordException;
import org.ethan.my8583.cnMessage;
import org.ethan.my8583.cnMessageFactory;
import org.ethan.my8583.example.Example;
import org.ethan.my8583.parse.cnConfigParser;
import org.springframework.beans.factory.InitializingBean;

public class J8583RequestDecorder implements RequestDecorder<cnMessage>,InitializingBean{

	private cnMessageFactory mfact = null;
	@Override
	public cnMessage decord(Object request) throws RequestDecordException {
		// TODO Auto-generated method stub
		try {
			return mfact.parseMessage((byte[])request, 46);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw new RequestDecordException("Error occurs when decord request(8583)",e);
		}
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		mfact = cnConfigParser.createFromStream(Example.class.getResourceAsStream("/config.xml"));
	}
}
