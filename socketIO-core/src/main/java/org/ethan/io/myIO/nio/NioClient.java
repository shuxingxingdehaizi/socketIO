package org.ethan.io.myIO.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NioClient {
	private Selector selector;
	
	private BufferedReader clientInput = new BufferedReader(new InputStreamReader(System.in));
	
	public void init() throws IOException {
		this.selector = Selector.open();
		
		SocketChannel channel = SocketChannel.open();
		
		channel.configureBlocking(false);
		channel.connect(new InetSocketAddress("127.0.0.1", 6666));
		
		channel.register(selector, SelectionKey.OP_CONNECT);
	}
	
	public void start() throws IOException {
		while(true) {
			selector.select(); //此方法会阻塞  直到至少有一个已注册的事件发生
			
			Iterator<SelectionKey>ite = this.selector.selectedKeys().iterator();
			
			while(ite.hasNext()) {
				SelectionKey key = ite.next();
				ite.remove();
				if(key.isConnectable()) {
					connect(key);
				}else if(key.isReadable()){
					read(key);
				}
			}
		}
	}
	
	public void connect(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel)key.channel();
		
		if(channel.isConnectionPending()) {//如果正在连接
			if(channel.finishConnect()) {//完成链接
				channel.configureBlocking(false);//设置为非阻塞
				channel.register(this.selector, SelectionKey.OP_READ);//注册读事件
				String request = clientInput.readLine();
				channel.write(ByteBuffer.wrap(request.getBytes("UTF-8")));//发送到服务器
			}else {
				key.cancel();
			}
		}
	}
	
	public void read(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel)key.channel();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		channel.read(buffer);
		String response = new String(buffer.array()).trim();
		System.out.println("Response from server is :"+response);
		String nextRequest = clientInput.readLine();
		ByteBuffer outBuffer = ByteBuffer.wrap(nextRequest.getBytes("UTF-8"));
		channel.write(outBuffer);
	}
	
	public static void main(String[] args) throws IOException {
		NioClient client = new NioClient();
		client.init();
		client.start();
	}
}

