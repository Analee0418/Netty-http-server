package com.lee.arphoto.server.http;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.cors.CorsConfigBuilder;
import io.netty.handler.codec.http.cors.CorsHandler;
import io.netty.handler.ssl.SslContext;

//
/**
 * HTTP Server Initializer
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

	private static Log logger = LogFactory.getLog(HttpServerInitializer.class);

	private final SslContext sslCtx;

	public HttpServerInitializer(SslContext sslCtx) {
		this.sslCtx = sslCtx;
	}

	@Override
	public void initChannel(SocketChannel ch) {
		ChannelPipeline p = ch.pipeline();
		if (sslCtx != null) {
			p.addLast(sslCtx.newHandler(ch.alloc()));
		}

		p.addLast(new HttpRequestDecoder());
		p.addLast(new HttpResponseEncoder());
		p.addLast(new HttpObjectAggregator(1024 *1024 * 5));

		p.addLast(new CorsHandler(CorsConfigBuilder.forAnyOrigin().allowNullOrigin().allowCredentials()
				.allowedRequestMethods(HttpMethod.POST, HttpMethod.GET).allowedRequestHeaders("*").build())); // 跨域配置

		p.addLast(new HttpServerOutboundEncoder()); // 2. 加密byte[]
		p.addLast(new HttpServerOutboundHandler()); // 1. 响应proto

		p.addLast(new HttpServerInboundDecoder()); // 1. 解密
		p.addLast(new HttpServerInboundHandler()); // 2. 处理
		p.addLast(new HttpUploadServerHandler()); // 3. 上传

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
		ctx.channel().close();

		// TODO remove session from cache
		logger.error("", new RuntimeException("@TODO 还没有加入清理Session缓存代码，请尽快完善"));

		if (logger.isInfoEnabled()) {
			logger.info("Connection force close!  Connection exception , " + cause.getMessage());
		}
	}

}
