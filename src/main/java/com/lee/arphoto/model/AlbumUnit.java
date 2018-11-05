package com.lee.arphoto.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.lee.arphoto.spring.context.AppContext;

import com.lee.arphoto.dao.PicDao;
import com.lee.arphoto.dao.VideoDao;

public class AlbumUnit extends AbstractDBModel {
	private final Log logger = LogFactory.getLog(this.getClass());

	private Long id;
	private Long picId;
	private String videoId;
	private boolean offCard;
	private String comment;

	private Pic pic;
	private List<Video> videoList = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPicId() {
		return picId;
	}

	public void setPicId(Long picId) {
		this.picId = picId;
	}

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public Pic getPic() {
		return pic;
	}

	public void setPic(Pic pic) {
		this.pic = pic;
	}

	public List<Video> getVideoList() {
		return videoList;
	}

	public void setVideoList(List<Video> videoList) {
		this.videoList = videoList;
	}

	public boolean isOffCard() {
		return offCard;
	}

	public void setOffCard(boolean offCard) {
		this.offCard = offCard;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void afterLoadDB() {
		if (StringUtils.isEmpty(this.videoId)) {
			logger.error("", new RuntimeException("该相册单元没有上传视频! albumUnit.id=" + this.getId()));
			return;
		}
		String[] videoIdStrs = this.videoId.split(",");
		List<Long> videoIdList = new ArrayList<>();
		for (String vidStr : videoIdStrs) {
			if (StringUtils.isEmpty(vidStr)) {
				continue;
			}
			try {
				Long videoId = Long.parseLong(vidStr);
				videoIdList.add(videoId);
			} catch (Exception e) {
			}
		}

		if (videoIdList.size() <= 0) { // 数据有误，没有任何视频
			logger.error("", new RuntimeException("该相册单元没有上传视频! albumUnit.id=" + this.getId()));
			return;
		}

		PicDao picDao = AppContext.getContext().getBean(PicDao.class);
		this.pic = picDao.getPicById(this.picId);
		if (null == pic) {
			logger.error("", new RuntimeException("该相册单元没有上传照片! albumUnit.id=" + this.getId()));
			return;
		}

		VideoDao videoDao = AppContext.getContext().getBean(VideoDao.class);
		for (Long vid : videoIdList) {
			Video video = videoDao.getVideoById(vid);
			if (null == video) {
				continue;
			}
			this.videoList.add(video);
		}
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [ id=" + id + ", picId=" + picId + ", videoId=" + videoId
				+ ", offCard=" + offCard + ", comment=" + comment + ", pic=" + pic + ", videoList=" + videoList + "]";
	}

}
