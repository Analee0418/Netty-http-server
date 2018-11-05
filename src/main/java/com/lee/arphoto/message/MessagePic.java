package com.lee.arphoto.message;

import com.lee.arphoto.model.Pic;

public class MessagePic {
	private Long id;
	// private String fileName;
	private String downloadPath;
	// private Date createTime;

	public MessagePic(Pic pic) {
		this.id = pic.getId();
		this.downloadPath = pic.getDownloadPath();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDownloadPath() {
		return downloadPath;
	}

	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}

}
