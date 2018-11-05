package com.lee.arphoto;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lee.arphoto.server.disruptor.DisruptorEngine;
import com.lee.arphoto.server.disruptor.consumer.DisruptorEventHandler;
import com.lee.arphoto.server.http.HttpServer;
import com.lee.arphoto.spring.context.AppContext;

/**
 * Main
 * 
 * 
 * @author xiaofei.lee(13651027213@163.com)
 * 
 *         始终相信天道酬勤！
 *
 */
public class App {
	private static final Log logger = LogFactory.getLog(App.class);

	public static void main(String[] args) {
		try {

			// 启动上下文
			AppContext.initialize();

			// 处理队列初始化
			DisruptorEngine.initialization(new DisruptorEventHandler());

			// 死亡回调
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					try {
						logger.info("Http server close.");
					} catch (Exception e) {
						logger.error("stop server error ", e);
					}
				}
			});

			// 启动网络服务
			HttpServer.startup(args);

		} catch (Exception e) {
			logger.error("Startup error:", e);
			System.exit(-1);
		}
	}
}
