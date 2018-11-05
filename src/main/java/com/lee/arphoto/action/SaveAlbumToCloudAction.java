package com.lee.arphoto.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import com.lee.arphoto.exception.AppErrorException;
import com.lee.arphoto.exception.ErrorCode;
import com.lee.arphoto.message.MessageAlbumUnit;
import com.lee.arphoto.model.AlbumUnit;
import com.lee.arphoto.model.Pic;
import com.lee.arphoto.model.User;
import com.lee.arphoto.model.Video;
import com.lee.arphoto.service.AlbumService;
import com.lee.arphoto.service.UserService;
import com.lee.arphoto.spring.config.server.AppServerProperties;
import com.lee.arphoto.util.FileUtil;
import com.lee.arphoto.util.RandomUtil;

/**
 * 上传相册到云端
 * 
 * { uid:1001, session:xxx, offCard:1/0, comment:String, pic:byte[],
 * video1:byte[], video2:byte[], ... }
 *
 */
@Controller
public class SaveAlbumToCloudAction extends AbstractAction {
	private final Log logger = org.apache.commons.logging.LogFactory.getLog(this.getClass());

	@Autowired
	private UserService userService;

	@Autowired
	private AlbumService albumServie;

	@Override
	public boolean isEasy() {
		return false;
	}

	@Override
	protected Object doAction(Map<String, Object> params, Map<String, File> files) {
		Long uid = Long.parseLong(params.get("uid").toString());
		User user = userService.getUserById(uid);
		if (null == user) {
			throw new AppErrorException(ErrorCode.ErrorParameter);
		}

		String comment = params.get("comment").toString();
		if (StringUtils.isEmpty(comment)) {
			comment = "";
		}
		comment = FileUtil.filterEmoji(comment);

		Object _offCard = params.get("offCard");
		boolean offCard = false;
		if (null != _offCard) {
			offCard = 1 == Integer.parseInt(_offCard.toString());
		}

		// 图片
		File picFile = files.get("pic");
		if (null == picFile) {
			throw new AppErrorException(ErrorCode.ErrorParameter);
		}
		Long newPicId = this.albumServie.getMaxPicId();
		Map<String, Object> map = _buildNewFile(user.getId(), newPicId, picFile);
		if (null == map) {
			throw new AppErrorException(ErrorCode.ErrorParameter);
		}

		Pic pic = new Pic();
		pic.setId(newPicId);
		pic.setDownloadPath(map.get("downloadPath").toString());
		pic.setCreateTime(new Date());
		pic.setFileName(map.get("fileName").toString());
		albumServie.createNewPic(pic);

		// 视频
		List<Video> videos = _buildVideoList(user, files);
		if (videos.size() <= 0) {
			throw new AppErrorException(ErrorCode.ErrorParameter);
		}

		AlbumUnit newUnit = this.albumServie.createNewAlbumUnit(user.getId(), pic, videos, offCard, comment);
		MessageAlbumUnit message = new MessageAlbumUnit(newUnit);
		// MessageUserAlbum message = new MessageUserAlbum(user.userAlbum);
		// logger.debug(params);
		// logger.debug(files);

		return message;
	}

	private final String _buildUserFileName(Long userId, Long fileUrlDBId, String fileExtName) {
		StringBuilder sb = new StringBuilder();
		sb.append(userId).append("_").append(fileUrlDBId).append(RandomUtil.getRandStringExNum(8));

		sb.append(".").append(fileExtName);
		return sb.toString();
	}

	private final Map<String, Object> _buildNewFile(Long userId, Long fileId, File originalFile) {
		String fullFileName = this._buildUserFileName(userId, fileId, FileUtil.getFileExtName(originalFile));
		String donwloadPath = AppServerProperties.getCdnServerAddress() + fullFileName;

		if (logger.isDebugEnabled()) {
			logger.debug("File name = " + fullFileName + ", download path = " + donwloadPath);
		}

		File newFile = new File(AppServerProperties.getDownloadPath() + fullFileName);
		try {
			Map<String, Object> map = new HashMap<>();
			newFile = FileUtil.forTransfer(originalFile, newFile);
			map.put("downloadPath", donwloadPath);
			map.put("fileName", newFile.getName());
			return map;
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}

	private final List<Video> _buildVideoList(User user, Map<String, File> files) {
		List<Video> videos = new ArrayList<>();

		for (int i = 1; i < 10; i++) {
			File videoFile = files.get("video" + i);
			if (null != videoFile) {
				Long newVideoId = this.albumServie.getMaxVideoId();
				Map<String, Object> map = _buildNewFile(user.getId(), newVideoId, videoFile);
				if (null != map) {
					Video video = new Video();
					video.setId(newVideoId);
					video.setDownloadPath(map.get("downloadPath").toString());
					video.setCreateTime(new Date());
					video.setFileName(map.get("fileName").toString());

					albumServie.createNewVideo(video);

					videos.add(video);
				}
			}
		}

		return videos;
	}
}
