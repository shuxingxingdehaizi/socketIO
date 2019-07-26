package com.ethan.io.Socket.j8583;

import java.io.IOException;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.ethan.io.myIO.nettyNio.digester.ResponseEncorder;
import org.ethan.io.myIO.nettyNio.exception.ResponseEncorderException;
import org.ethan.my8583.cnMessage;

public class J8583ResponseEncorder implements ResponseEncorder<cnMessage> {

	@Override
	public byte[] encord(cnMessage response) throws ResponseEncorderException {
		// TODO Auto-generated method stub
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			response.write(out, 4, 10);
		} catch (IOException e) {
			throw new ResponseEncorderException("Error occurs when encord response(8583)",e);
		}
		return out.toByteArray();
	}

}
