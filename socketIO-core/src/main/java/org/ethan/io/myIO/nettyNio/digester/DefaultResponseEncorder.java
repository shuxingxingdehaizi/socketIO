package org.ethan.io.myIO.nettyNio.digester;

import java.io.UnsupportedEncodingException;

import org.ethan.io.myIO.nettyNio.exception.ResponseEncorderException;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

@Component("defaultResponseEncorder")
public class DefaultResponseEncorder implements ResponseEncorder {

	@Override
	public byte[] encord(Object response) throws ResponseEncorderException{
		// TODO Auto-generated method stub
		if(response == null) {
			return null;
		}
		if(response instanceof String) {
			try {
				return response.toString().getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				throw new ResponseEncorderException("Error occurs when encord response",e);
			}
		}
		if(response instanceof Integer) {
			return int2bytes((int)response);
		}
		try {
			return JSON.toJSONString(response).getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new ResponseEncorderException("Errr occurs when JSON encord response",e);
		}
	}
	
	public static byte[] int2bytes(int i){
		byte[] arr = new byte[4] ;
		arr[0] = (byte)i ;
		arr[1] = (byte)(i >> 8) ;
		arr[2] = (byte)(i >> 16) ;
		arr[3] = (byte)(i >> 24) ;
		return arr ;
	}
	public static  Byte[] shortToBytes(short number) {
        Byte[] bytes = new Byte[2];
        for (int i = 0; i < 2; i++) {
            bytes[i] = (byte) (number >> (2 * i));
        }
        return bytes;
    }

}
