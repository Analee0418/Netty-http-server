package com.lee.arphoto.spring.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 服务器应用上下文
 * 
 * @author xiaofei.lee(13651027213@163.com)
 *
 */
public class AppContext {

	private static ApplicationContext context;

	public static void initialize() {
		if (context != null) {
			return;
		}
		context = new ClassPathXmlApplicationContext("classpath*:applicationContext*.xml");
	}

	public static ApplicationContext getContext() {
		return context;
	}
}
