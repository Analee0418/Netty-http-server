package com.lee.arphoto.dao.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lee.arphoto.model.User;
import com.lee.arphoto.util.DateUtil;

public class UserDaoProvider {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String updateUserBatch(Map params) {
		Object obj = params.get("userList");
		List<User> userList = new ArrayList<>();

		if (null != obj) {
			userList = (List<User>) obj;
		}

		StringBuilder sb = new StringBuilder();

		for (User user : userList) {
			String lastLoginTime = null != user.getLastLoginTime() ? DateUtil.dateStr(user.getLastLoginTime()) : "";

			String sql = String.format(
					"UPDATE user SET nikeName = '%s', sex = %d, homePicUrl = '%s', lastLoginTime = '%s' WHERE id = %d;",
					user.getNikeName(), user.getSex(), user.getHomePicUrl(), lastLoginTime, user.getId());

			sb.append(sql);
		}

		return sb.toString();
	}
}
