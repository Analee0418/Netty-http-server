package com.lee.arphoto.service;

public interface SmsService {
	/**
	 * 用户请求短信验证
	 * 
	 * @param roleId
	 * @param tel
	 * @return
	 */
	String requestValidCode(Long roleId, String tel);

	/**
	 * 玩家校验短信验证码
	 * 
	 * @param code
	 * @param tel
	 * @return
	 */
	String verifyCode(String code, String tel);

}
