package com.lee.arphoto.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 玩家数据快照
 */
public class User implements Serializable {
	private static final long serialVersionUID = 8770261677228385301L;

	private long id;
	private String userName;
	private String password;
	private String nikeName;
	private byte sex; // 性别
	private String homePicUrl; // 主页背景图
	private int platformId; // 平台id,不同平台可能有相同的用户token',
	private String platformName;
	private Date createTime;
	private Date lastLoginTime; // 最后一次登录时间

	public boolean needUpdate;
	public UserAlbum userAlbum;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getPlatformId() {
		return platformId;
	}

	public void setPlatformId(int platformId) {
		this.platformId = platformId;
	}

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public byte getSex() {
		return sex;
	}

	public void setSex(byte sex) {
		this.sex = sex;
	}

	public String getHomePicUrl() {
		return homePicUrl;
	}

	public void setHomePicUrl(String homePicUrl) {
		this.homePicUrl = homePicUrl;
	}

	public String getNikeName() {
		return nikeName;
	}

	public void setNikeName(String nikeName) {
		this.nikeName = nikeName;
	}

}