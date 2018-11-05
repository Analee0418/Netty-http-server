package com.lee.arphoto.action;

import java.io.File;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;

import com.lee.arphoto.action.cache.ActionMappingCache;
import com.lee.arphoto.action.cache.SessionCache;
import com.lee.arphoto.exception.AppErrorException;
import com.lee.arphoto.exception.ErrorCode;

/**
 * 
 * AbstractAction 请求Action处理器
 * 
 * @author xiaofei.lee(13651027213@163.com)
 *
 */
public abstract class AbstractAction implements InitializingBean {

	/**
	 * 复杂的逻辑抛给特定线程池处理
	 * 
	 * @return
	 */
	public abstract boolean isEasy();

	public Object handle(Map<String, Object> params, Map<String, File> files) {
		this.verifySession(params);

		Object obj = this.doAction(params, files);
		if (null != obj) {
			return obj;
		}

		return this.doAction(params);
	}

	/**
	 * 处理消息
	 * 
	 * @param session
	 * @param message
	 * @return
	 */
	protected Object doAction(Map<String, Object> params) {
		return null;
	}

	protected Object doAction(Map<String, Object> params, Map<String, File> files) {
		return null;
	}

	public void verifySession(Map<String, Object> params) {
		if (!(this instanceof RegistLoginAction) && !(this instanceof LoginAction)) {
			Object uidObj = params.get("uid");
			if (null == uidObj) {
				throw new AppErrorException(ErrorCode.ErrorParameter);
			}
			Object sessionidObj = params.get("session");
			if (null == sessionidObj) {
				throw new AppErrorException(ErrorCode.ErrorParameter);
			}

			Long uid = Long.parseLong(uidObj.toString());
			String sessionId = sessionidObj.toString();
			SessionCache.verifySession(uid, sessionId);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		ActionMappingCache._pushActionInstanceWithName(this.getClass().getSimpleName(), this);
	}

}
