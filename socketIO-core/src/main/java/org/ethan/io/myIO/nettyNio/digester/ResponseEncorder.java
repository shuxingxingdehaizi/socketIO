package org.ethan.io.myIO.nettyNio.digester;

import org.ethan.io.myIO.nettyNio.exception.ResponseEncorderException;

public interface ResponseEncorder<T> {
	
	public byte[] encord(T response) throws ResponseEncorderException;
}
