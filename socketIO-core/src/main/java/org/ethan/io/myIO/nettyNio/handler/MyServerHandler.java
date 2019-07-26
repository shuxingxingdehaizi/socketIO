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
import org.ethan.io.myIO.nettyNio.route.RouteIndicator;
import org.ethan.io.myIO.nettyNio.route.SocketRequestRouter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandler.Sharable;

@Component
@Sharable
public class MyServerHandler extends ChannelInboundHandlerAdapter implements InitializingBean{
	
	@Autowired
	private SpringContextHolder springContextHolder;
	
	@Autowired
	private SocketRequestRouter requestRouter;
	
	private List<SocketFilter>filters;
	
	private ApplicationContext springContext;
	
	private RequestDecorder requestDecorder;
	
	private ResponseEncorder responseEncorder;
	
	private RouteIndicator routeIndicator;
	
	
	
	public MyServerHandler() {}

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
		Object request = this.requestDecorder.decord(msg);
		
		SocketRequest socketRequest = new SocketRequest(msg,request);
		
		SocketResponse socketResponse = new SocketResponse(ctx.channel(), "UTF-8");
		
		
		//2.组装请求上下文
		SocketRequestContext requestContext = new SocketRequestContext(ctx,socketRequest,socketResponse);
		
		//3.前置过滤器
		if(this.filters != null && !this.filters.isEmpty()) {
        	SocketFilterChain chain = new SocketFilterChain(this.filters);
            chain.doPreFilter(chain, requestContext);
        }
		
		//4.将请求路由至对应Controller,获取响应
		SocketRequestRouter.ControllerMethod cm = requestRouter.getControllerMethod(this.routeIndicator.getRouteIndicator(socketRequest.getRequestMessage()));
		if(cm == null) {
			ctx.channel().writeAndFlush("No mapping found").sync();
			return;
		}
		Object response = cm.getMethod().invoke(cm.getControllerBean(), socketRequest.getRequestMessage());
		
		//5.后置过滤器
		socketResponse.setResponseMsg(response);
		
		SocketRequestContext responseContext = new SocketRequestContext(ctx, socketRequest, socketResponse);
		if(this.filters != null && !this.filters.isEmpty()) {
        	SocketFilterChain chain = new SocketFilterChain(this.filters);
            chain.doAfterFilter(chain, responseContext);
        }
		
		//6.响应编码
		byte[] responseBytes = this.responseEncorder.encord(response);
        
        //3.生成响应，发送至客户端
        ctx.channel().writeAndFlush(responseBytes).sync();
       
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
	
	private void initDigister() {
		Map<String,RequestDecorder>requestDecorders = springContext.getBeansOfType(RequestDecorder.class);
		if(requestDecorders.size() == 1) {
			requestDecorder = (RequestDecorder) requestDecorders.values().toArray()[0];//未配置解码器，使用默认解码器
		}else {
			for(Map.Entry<String, RequestDecorder>ent : requestDecorders.entrySet()) {
				if(!ent.getKey().equals("defaultRequestDecorder")) {
					requestDecorder = ent.getValue();
					break;
				}
			}
		}
		System.out.println("Use "+requestDecorder.getClass().getName()+" as requestDecorder");
		
		Map<String,ResponseEncorder>responseEncorders = springContext.getBeansOfType(ResponseEncorder.class);
		if(responseEncorders.size() == 1) {//未配置编码器，使用默认编码器
			responseEncorder = (ResponseEncorder) responseEncorders.values().toArray()[0];
		}else {
			for(Map.Entry<String, ResponseEncorder>ent : responseEncorders.entrySet()) {
				if(!ent.getKey().equals("defaultResponseEncorder")) {
					responseEncorder = ent.getValue();
					break;
				}
			}
		}
		System.out.println("Use "+responseEncorder.getClass().getName()+" as responseEncorder");
	}
	
	private void initRouteIndicator() {
		Map<String,RouteIndicator>routeIndicators = springContext.getBeansOfType(RouteIndicator.class);
		if(routeIndicators.size() == 1) {
			routeIndicator = (RouteIndicator)routeIndicators.values().toArray()[0];
		}else {
			for(Map.Entry<String, RouteIndicator>ent : routeIndicators.entrySet()) {
				if(!ent.getKey().equals("defaultIndicator")) {
					routeIndicator = ent.getValue();
					System.out.println("Set routeIndicator as "+ent.getKey());
					break;
				}
			}
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		getSpringContext();
		
		initFilterChain();
		
		initDigister();
		
		initRouteIndicator();
	}
}
