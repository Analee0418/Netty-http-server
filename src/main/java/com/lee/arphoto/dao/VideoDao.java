/**
 * 
 */
package com.lee.arphoto.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.lee.arphoto.model.Video;

/**
 * @author xiaofei.lee(13651027213@163.com)
 */
public interface VideoDao {
	static final String TABLE = "video";

	static final String FIELDS = "id, fileName, downloadPath, createTime";
	static final String INSERT_FIELDS = "#{id}, #{fileName}, #{downloadPath}, #{createTime}";

	@Select(value = "select " + FIELDS + " from " + TABLE + " where id > #{cursor} order by id asc limit #{count}")
	List<Video> getVideoBatch(@Param("cursor") long cursor, @Param("count") int count);

	@Select(value = "SELECT " + FIELDS + " FROM " + TABLE + " where id = #{id}")
	Video getVideoById(@Param("id") Long id);

	@Insert(value = "INSERT INTO " + TABLE + "(" + FIELDS + ") " + "VALUES(" + INSERT_FIELDS + ")")
	int insertVideo(Video video);

	@Select(value = "select max(id) from " + TABLE)
	Long getMaxId();
}
