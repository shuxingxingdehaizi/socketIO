package org.ethan.io.myIO.nettyNio;


import java.nio.charset.Charset;

import javax.net.ssl.SSLEngine;

import org.ethan.io.myIO.nettyNio.handler.MyServerHandler;
import org.ethan.io.myIO.nettyNio.ssl.SSLContextFactory;
import org.ethan.io.myIO.util.PropertiesUtil;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslHandler;

@Component("nettyNioServer")
public class NettyNioServer implements InitializingBean,DisposableBean{
	private static final int PORT = Integer.valueOf(PropertiesUtil.getServerConfig("port"));
	
	private boolean sslEnable = Boolean.valueOf(PropertiesUtil.getServerConfig("isSSLEnable"));
	
	public NettyNioServer() {
		
	}
	
	public void start() throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		
		EventLoopGroup group = new NioEventLoopGroup();
		
		try {
			ServerBootstrap sb = new  ServerBootstrap();
			sb.option(ChannelOption.SO_BACKLOG, 1024);
			sb.group(bossGroup, group)//绑定线程池
				.channel(NioServerSocketChannel.class)//指定channel
				.localAddress(PORT);
			
			sb.childHandler(new ChannelInitializer<SocketChannel>() {//客户端建立链接时触发操作
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					// TODO Auto-generated method stub
					System.out.println("Got a client connect request: Host:"+ch.localAddress().getHostName()+" Port:"+ch.localAddress().getPort());
					if(sslEnable) {
				        SSLEngine engine = SSLContextFactory.getServerContext().createSSLEngine();
				        engine.setUseClientMode(false);
				        engine.setNeedClientAuth(true);
				        ch.pipeline().addLast("ssl", new SslHandler(engine));
				        
					}
//					ch.pipeline().addLast("framer",new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
					ch.pipeline().addLast("encoder",new StringEncoder(Charset.forName("UTF-8")));//会把服务端返回的字符串编码为字节码
//					ch.pipeline().addLast("decoder", new StringDecoder(Charset.forName("UTF-8")));//会把服务端收的字节码转换为字符串
                    ch.pipeline().addLast("handler",new MyServerHandler()); // 客户端触发操作
                    ch.pipeline().addLast(new ByteArrayEncoder());
			        ch.flush();
				}
			});
			
			
			ChannelFuture cf = sb.bind().sync();// 服务器异步创建绑定
			if(sslEnable) {
				System.out.println("Socket server start success and Listening on port :"+PORT+" with SSL enable");
			}else {
				System.out.println("Socket server start success and Listening on port :"+PORT);
			}
			
			cf.channel().closeFuture().sync(); // 关闭服务器通道
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			group.shutdownGracefully().sync();
			bossGroup.shutdownGracefully().sync();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		new NettyNioServer().start();
	}

	@Override
	public void afterPropertiesSet() throws Exception {//Spring加载完后启动服务器
		// TODO Auto-generated method stub
		new NettyNioServer().start();
	}

	@Override
	public void destroy() throws Exception {//Spring容器销毁时，销毁服务器
		// TODO Auto-generated method stub
		
	}
}
