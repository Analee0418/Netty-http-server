package com.lee.arphoto.action.cache;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.StringUtils;

import com.lee.arphoto.exception.AppErrorException;
import com.lee.arphoto.exception.ErrorCode;
import com.lee.arphoto.server.AppSession;

/**
 * 用户登录状态
 * 
 * @author xiaofei.lee(13651027213@163.com)
 *
 */
public final class SessionCache {
	private final static Map<Long, AppSession> sessions = new ConcurrentHashMap<>();

	public static AppSession createSession(Long uid) {
		AppSession session = new AppSession();
		session.setUserId(uid);
		session.setSessionUUID(UUID.randomUUID().toString());

		sessions.put(uid, session);
		return session;
	}

	public static void verifySession(Long uid, String sessionId) {
		if (StringUtils.isEmpty(sessionId)) { // 提示玩家重新登录
			throw new AppErrorException(ErrorCode.ErrorNoLogin);
		}
		AppSession session = sessions.get(uid);
		if (null == session) { // 提示玩家重新登录
			throw new AppErrorException(ErrorCode.ErrorNoLogin);
		}
		if (!sessionId.equals(session.getSessionUUID())) { // 提示玩家重新登录
			throw new AppErrorException(ErrorCode.ErrorNoLogin);
		}
	}
}
