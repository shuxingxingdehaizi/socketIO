package org.ethan.io.myIO.util;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
	private static Properties serverP = new Properties();
	
	private static Properties clientP = new Properties();
	
	static {
		try {
			serverP.load(PropertiesUtil.class.getResourceAsStream("/serverConfig.properties"));
			clientP.load(PropertiesUtil.class.getResourceAsStream("/clientConfig.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getServerConfig(String key) {
		return serverP.getProperty(key);
	}
	
	public static String getClientConfig(String key) {
		return clientP.getProperty(key);
	}
	
	public static void main(String[] args) {
		System.out.println(serverP.get("serverKeyStore"));
	}
}
