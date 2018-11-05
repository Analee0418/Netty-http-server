package com.lee.arphoto.service.impl;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lee.arphoto.dao.AlbumUnitDao;
import com.lee.arphoto.dao.PicDao;
import com.lee.arphoto.dao.UserAlbumDao;
import com.lee.arphoto.dao.VideoDao;
import com.lee.arphoto.exception.AppErrorException;
import com.lee.arphoto.exception.ErrorCode;
import com.lee.arphoto.model.AlbumUnit;
import com.lee.arphoto.model.Pic;
import com.lee.arphoto.model.User;
import com.lee.arphoto.model.UserAlbum;
import com.lee.arphoto.model.Video;
import com.lee.arphoto.service.AlbumService;
import com.lee.arphoto.service.UserService;

@Service
public class AlbumServiceImpl implements AlbumService, InitializingBean {
	private final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private UserService userService;

	@Autowired
	private UserAlbumDao userAlbumDao;
	@Autowired
	private AlbumUnitDao albumUnitDao;
	@Autowired
	private VideoDao videoDao;
	@Autowired
	private PicDao picDao;

	private AtomicLong maxPicId;
	private AtomicLong maxVideoId;
	private AtomicLong maxAlbumUnitId;
	private AtomicLong maxUserAlbumId;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 最大picId
		Long _maxPicId = picDao.getMaxId();
		if (null == _maxPicId) {
			_maxPicId = 10000000L;
		}
		this.maxPicId = new AtomicLong(_maxPicId);
		// 最大视频id
		Long _maxVideoId = videoDao.getMaxId();
		if (null == _maxVideoId) {
			_maxVideoId = 10000000L;
		}
		this.maxVideoId = new AtomicLong(_maxVideoId);
		// 最大相册单元id
		Long _maxAlbumUnitId = albumUnitDao.getMaxId();
		if (null == _maxAlbumUnitId) {
			_maxAlbumUnitId = 10000000L;
		}
		this.maxAlbumUnitId = new AtomicLong(_maxAlbumUnitId);
		// 最大用户id
		Long _maxUserAlbumId = userAlbumDao.getMaxId();
		if (null == _maxUserAlbumId) {
			_maxUserAlbumId = 10000000L;
		}
		this.maxUserAlbumId = new AtomicLong(_maxUserAlbumId);
	}

	@Override
	public UserAlbum loadUserAlbum(Long userId) {
		List<UserAlbum> userAlbumList = userAlbumDao.getUserAlbumByUserId(userId);
		UserAlbum album = null;
		if (null != userAlbumList && userAlbumList.size() > 0) {
			album = userAlbumList.get(0);
			album.afterLoadDB();
		}

		return album;
	}

	@Override
	public void createNewPic(Pic pic) {
		this.picDao.insertPic(pic);
	}

	@Override
	public void createNewVideo(Video video) {
		this.videoDao.insertVideo(video);
	}

	@Override
	public AlbumUnit createNewAlbumUnit(Long userId, Pic pic, List<Video> video, boolean offCard, String comment) {
		if (video.size() <= 0) {
			throw new AppErrorException(ErrorCode.ErrorFileUploadVideoInvalid);
		}

		AlbumUnit unit = new AlbumUnit();
		unit.setComment(comment);
		unit.setOffCard(offCard);
		unit.setId(this.maxAlbumUnitId.incrementAndGet());
		unit.setPicId(pic.getId());
		String c = "";
		String videoIdList = "";
		for (Video v : video) {
			if (null == v) {
				continue;
			}
			videoIdList += (c + "" + v.getId());
			c = ",";
		}
		unit.setVideoId(videoIdList);

		int insertResult = albumUnitDao.insertAlbum(unit);
		if (insertResult > 0) {
			unit.setPic(pic);
			unit.setVideoList(video);
		}

		if (logger.isDebugEnabled()) {
			logger.debug(unit);
		}

		UserAlbum userAlbum = loadUserAlbum(userId);
		if (null == userAlbum) {
			userAlbum = new UserAlbum();
			userAlbum.setId(this.maxUserAlbumId.incrementAndGet());
			userAlbum.setUserId(userId);

			this.userAlbumDao.insertUserAlbum(userAlbum);
		}
		AlbumUnit newUnit = userAlbum.addAlbumUnit(unit.getId());

		User user = userService.getUserById(userId);
		user.userAlbum = userAlbum;
		this.userAlbumDao.updateUserAlbum(userAlbum);

		return newUnit;
	}

	@Override
	public void loopSaveToDB() {
		// TODO Auto-generated method stub

	}

	@Override
	public Long getMaxPicId() {
		return this.maxPicId.incrementAndGet();
	}

	@Override
	public Long getMaxVideoId() {
		return this.maxVideoId.incrementAndGet();
	}

}
