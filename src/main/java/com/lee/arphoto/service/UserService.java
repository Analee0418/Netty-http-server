package com.lee.arphoto.service;

import com.lee.arphoto.model.User;

public interface UserService {
	public User getUserById(long uid);

	public User getUserByName(String userName);

	public User createNewUser(String userName, String password);

	public void updateLastLoginTime(User user);
	
	public void loopSaveToDB();
}
