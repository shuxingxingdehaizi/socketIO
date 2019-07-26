package org.ethan.io.myIO.nettyNio.digester;

import java.io.UnsupportedEncodingException;

import org.ethan.io.myIO.nettyNio.exception.RequestDecordException;
import org.springframework.stereotype.Component;

import io.netty.buffer.ByteBuf;

@Component("defaultRequestDecorder")
public class DefaultRequestDecorder implements RequestDecorder{

	@Override
	public Object decord(Object request) throws RequestDecordException {
		// TODO Auto-generated method stub
		if(request == null) {
			return null;
		}
		if(request instanceof String) {
			return request;
		}else if(request instanceof ByteBuf){
				ByteBuf buf = (ByteBuf) request;
			return getMessage(buf);
		}else {
			throw new RequestDecordException("unknow type of request:"+request.getClass().getName());
		}
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
