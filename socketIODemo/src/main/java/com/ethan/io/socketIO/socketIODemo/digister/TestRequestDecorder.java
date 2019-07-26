package com.ethan.io.socketIO.socketIODemo.digister;

import java.io.UnsupportedEncodingException;

import org.ethan.io.myIO.nettyNio.digester.RequestDecorder;
import org.ethan.io.myIO.nettyNio.exception.RequestDecordException;
import org.springframework.stereotype.Service;

import com.ethan.io.socketIO.socketIODemo.web.form.TestForm;

import io.netty.buffer.ByteBuf;

@Service
public class TestRequestDecorder implements RequestDecorder<TestForm> {

	@Override
	public TestForm decord(Object request) throws RequestDecordException{
		// TODO Auto-generated method stub
		if(request == null) {
			throw new RequestDecordException("request is empty");
		}
		if(request instanceof String) {
			request = (String)request;
		}else if(request instanceof ByteBuf){
				ByteBuf buf = (ByteBuf) request;
			request = getMessage(buf);
		}
		
		String[]requrestStrArr = ((String)request).split(" ");//假设demo报文由两部分组成，用空格分隔
		TestForm form = new TestForm();
		form.setCommand(requrestStrArr[0]);
		if(requrestStrArr.length == 2) {
			form.setContent(requrestStrArr[1]);
		}
		return form;
	}
	
	private String getMessage(ByteBuf buf) {
        byte[] con = new byte[buf.readableBytes()];
        buf.readBytes(con);
        try {
            return new String(con, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
