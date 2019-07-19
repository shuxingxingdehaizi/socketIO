package org.ethan.io.myIO.nettyNio.handler;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.ethan.io.myIO.nettyNio.context.SocketRequestContext;
import org.ethan.io.myIO.nettyNio.context.SpringContextHolder;
import org.ethan.io.myIO.nettyNio.context.request.SocketRequest;
import org.ethan.io.myIO.nettyNio.context.response.SocketResponse;
import org.ethan.io.myIO.nettyNio.digester.RequestDecorder;
import org.ethan.io.myIO.nettyNio.digester.ResponseEncorder;
import org.ethan.io.myIO.nettyNio.filter.SocketFilter;
import org.ethan.io.myIO.nettyNio.filter.SocketFilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@Component
public class MyServerHandler extends ChannelInboundHandlerAdapter {
	
	@Autowired
	private SpringContextHolder springContextHolder;
	
	private List<SocketFilter>filters;
	
	private ApplicationContext springContext;
	
	private RequestDecorder requestDecorder;
	
	private ResponseEncorder responseEncorder;
	
	public MyServerHandler() {
		// TODO Auto-generated constructor stub
		getSpringContext();
		
		initFilterChain();
		
		initDisister();
	}

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
		//1.读取请求,解码
		Object request = "";
		if(this.requestDecorder == null) {//如未配置解码器，使用默认解码方式
			
			if(msg instanceof String) {
				request = (String)msg;
			}else if(msg instanceof ByteBuf){
				ByteBuf buf = (ByteBuf) msg;
				request = getMessage(buf);
			}
		}else {
			request = this.requestDecorder.decord(msg); 
		}
		
		//2.组装请求上下文
		SocketRequestContext requestContext = new SocketRequestContext(ctx,new SocketRequest(msg,request),new SocketResponse(ctx.channel(), "UTF-8"));
		
		//3.前置过滤器
		if(this.filters != null && !this.filters.isEmpty()) {
        	SocketFilterChain chain = new SocketFilterChain(this.filters);
            chain.doPreFilter(chain, requestContext);
        }
		//4.将请求路由至对应Controller,获取响应
		Object response = null;
		
		//5.后置过滤器
		if(this.filters != null && !this.filters.isEmpty()) {
        	SocketFilterChain chain = new SocketFilterChain(this.filters);
            chain.doAfterFilter(chain, response);
        }
		
		byte[] responseBytes = null;
		//6.响应编码
		if(this.responseEncorder == null){//使用默认编码方式
			
		}else {
			responseBytes = this.responseEncorder.encord(response);
		}
		
		//5.业务逻辑处理...
        System.out.println(Thread.currentThread().getName()+"-----Received data from client:" + request);
        
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
	
	private void getSpringContext() {
		springContext = springContextHolder.getSpringApplicationContext();
	}
	
	private void initFilterChain() {
        Map<String,SocketFilter>filterMaps = springContext.getBeansOfType(SocketFilter.class);
        if(filters != null && !filters.isEmpty()) {
        	this.filters = new ArrayList<SocketFilter>();
        	for(Map.Entry<String,SocketFilter>ent : filterMaps.entrySet()) {
        		this.filters.add(ent.getValue());
        	}
        	
        	//对拦截器排序
        	filters.sort(new Comparator<SocketFilter>() {
				@Override
				public int compare(SocketFilter o1, SocketFilter o2) {
					// TODO Auto-generated method stub
					return o1.getOrder() - o2.getOrder();
				}
			});
        }
	}
	
	private void initDisister() {
		requestDecorder = springContext.getBean(RequestDecorder.class);
		responseEncorder = springContext.getBean(ResponseEncorder.class);
	}
}
