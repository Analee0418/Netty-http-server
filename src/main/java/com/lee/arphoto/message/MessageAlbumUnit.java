package com.lee.arphoto.message;

import java.util.ArrayList;
import java.util.List;

import com.lee.arphoto.model.AlbumUnit;
import com.lee.arphoto.model.Video;

public class MessageAlbumUnit extends AbstractMessage {
	private Long id;
	private int offCard;
	private MessagePic pic;
	private List<MessageVideo> videoList = new ArrayList<>();
	private String comment;

	public MessageAlbumUnit(AlbumUnit albumUnit) {
		this.id = albumUnit.getId();
		this.offCard = albumUnit.isOffCard() ? 1 : 0;
		this.comment = albumUnit.getComment();
		this.pic = new MessagePic(albumUnit.getPic());
		for (Video v : albumUnit.getVideoList()) {
			this.videoList.add(new MessageVideo(v));
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public MessagePic getPic() {
		return pic;
	}

	public void setPic(MessagePic pic) {
		this.pic = pic;
	}

	public List<MessageVideo> getVideoList() {
		return videoList;
	}

	public void setVideoList(List<MessageVideo> videoList) {
		this.videoList = videoList;
	}

	public int getOffCard() {
		return offCard;
	}

	public void setOffCard(int offCard) {
		this.offCard = offCard;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
