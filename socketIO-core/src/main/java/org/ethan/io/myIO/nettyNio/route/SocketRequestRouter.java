package org.ethan.io.myIO.nettyNio.route;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.ethan.io.myIO.nettyNio.annotation.Controller;
import org.ethan.io.myIO.nettyNio.annotation.RequestMapping;
import org.ethan.io.myIO.nettyNio.context.SpringContextHolder;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class SocketRequestRouter implements InitializingBean,DisposableBean{
	
	/**
	 * 
	 */
	private ConcurrentHashMap<String, ControllerMethod>routMap;
	
	@Autowired
	private SpringContextHolder springContextHolder;
	
	private ApplicationContext springContext;
	
	public ControllerMethod getControllerMethod(String indicator) {
		return routMap == null ? null : routMap.get(indicator);
	}
	
	public class ControllerMethod{
		Object controllerBean;
		
		Method method;

		public ControllerMethod(Object controllerBean, Method method) {
			super();
			this.controllerBean = controllerBean;
			this.method = method;
		}

		public Object getControllerBean() {
			return controllerBean;
		}

		public void setControllerBean(Object controllerBean) {
			this.controllerBean = controllerBean;
		}

		public Method getMethod() {
			return method;
		}

		public void setMethod(Method method) {
			this.method = method;
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		springContext = springContextHolder.getSpringApplicationContext();
		initRoutMap();
	} 
	
	private void initRoutMap() {
		Map<String,Object> controllers = springContext.getBeansWithAnnotation(Controller.class);
		springContext.getBean("testController").getClass().getAnnotations();
		if(controllers == null || controllers.isEmpty()) {
			return;
		}
		for(Map.Entry<String,Object>ent : controllers.entrySet()) {
			String controllerName = ent.getKey();
			Method[] ms = ent.getValue().getClass().getMethods();
			if(ms != null && ms.length>0) {
				for(Method m : ms) {
					if(m.isAnnotationPresent(RequestMapping.class)) {
						RequestMapping map = m.getAnnotation(RequestMapping.class);
						String indicator =  map.key();
						ControllerMethod cm = new ControllerMethod(ent.getValue(),m);
						if(this.routMap == null) {
							this.routMap = new ConcurrentHashMap<String, SocketRequestRouter.ControllerMethod>();
						}
						this.routMap.put(indicator, cm);
						System.out.println("Mapped ["+indicator+"] to controller ["+controllerName+"]:"+ ent.getValue().getClass().getName()+"."+m.getName());
					}
				}
			}
		}
		System.out.println("routMap init complete");
	}

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		this.routMap.clear();
	}
	
	
}
