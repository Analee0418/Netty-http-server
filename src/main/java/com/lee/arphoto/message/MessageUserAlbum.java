package com.lee.arphoto.message;

import java.util.ArrayList;
import java.util.List;

import com.lee.arphoto.model.AlbumUnit;
import com.lee.arphoto.model.UserAlbum;

public class MessageUserAlbum extends AbstractMessage {
	private List<MessageAlbumUnit> units = new ArrayList<>();
	private List<MessageAlbumUnit> friendsUnits = new ArrayList<>();

	public MessageUserAlbum(UserAlbum userAlbum) {
		if (null == userAlbum) {
			return;
		}
		
		for (AlbumUnit unit : userAlbum.getUnits().values()) {
			this.units.add(new MessageAlbumUnit(unit));
		}

		for (AlbumUnit unit : userAlbum.getFriendsUnits().values()) {
			this.friendsUnits.add(new MessageAlbumUnit(unit));
		}
	}

	public List<MessageAlbumUnit> getUnits() {
		return units;
	}

	public void setUnits(List<MessageAlbumUnit> units) {
		this.units = units;
	}

	public List<MessageAlbumUnit> getFriendsUnits() {
		return friendsUnits;
	}

	public void setFriendsUnits(List<MessageAlbumUnit> friendsUnits) {
		this.friendsUnits = friendsUnits;
	}

}
