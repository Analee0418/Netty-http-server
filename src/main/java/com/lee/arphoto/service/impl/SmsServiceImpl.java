package com.lee.arphoto.service.impl;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lee.arphoto.httptools.utils.HttpRequester;
import com.lee.arphoto.httptools.utils.HttpRespons;
import com.lee.arphoto.service.SmsService;
import com.lee.arphoto.util.DateUtil;
import com.lee.arphoto.util.RandomUtil;

/**
 * 网易云信
 * 
 * @author xiaofei.lee(13651027213@163.com)
 *
 */
@Service
public class SmsServiceImpl implements SmsService {
	private final org.apache.commons.logging.Log logger = LogFactory.getLog(this.getClass());

	private static final String AppKey_NetEase = "e7f6ca5a8f864cb845593a7a6b4df083";
	private static final String AppSecret_NetEase = "c3b1d3b86252";

	/**
	 * 请求验证码短信
	 */
	private static final String URL_Request_Code = "https://api.netease.im/sms/sendcode.action";

	/**
	 * 验证码是否合法
	 */
	private static final String URL_Verify_Code = "https://api.netease.im/sms/verifycode.action";

	private final static Map<String, Long> requestSmsCodeTime = new HashMap<>();

	@Override
	public String requestValidCode(Long roleId, String tel) {
		long now = System.currentTimeMillis();
		Long lastReuqestTime = requestSmsCodeTime.get(tel);
		if (null != lastReuqestTime) {
			if (now - lastReuqestTime < DateUtil.MINUTE) {
				logger.error("太频繁");
				return null;
			}
		}

		if (!verifyTelNumber(tel)) {
			logger.error("手机号不合法");
			return null;
		}

		requestSmsCodeTime.put(tel, now);

		Map<String, String> headers = new HashMap<>(getCheckSumParam());
		headers.put("AppKey", AppKey_NetEase);
		headers.put("Content-Type", "application/x-www-form-urlencoded");

		StringBuilder sb = new StringBuilder();
		sb.append("mobile").append("=").append(tel);
		sb.append("&");
		sb.append("templateid").append("=").append(3056214);
		// sb.append("&");
		// sb.append("codeLen").append("=").append(4);

		try {
			HttpRespons response = new HttpRequester().sendNormalMsgPost(URL_Request_Code, sb.toString(), headers);
			String content = response.getContent();

			JsonObject jb = new Gson().fromJson(content, JsonObject.class);
			String code = jb.get("code").getAsString();
			if (!"200".equals(code)) {
				logger.error("获取失败:" + jb.toString());
				return null;
			}

			String obj = jb.get("obj").getAsString();

			return obj;
		} catch (IOException e) {
			logger.error("Error: ", e);
			return null;
		}
	}

	@Override
	public String verifyCode(String code, String tel) {
		if (!verifyTelNumber(tel)) {
			logger.error("手机号不合法");
			return "Error";
		}

		Map<String, String> headers = new HashMap<>(getCheckSumParam());
		headers.put("AppKey", AppKey_NetEase);
		headers.put("Content-Type", "application/x-www-form-urlencoded");

		StringBuilder sb = new StringBuilder();
		sb.append("mobile").append("=").append(tel);
		sb.append("&");
		sb.append("code").append("=").append(code);
		// sb.append("&");
		// sb.append("codeLen").append("=").append(4);

		try {
			HttpRespons response = new HttpRequester().sendNormalMsgPost(URL_Verify_Code, sb.toString(), headers);
			String content = response.getContent();

			JsonObject jb = new Gson().fromJson(content, JsonObject.class);
			code = jb.get("code").getAsString();
			return code;
		} catch (IOException e) {
			logger.error("Error: ", e);
			return "Error";
		}
	}

	/**
	 * 手机号码是否合法
	 * 
	 * @param mobiles
	 * @return
	 */
	private final static boolean verifyTelNumber(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 计算并获取CheckSum
	 * 
	 * @return
	 */
	private final static Map<String, String> getCheckSumParam() {
		Map<String, String> maps = new HashMap<>();

		final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

		long nowMillis = System.currentTimeMillis() / 1000; // 当前秒值
		maps.put("CurTime", String.valueOf(nowMillis));
		String nonce = RandomUtil.getRandStringEx(10); // 10位随机数
		maps.put("Nonce", nonce);

		String value = AppSecret_NetEase + nonce + String.valueOf(nowMillis);
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("sha1");
			messageDigest.update(value.getBytes());
			byte[] bytes = messageDigest.digest();

			int len = bytes.length;
			StringBuilder buf = new StringBuilder(len * 2);
			for (int j = 0; j < len; j++) {
				buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
				buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
			}
			maps.put("CheckSum", buf.toString());

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return maps;
	}

}
