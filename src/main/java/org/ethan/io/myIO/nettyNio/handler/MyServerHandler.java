package org.ethan.io.myIO.nettyNio.handler;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class MyServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		 System.out.println(ctx.channel().localAddress().toString() + " Chanel Activated!");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(ctx.channel().localAddress().toString() + " Channel deactivated!");
	}

	/**
	 * 服务器收到客户端请求后触发的方法
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		//1.读取请求
		String request = "";
		if(msg instanceof String) {
			request = (String)msg;
		}else if(msg instanceof ByteBuf){
			ByteBuf buf = (ByteBuf) msg;
			request = getMessage(buf);
		}
		//2.业务逻辑处理...
        System.out.println("Received data from client:" + request);
        
        //3.生成响应，发送至客户端
        byte[] response = ("Hello :"+ request).getBytes();
        ctx.channel().writeAndFlush(response).sync();
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

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Data receive complete!");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		ctx.close();
	    System.out.println("Error occurs when handle request:" + cause.getMessage());
	}
}
