/**
 * 
 */
package com.lee.arphoto.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.lee.arphoto.model.AlbumUnit;

/**
 * @author xiaofei.lee(13651027213@163.com)
 */
public interface AlbumUnitDao {
	static final String TABLE = "albumunit";

	static final String FIELDS = "id, picId, videoId, offCard, comment";
	static final String INSERT_FIELDS = "#{id}, #{picId}, #{videoId}, #{offCard}, #{comment}";

	@Select(value = "select " + FIELDS + " from " + TABLE + " where id > #{cursor} order by id asc limit #{count}")
	List<AlbumUnit> getAlbumUnitBatch(@Param("cursor") long cursor, @Param("count") int count);

	@Select(value = "SELECT " + FIELDS + " FROM " + TABLE + " where id = #{id}")
	List<AlbumUnit> getAlbumUnitById(@Param("id") Long id);

	@Insert(value = "INSERT INTO " + TABLE + "(" + FIELDS + ") " + "VALUES(" + INSERT_FIELDS + ")")
	int insertAlbum(AlbumUnit albumUnit);

	@Select(value = "select max(id) from " + TABLE)
	Long getMaxId();
}
