package com.lee.arphoto.action;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.lee.arphoto.exception.AppErrorException;
import com.lee.arphoto.exception.ErrorCode;
import com.lee.arphoto.message.MessageUseInfo;
import com.lee.arphoto.model.User;
import com.lee.arphoto.service.UserService;
import com.lee.arphoto.util.FileUtil;

/**
 * 更新用户信息
 * 
 * { uid:1001, session:xxx, nikeName:xxx, sex:xxx }
 *
 */
@Controller
public class UpdateUserInfoAction extends AbstractAction {

	@Autowired
	private UserService userService;

	@Override
	public boolean isEasy() {
		return true;
	}

	@Override
	protected Object doAction(Map<String, Object> params) {
		Long uid = Long.parseLong(params.get("uid").toString());
		User user = userService.getUserById(uid);
		if (null == user) {
			throw new AppErrorException(ErrorCode.ErrorParameter);
		}

		String nikeName = null;
		if (params.containsKey("nikeName")) {
			nikeName = params.get("nikeName").toString();
			nikeName = FileUtil.filterEmoji(nikeName);
			user.setNikeName(nikeName);
			user.needUpdate = true;
		}
		byte sex = -1;
		if (params.containsKey("sex")) {
			sex = Byte.parseByte(params.get("sex").toString());
			user.setSex(sex);
			user.needUpdate = true;
		}

		MessageUseInfo msg = new MessageUseInfo(user.getNikeName(), user.getSex(), user.getHomePicUrl());
		return msg;
	}

}
