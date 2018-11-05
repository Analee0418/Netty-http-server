package com.lee.arphoto.spring.config.process;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Processor properties
 * 
 * @author xiaofei.lee(13651027213@163.com)
 *
 */
@Component
public class ProcessorProperties implements InitializingBean {

	private static ProcessorProperties INSTANCE;

	@Value("#{serverProperties['CORE_POOL_SIZE']}")
	private int corePoolSize;

	@Value("#{serverProperties['MAX_POOL_SIZE']}")
	private int maximumPoolSize;

	@Value("#{serverProperties['KEEP_ALIVE_TIME_SECONDS']}")
	private int keepAliveTimeSeconds;

	public static int getCorePoolSize() {
		return INSTANCE.corePoolSize;
	}

	public static int getMaximumPoolSize() {
		return INSTANCE.maximumPoolSize;
	}

	public static int getKeepAliveTimeSeconds() {
		return INSTANCE.keepAliveTimeSeconds;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		INSTANCE = this;
	}
}
