package org.ethan.io.myIO.nettyNio.ssl;

import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.ethan.io.myIO.util.PropertiesUtil;

import io.netty.util.internal.SystemPropertyUtil;

public class SSLContextFactory {
	private static SSLContext SERVER_CONTEXT;
	
	private static SSLContext CLIENT_CONTEXT;
	
	private static final String PROTOCOL = "TLS";
    
    private static void initServerContext() {
    	 String algorithm = SystemPropertyUtil.get("ssl.KeyManagerFactory.algorithm");
         if (algorithm == null) {
             algorithm = "SunX509";
         }
         SSLContext serverContext;
         try {
             KeyStore ks = KeyStore.getInstance("JKS");
             ks.load(new FileInputStream(PropertiesUtil.getServerConfig("serverKeyStore")), PropertiesUtil.getServerConfig("serverKeyStorePassword").toCharArray());
             KeyStore tks = KeyStore.getInstance("JKS");
             tks.load(new FileInputStream(PropertiesUtil.getServerConfig("serverTrustKeyStore")), PropertiesUtil.getServerConfig("serverTrustKeyStorePassword").toCharArray());
             KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
             TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
             kmf.init(ks, PropertiesUtil.getServerConfig("serverKeyStorePassword").toCharArray());
             tmf.init(tks);
             serverContext = SSLContext.getInstance(PROTOCOL);
             serverContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
         } catch (Exception e) {
             throw new Error(e);
         }
         SERVER_CONTEXT = serverContext;
    }
    
    private static void initClientContext() {
    	String algorithm = SystemPropertyUtil.get("ssl.KeyManagerFactory.algorithm");
        if (algorithm == null) {
            algorithm = "SunX509";
        }
        SSLContext clientContext;
        try {
            KeyStore ks2 = KeyStore.getInstance("JKS");
            ks2.load(new FileInputStream(PropertiesUtil.getClientConfig("clientKeyStore")), PropertiesUtil.getClientConfig("clientKeyStorePassword").toCharArray());
            KeyStore tks2 = KeyStore.getInstance("JKS");
            tks2.load(new FileInputStream(PropertiesUtil.getClientConfig("clientTrustKeyStore")), PropertiesUtil.getClientConfig("clientTrustKeyStorePassword").toCharArray());
            KeyManagerFactory kmf2 = KeyManagerFactory.getInstance(algorithm);
            TrustManagerFactory tmf2 = TrustManagerFactory.getInstance("SunX509");
            kmf2.init(ks2,  PropertiesUtil.getClientConfig("clientKeyStorePassword").toCharArray());
            tmf2.init(tks2);
            clientContext = SSLContext.getInstance(PROTOCOL);
            clientContext.init(kmf2.getKeyManagers(), tmf2.getTrustManagers(), null);
        } catch (Exception e) {
            throw new Error(e);
        }
        CLIENT_CONTEXT = clientContext;
    }
    
    public static SSLContext getServerContext() {
    	if(SERVER_CONTEXT == null) {
    		initServerContext();
    	}
        return SERVER_CONTEXT;
    }
    
    public static SSLContext getClientContext() {
    	if(CLIENT_CONTEXT == null) {
    		initClientContext();
    	}
        return CLIENT_CONTEXT;
    }
}
