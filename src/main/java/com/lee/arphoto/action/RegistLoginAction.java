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
 * 注册新用户
 * 
 * { userName:xxx, password:xxx }
 *
 */
@Controller
public class RegistLoginAction extends AbstractAction {

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
			throw new AppErrorException(ErrorCode.ErrorParameter, "userName");
		}

		User user = userService.createNewUser(userName, password); // 创建账号数据
		AppSession session = SessionCache.createSession(user.getId()); // 登录状态

		MessageOnLogin msg = new MessageOnLogin(user.getId(), session.getSessionUUID(), user.getNikeName(),
				user.getSex(), user.getHomePicUrl());
		return msg;
	}

}
