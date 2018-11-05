package com.lee.arphoto.httptools.response;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.nio.charset.Charset;
import java.util.Map;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;

/**
 * HTTP Response Data Factory
 * 
 * @author xiaofei.lee(13651027213@163.com) 始终相信天道酬勤!
 */
public class HTTPResponseFactory {

	/**
	 * 构造一个ResponseObject响应数据结构实例
	 * 
	 * @param responseString
	 * @return
	 */
	public static FullHttpResponse buildHttpResponse(String responseString) {
		return buildHttpResponse(responseString, null);
	}

	public static FullHttpResponse buildHttpResponse(String responseString, Map<String, String> cookies) {
		byte[] contentBytes = responseString.getBytes(Charset.forName("UTF-8"));

		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(contentBytes));
		response.headers().set("Content-Type", "text/html; charset=utf-8");
		response.headers().set("Cache-Control", "private");
		response.headers().setInt("Content-Length", response.content().readableBytes());
		response.headers().set("Keep-Alive", "timeout=10, max=50");

		if (null != cookies && !cookies.isEmpty()) {
			for (String cookieKey : cookies.keySet()) {
				if (null == cookieKey) {
					continue;
				}
				String cookieValue = cookies.get(cookieKey);
				if (null == cookieValue) {
					continue;
				}

				Cookie cookie = new DefaultCookie(cookieKey, cookieValue);
				cookie.setMaxAge(60 * 5);

				response.headers().add(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.STRICT.encode(cookie));
			}
		}

		return response;
	}

	/**
	 * 构造一个ResponseObject响应数据结构实例
	 * 
	 * @param data
	 * @return
	 */
	public static FullHttpResponse buildHttpResponse(byte[] data) {
		return buildHttpResponse(data, null);
	}

	public static FullHttpResponse buildHttpResponse(byte[] data, Map<String, String> cookies) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(data));
		response.headers().set("Content-Type", "text/plain; charset=utf-8");
		response.headers().setInt("Content-Length", response.content().readableBytes());

		if (null != cookies && !cookies.isEmpty()) {
			for (String cookieKey : cookies.keySet()) {
				if (null == cookieKey) {
					continue;
				}
				String cookieValue = cookies.get(cookieKey);
				if (null == cookieValue) {
					continue;
				}

				Cookie cookie = new DefaultCookie(cookieKey, cookieValue);
				cookie.setMaxAge(60 * 5);

				response.headers().add(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.STRICT.encode(cookie));

			}
		}

		return response;
	}
}
