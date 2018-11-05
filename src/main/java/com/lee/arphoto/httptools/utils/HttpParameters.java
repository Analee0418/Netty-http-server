package com.lee.arphoto.httptools.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.StringUtils;

import io.netty.handler.codec.http.QueryStringDecoder;

public class HttpParameters {

	public static Map<String, String> parameters(String uri) {
		if (null == uri) {
			return new HashMap<>();
		}

		if (uri.indexOf("?") == -1) {
			uri = "?" + uri;
		}
		QueryStringDecoder decoder = new QueryStringDecoder(uri);

		Map<String, String> ps = new HashMap<>();

		Map<String, List<String>> parameters = decoder.parameters();
		for (String key : decoder.parameters().keySet()) {
			List<String> list = parameters.get(key);
			if (null != list) {
				ps.put(key, list.get(0));
			}
		}

		return ps;
	}

	/**
	 * 获取统一平台待签名字符串
	 * 
	 * @param except
	 *            除外的参数
	 * @return
	 */
	public static String getAnysdkSecretSign(Map<String, String> param, Set<String> except) {
		if (null == param || param.isEmpty()) {
			return "";
		}

		List<String> keyList = new ArrayList<>(param.keySet());
		Collections.sort(keyList);
		StringBuilder sb = new StringBuilder();

		for (String key : keyList) {
			if (StringUtils.isEmpty(key)) {
				continue;
			}
			if (except != null && except.contains(key)) {
				continue;
			}

			String str = param.get(key);
			if (StringUtils.isEmpty(str)) {
				continue;
			}
			sb.append(str);
		}
		return sb.toString();
	}

}
