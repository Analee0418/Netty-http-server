/**
 * 
 */
package com.lee.arphoto.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import com.lee.arphoto.dao.provider.UserDaoProvider;
import com.lee.arphoto.model.User;

/**
 * @author xiaofei.lee(13651027213@163.com)
 */
public interface UserDao {
	@Select(value = "select id, userName, password, nikeName, sex, homePicUrl, platformId, platformName, createTime, lastLoginTime from user where id > #{cursor} order by id asc limit #{count}")
	List<User> getUserBatch(@Param("cursor") long cursor, @Param("count") int count);

	@Select(value = "SELECT id, userName, password, nikeName, sex, homePicUrl, platformId, platformName, createTime, lastLoginTime FROM user where userName = #{userName}")
	User getUserByUserName(@Param("userName") String userName);

	@Insert(value = "INSERT INTO user(id, userName, password, nikeName, sex, homePicUrl, platformId, platformName, createTime, lastLoginTime) "
			+ "VALUES(#{id}, #{userName}, #{password}, #{nikeName}, #{sex}, #{homePicUrl},  #{platformId}, #{platformName}, #{createTime}, #{lastLoginTime})")
	int insertUser(User user);

	@Select(value = "select max(id) from user")
	Long getMaxId();

	@Update(value = "UPDATE user SET lastLoginTime = #{lastLoginTime} WHERE `id` = #{id}")
	int updateLastLoginTime(User user);

	@UpdateProvider(type = UserDaoProvider.class, method = "updateUserBatch")
	int updateUserBatch(@Param("userList") List<User> userList);
}
