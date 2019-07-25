package org.ethan.io.myIO.nettyNio.digester;

public interface ResponseEncorder<T> {
	
	public byte[] encord(T response);
}
