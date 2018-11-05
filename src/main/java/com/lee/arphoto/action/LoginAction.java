package com.lee.arphoto.action;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import com.lee.arphoto.action.cache.SessionCache;
import com.lee.arphoto.exception.AppErrorException;
import com.lee.arphoto.exception.ErrorCode;
import com.lee.arphoto.message.MessageOnLogin;
import com.lee.arphoto.model.User;
import com.lee.arphoto.server.AppSession;
import com.lee.arphoto.service.UserService;

/**
 * 用户登录
 * 
 * { userName:xxx, password:xxx }
 *
 */
@Controller
public class LoginAction extends AbstractAction {

	@Autowired
	private UserService userService;

	@Override
	public boolean isEasy() {
		return true;
	}

	@Override
	protected Object doAction(Map<String, Object> params) {
		String userName = params.get("userName").toString();
		String password = params.get("password").toString();
		if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
			throw new AppErrorException(ErrorCode.ErrorParameter);
		}

		User user = userService.getUserByName(userName);
		if (null == user) {
			throw new AppErrorException(ErrorCode.ErrorParameter);
		}

		if (!user.getPassword().equals(password)) { // 密码错误
			throw new AppErrorException(ErrorCode.ErrorPassword);
		}

		userService.updateLastLoginTime(user); // 更新用户登录时间
		AppSession session = SessionCache.createSession(user.getId()); // 登录状态

		MessageOnLogin msg = new MessageOnLogin(user.getId(), session.getSessionUUID(), user.getNikeName(),
				user.getSex(), user.getHomePicUrl());
		return msg;
	}

}
