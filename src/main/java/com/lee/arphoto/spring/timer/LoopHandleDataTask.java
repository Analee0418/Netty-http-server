package com.lee.arphoto.spring.timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lee.arphoto.service.UserService;

/**
 * 循环(间隔时间)处理数据任务
 * 
 */
@Service
public class LoopHandleDataTask implements InitializingBean {
	private static final Log logger = LogFactory.getLog(LoopHandleDataTask.class);

	@Autowired
	private UserService userService;

	/**
	 * 处理每隔10分钟需要处理的任务
	 */
	public void dealOneHourLoopData() {
		if (logger.isInfoEnabled()) {
			logger.info(".");
		}

		userService.loopSaveToDB();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("");
	}

}
