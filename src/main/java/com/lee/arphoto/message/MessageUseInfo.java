package com.lee.arphoto.message;

public class MessageUseInfo extends AbstractMessage {
	private String nikeName;
	private byte sex; // 性别
	private String homePicUrl; // 主页背景图

	public MessageUseInfo(String nikeName, byte sex, String homePicUrl) {
		super();
		this.nikeName = nikeName;
		this.sex = sex;
		this.homePicUrl = homePicUrl;
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
