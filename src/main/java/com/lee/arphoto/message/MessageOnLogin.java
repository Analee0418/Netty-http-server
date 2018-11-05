package com.lee.arphoto.message;

public class MessageOnLogin extends AbstractMessage {
	private Long uid;
	private String session;
	private String nikeName;
	private byte sex; // 性别
	private String homePicUrl; // 主页背景图

	public MessageOnLogin(Long uid, String session, String nikeName, byte sex, String homePicUrl) {
		super();
		this.uid = uid;
		this.session = session;
		this.nikeName = nikeName;
		this.sex = sex;
		this.homePicUrl = homePicUrl;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public String getNikeName() {
		return nikeName;
	}

	public void setNikeName(String nikeName) {
		this.nikeName = nikeName;
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

}
