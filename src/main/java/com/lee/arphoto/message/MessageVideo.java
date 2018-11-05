package com.lee.arphoto.message;

import com.lee.arphoto.model.Video;

public class MessageVideo {
	private Long id;
	// private String fileName;
	private String downloadPath;
	// private Date createTime;

	public MessageVideo(Video video) {
		this.id = video.getId();
		this.downloadPath = video.getDownloadPath();
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
