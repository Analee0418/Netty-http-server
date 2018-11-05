package com.lee.arphoto.spring.config.server;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Server properties
 * 
 * @author xiaofei.lee(13651027213@163.com)
 *
 */
@Component
public class AppServerProperties implements InitializingBean {

	private static AppServerProperties INSTANCE;

	/**
	 * 服务器id
	 */
	@Value("#{serverProperties['id']}")
	private int id;

	/**
	 * 游戏端口
	 */
	@Value("#{serverProperties['port']}")
	private int port;

	@Value("#{serverProperties['fileUploadMaxSize']}")
	private int fileUploadMaxSize;

	@Value("#{serverProperties['downloadPath']}")
	private String downloadPath;

	@Value("#{serverProperties['cdnServerAddress']}")
	private String cdnServerAddress;

	public static int getServerId() {
		return INSTANCE.id;
	}

	public static int getPort() {
		return INSTANCE.port;
	}

	public static int getFileUploadMaxBytes() {
		return INSTANCE.fileUploadMaxSize * 1024 * 1024;
	}

	public static String getDownloadPath() {
		return INSTANCE.downloadPath;
	}

	public static String getCdnServerAddress() {
		return INSTANCE.cdnServerAddress;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		INSTANCE = this;
	}
}
