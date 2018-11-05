package com.lee.arphoto.service;

import java.util.List;

import com.lee.arphoto.model.AlbumUnit;
import com.lee.arphoto.model.Pic;
import com.lee.arphoto.model.UserAlbum;
import com.lee.arphoto.model.Video;

public interface AlbumService {

	UserAlbum loadUserAlbum(Long userId);

	void createNewPic(Pic pic);

	void createNewVideo(Video video);

	AlbumUnit createNewAlbumUnit(Long userId, Pic pic, List<Video> videos, boolean offCard, String comment);

	Long getMaxPicId();

	Long getMaxVideoId();

	void loopSaveToDB();
}
