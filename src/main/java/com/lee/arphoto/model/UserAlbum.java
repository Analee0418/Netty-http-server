package com.lee.arphoto.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.lee.arphoto.dao.AlbumUnitDao;
import com.lee.arphoto.spring.context.AppContext;

public class UserAlbum extends AbstractDBModel {
	private final Log logger = LogFactory.getLog(this.getClass());

	private Long id;
	private Long userId;
	private String data;
	private String friendsData;

	private Map<Long, AlbumUnit> units = new HashMap<>();
	private Map<Long, AlbumUnit> friendsUnits = new HashMap<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getFriendsData() {
		return friendsData;
	}

	public void setFriendsData(String friendsData) {
		this.friendsData = friendsData;
	}

	public Map<Long, AlbumUnit> getUnits() {
		return units;
	}

	public void setUnits(Map<Long, AlbumUnit> units) {
		this.units = units;
	}

	public Map<Long, AlbumUnit> getFriendsUnits() {
		return friendsUnits;
	}

	public void setFriendsUnits(Map<Long, AlbumUnit> friendsUnits) {
		this.friendsUnits = friendsUnits;
	}

	public AlbumUnit addAlbumUnit(Long unitId) {
		AlbumUnitDao albumUnitDao = AppContext.getContext().getBean(AlbumUnitDao.class);
		List<AlbumUnit> units = albumUnitDao.getAlbumUnitById(unitId);
		AlbumUnit unit = null;
		if (units.size() > 0) {
			unit = units.get(0);
		}
		if (null == unit) {
			logger.error("", new RuntimeException("self album unit is null"));
			return null;
		}

		unit.afterLoadDB();
		if (StringUtils.isEmpty(this.data)) {
			this.data = unitId + "";
		} else {
			this.data += ("," + unitId);
		}
		this.units.put(unit.getId(), unit);
		return unit;
	}

	public void addFriendAlbumUnit(Long unitId) {
		AlbumUnitDao albumUnitDao = AppContext.getContext().getBean(AlbumUnitDao.class);
		List<AlbumUnit> units = albumUnitDao.getAlbumUnitById(unitId);
		AlbumUnit unit = null;
		if (units.size() > 0) {
			unit = units.get(0);
		}
		if (null == unit) {
			logger.error("", new RuntimeException("friend album unit is null"));
			return;
		}

		unit.afterLoadDB();
		if (StringUtils.isEmpty(this.friendsData)) {
			this.friendsData = unitId + "";
		} else {
			this.friendsData += ("," + unitId);
		}
		this.friendsUnits.put(unit.getId(), unit);
	}

	public void afterLoadDB() {
		if (StringUtils.isEmpty(this.data)) {
			logger.error("", new RuntimeException("该相册没有内容: albumid=" + this.getId()));
			return;
		}

		/*
		 * 查找自己的照片
		 */
		if (StringUtils.isEmpty(this.data)) {
			logger.error("", new RuntimeException("该相册没有上传任何内容! albumid=" + this.getId()));
			return;
		}
		String[] selfDataStrArray = this.data.split(",");
		List<Long> selfAlbumUnitIdList = new ArrayList<>();
		for (String aidStr : selfDataStrArray) {
			if (StringUtils.isEmpty(aidStr)) {
				continue;
			}
			try {
				Long videoId = Long.parseLong(aidStr);
				selfAlbumUnitIdList.add(videoId);
			} catch (Exception e) {
			}
		}

		if (selfAlbumUnitIdList.size() <= 0) { // 数据有误，没有任何视频
			logger.error("", new RuntimeException("该相册没有上传任何内容! albumid=" + this.getId()));
			return;
		}

		AlbumUnitDao albumUnitDao = AppContext.getContext().getBean(AlbumUnitDao.class);
		for (Long unitId : selfAlbumUnitIdList) {
			List<AlbumUnit> units = albumUnitDao.getAlbumUnitById(unitId);
			AlbumUnit unit = null;
			if (units.size() > 0) {
				unit = units.get(0);
			}
			if (null == unit) {
				continue;
			}
			unit.afterLoadDB();
			this.units.put(unit.getId(), unit);
		}

		/*
		 * 查找好友的照片
		 */
		if (!StringUtils.isEmpty(this.friendsData)) {
			String[] friendsDataStrArray = this.friendsData.split(",");
			List<Long> friendAlbumUnitIdList = new ArrayList<>();
			for (String aidStr : friendsDataStrArray) {
				if (StringUtils.isEmpty(aidStr)) {
					continue;
				}
				try {
					Long videoId = Long.parseLong(aidStr);
					friendAlbumUnitIdList.add(videoId);
				} catch (Exception e) {
				}
			}

			if (friendAlbumUnitIdList.size() <= 0) {
				return;
			}

			for (Long unitId : friendAlbumUnitIdList) {
				List<AlbumUnit> units = albumUnitDao.getAlbumUnitById(unitId);
				AlbumUnit unit = null;
				if (units.size() > 0) {
					unit = units.get(0);
				}
				if (null == unit) {
					continue;
				}
				unit.afterLoadDB();
				this.friendsUnits.put(unit.getId(), unit);
			}
		}
	}

}
