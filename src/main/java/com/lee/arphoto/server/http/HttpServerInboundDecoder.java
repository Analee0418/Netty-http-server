package com.lee.arphoto.server.http;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

/**
 * HTTP server 请求处理
 * 
 * 
 * 1.正常http请求，Decoder将请求转换为map集合传入下级进行处理
 * 2.如果action为fileUpload则不进行转换交由HttpUploadServerHandler处理
 * 
 * Client->HTTPServer->Decoder->HTTPServerHandler->HTTPUploadServerHandler
 * 
 * @author xiaofei.lee(13651027213@163.com)
 */
public class HttpServerInboundDecoder extends ChannelInboundHandlerAdapter {
	private static final Log logger = LogFactory.getLog(HttpServerInboundDecoder.class);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		if (msg instanceof HttpRequest) {
			/*
			 * 解析action
			 */
			HttpRequest req = (HttpRequest) msg;
			QueryStringDecoder decoder = new QueryStringDecoder(req.uri());
			String _uriPath = decoder.path();
			String[] uriArray = _uriPath.split("\\/");
			if (uriArray.length <= 0) {
				if (logger.isWarnEnabled()) {
					logger.warn("Request uri invalid : " + _uriPath);
				}
				ctx.close();
				return;
			}

			/*
			 * action解析正常
			 */
			String action = uriArray[uriArray.length - 1];
			if ("SaveAlbumToCloudAction".equals(action)) {

				// 如果是上传协议则抛掷下一级handler处理
				ctx.fireChannelRead(msg);

			} else {

				// 尝试转换位正常请求
				this._parseSimpleHttpRequest(action, ctx, msg);

			}
		}
	}

	private final void _parseSimpleHttpRequest(String action, ChannelHandlerContext ctx, Object msg) {
		if (msg instanceof FullHttpRequest) {
			FullHttpRequest req = (FullHttpRequest) msg;

			String _uri = req.uri();

			try {
				String contentStr = req.content().toString(Charset.forName("UTF-8"));
				if (!StringUtils.isEmpty(contentStr)) {
					if (_uri.indexOf("?") > -1) {
						_uri += "&" + contentStr;
					} else {
						_uri += "?j=j&" + contentStr;
					}
				}
			} catch (Exception e) {
			}

			QueryStringDecoder decoder = new QueryStringDecoder(_uri);
			Map<String, Object> params = new HashMap<>();

			Map<String, List<String>> uri_params = decoder.parameters();
			for (String uriParamKey : uri_params.keySet()) {
				List<String> _uriKeyParamList = uri_params.get(uriParamKey);
				if (null != _uriKeyParamList) {
					params.put(uriParamKey, _uriKeyParamList.get(_uriKeyParamList.size() - 1));
				}
			}

			logger.debug("\nAction: " + action + " \nParams: " + params);

			params.put("action", action);
			ctx.fireChannelRead(params);
		}
	}

}
