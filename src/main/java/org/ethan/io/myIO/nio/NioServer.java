package org.ethan.io.myIO.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NioServer {
	public static final int PORT = 6666;
	
	private Selector selector;
	
	public void init() throws IOException {
		this.selector = Selector.open();//创建选择器
		
		ServerSocketChannel channel = ServerSocketChannel.open();
		channel.configureBlocking(false);
		ServerSocket serverSocket = channel.socket();
		
		InetSocketAddress address = new InetSocketAddress("127.0.0.1",PORT);//绑定域名端口
		serverSocket.bind(address);
		
		channel.register(selector, SelectionKey.OP_ACCEPT);//注册accept事件
	}
	
	public void start() throws IOException{
		while(true) {
			this.selector.select();//此方法会阻塞，直到至少有一个已注册的事件
			Iterator<SelectionKey>ite = this.selector.selectedKeys().iterator();
			while(ite.hasNext()) {
				SelectionKey key = ite.next();
				ite.remove();
				if(key.isAcceptable()) {
					accept(key);
				}else if(key.isReadable()) {
					read(key);
				}
			}
		}
	}
	
	public void accept(SelectionKey key) throws IOException {
		ServerSocketChannel server = (ServerSocketChannel)key.channel();
		SocketChannel channel = server.accept();
		channel.configureBlocking(false);
		channel.register(this.selector, SelectionKey.OP_READ);
	}
	
	public void read(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		channel.read(buffer);
		String request = new String(buffer.array(),"UTF-8");
		System.out.println("Request form client is:"+request);
		
		
		String response = "Hello world:"+request;
		ByteBuffer outBuffer = ByteBuffer.wrap(response.getBytes("UTF-8"));
		channel.write(outBuffer);
	}
	
	public static void main(String[] args) throws IOException {
		NioServer server = new NioServer();
		server.init();
		System.out.println("Server listening at port "+PORT);
		server.start();
		
	}
}
