package com.lee.arphoto.action;

import java.io.File;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.lee.arphoto.exception.AppErrorException;
import com.lee.arphoto.exception.ErrorCode;
import com.lee.arphoto.message.MessageUserAlbum;
import com.lee.arphoto.model.User;
import com.lee.arphoto.service.AlbumService;
import com.lee.arphoto.service.UserService;

/**
 * 上传相册到云端
 * 
 * { uid:1001, session:xxx }
 *
 */
@Controller
public class GetAlbumAction extends AbstractAction {

	@Autowired
	private UserService userService;

	@Autowired
	private AlbumService albumService;

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

		if (null == user.userAlbum) {
			user.userAlbum = albumService.loadUserAlbum(user.getId());
		}
		MessageUserAlbum message = new MessageUserAlbum(user.userAlbum);

		return message;
	}

}
