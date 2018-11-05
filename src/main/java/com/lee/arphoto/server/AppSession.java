package com.lee.arphoto.server;

import java.util.Date;

public class AppSession {
	private Long userId;
	private String uidSerect;
	private String sessionUUID;
	private Date createTime = new Date();

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUidSerect() {
		return uidSerect;
	}

	public void setUidSerect(String uidSerect) {
		this.uidSerect = uidSerect;
	}

	public String getSessionUUID() {
		return sessionUUID;
	}

	public void setSessionUUID(String sessionUUID) {
		this.sessionUUID = sessionUUID;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
