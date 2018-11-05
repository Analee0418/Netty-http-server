/**
 * 
 */
package com.lee.arphoto.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.lee.arphoto.model.UserAlbum;

/**
 * @author xiaofei.lee(13651027213@163.com)
 */
public interface UserAlbumDao {
	static final String TABLE = "useralbum";

	static final String FIELDS = "id, userId, data, friendsData";
	static final String INSERT_FIELDS = "#{id}, #{userId}, #{data}, #{friendsData}";

	@Select(value = "select " + FIELDS + " from " + TABLE + " where id > #{cursor} order by id asc limit #{count}")
	List<UserAlbum> getUserAlbumBatch(@Param("cursor") long cursor, @Param("count") int count);

	@Select(value = "SELECT " + FIELDS + " FROM " + TABLE + " where id = #{id}")
	UserAlbum getUserAlbumById(@Param("id") Long id);

	@Select(value = "SELECT " + FIELDS + " FROM " + TABLE + " where userId = #{userId}")
	List<UserAlbum> getUserAlbumByUserId(@Param("userId") Long userId);

	@Insert(value = "INSERT INTO " + TABLE + "(" + FIELDS + ") " + "VALUES(" + INSERT_FIELDS + ")")
	int insertUserAlbum(UserAlbum albumUnit);
	
	
	@Update(value = " UPDATE " + TABLE + " SET data = #{data}, friendsData=#{friendsData} WHERE `id` = #{id}")
	int updateUserAlbum(UserAlbum albumUnit);

	@Select(value = "select max(id) from " + TABLE)
	Long getMaxId();
}
