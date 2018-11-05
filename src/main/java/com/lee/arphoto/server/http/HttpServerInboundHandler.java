package com.lee.arphoto.server.http;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lee.arphoto.server.disruptor.DisruptorEngine;
import com.lee.arphoto.server.disruptor.pojo.RequestPojo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;

/**
 * 解析
 * 
 * @author xiaofei.lee(13651027213@163.com)
 *
 *         始终相信天道酬勤!
 */
public class HttpServerInboundHandler extends ChannelInboundHandlerAdapter {
	private static final Log logger = LogFactory.getLog(HttpServerInboundHandler.class);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		if (msg instanceof Map) {
			Map req = (Map) msg;

			// 耗时不长的 放置Disruptor处理，其他由各自线程池处理
			RequestPojo requestPojo = new RequestPojo(ctx, req);
			DisruptorEngine.pushEvent(requestPojo);
		}

		else if (msg instanceof HttpRequest) {

			// HttpRequest请求为fileUpload
			ctx.fireChannelRead(msg);
		}

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		logger.error("Channel exception: " + cause.getMessage());
		ctx.close();
	}
}
