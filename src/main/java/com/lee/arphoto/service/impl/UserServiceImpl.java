package com.lee.arphoto.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lee.arphoto.dao.UserDao;
import com.lee.arphoto.exception.AppErrorException;
import com.lee.arphoto.exception.ErrorCode;
import com.lee.arphoto.model.User;
import com.lee.arphoto.service.UserService;
import com.lee.arphoto.util.FileUtil;
import com.lee.arphoto.util.RandomUtil;

@Service
public class UserServiceImpl implements UserService, InitializingBean {
	private final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private UserDao userDao;

	private AtomicLong maxId;

	private final Map<Long, User> userIdCache = new ConcurrentHashMap<Long, User>();

	private final Map<String, User> userNameCache = new ConcurrentHashMap<String, User>();

	private static final int BATCH_LOAD_SIZE = 1000; // 分批次取，每次取1000人

	@Override
	public void afterPropertiesSet() throws Exception {
		// 加载所有用户基础信息
		loadAllUser();

		// 最大id
		Long _maxId = userDao.getMaxId();
		if (null == _maxId) {
			_maxId = 1000000L;

			if (logger.isInfoEnabled()) {
				logger.info("New server the first user's id has created! [  " + _maxId.longValue() + " ]");
			}
		}
		this.maxId = new AtomicLong(_maxId);
	}

	@Override
	public User getUserById(long uid) {
		return this.userIdCache.get(uid);
	}

	@Override
	public User getUserByName(String userName) {
		return this.userNameCache.get(userName);
	}

	@Override
	public User createNewUser(String userName, String password) {
		User user = new User();

		User existUser = this.getUserByName(userName);
		if (null == existUser) {
			existUser = userDao.getUserByUserName(userName);
		}

		// 是否已经有账号信息，但缺少role数据
		if (null != existUser) {
			throw new AppErrorException(ErrorCode.ErrorRepeateUserNameRegist);
			// user = existUser;
			// user.setPassword(password);
			// user.setLastLoginTime(new Date()); // 最后一次登录时间
		}
		// 玩家没有账号需要新建账号数据
		else {
			user.setId(this.maxId.incrementAndGet());
			user.setUserName(userName);
			user.setPassword(password);
			user.setNikeName("游客" + RandomUtil.getRandStringEx(8));
			user.setCreateTime(new Date());
			user.setLastLoginTime(new Date()); // 最后一次登录时间
			int count = userDao.insertUser(user);
			if (count < 0) {
				logger.error("Error: MySQL Create user failed! user=" + user);
				throw new AppErrorException(ErrorCode.ErrorServer);
			}
		}

		this.addUserToCache(user);
		return user;
	}

	private void addUserToCache(User user) {
		userIdCache.put(user.getId(), user);
		userNameCache.put(user.getUserName(), user);

		if (logger.isDebugEnabled()) {
			logger.debug("Add user to cache. user = " + user);
		}
	}

	private void loadAllUser() {
		long cursor = 0;

		if (logger.isInfoEnabled()) {
			logger.info("Start load all user data.");
		}
		while (true) {
			List<User> userList = userDao.getUserBatch(cursor, BATCH_LOAD_SIZE);
			for (User user : userList) {
				addUserToCache(user);

				if (cursor < user.getId()) {
					cursor = user.getId();
				}
			}

			if (userList.size() < BATCH_LOAD_SIZE) {
				break;
			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("Finish loaded all user data. all size=" + this.userIdCache.size());
		}
	}

	@Override
	public void updateLastLoginTime(User user) {
		user.setLastLoginTime(new Date());
		userDao.updateLastLoginTime(user);
	}

	@Override
	public void loopSaveToDB() {
		List<User> list = new ArrayList<>();
		for (Long id : this.userIdCache.keySet()) {
			User user = userIdCache.get(id);
			if (null == user) {
				continue;
			}
			if (!user.needUpdate) {
				continue;
			}

			user.setNikeName(FileUtil.filterEmoji(user.getNikeName()));
			list.add(user);
			user.needUpdate = false;
		}

		if (list == null || list.isEmpty()) {
			return;
		}

		userDao.updateUserBatch(list);
	}

}
