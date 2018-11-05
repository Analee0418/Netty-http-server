package com.lee.arphoto.server.http;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lee.arphoto.spring.config.server.AppServerProperties;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

/**
 * HTTP Server
 * 
 * <短连接>
 * 
 * @author xiaofei.lee(13651027213@163.com)
 *
 */
public final class HttpServer {

	private static final Log logger = LogFactory.getLog(HttpServer.class);

	static final boolean SSL = System.getProperty("ssl") != null;

	public static void startup(String[] args) throws Exception {
		// Configure SSL.
		final SslContext sslCtx;
		if (SSL) {
			SelfSignedCertificate ssc = new SelfSignedCertificate();
			sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
		} else {
			sslCtx = null;
		}

		// Configure the server.
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.option(ChannelOption.SO_BACKLOG, 1024);
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.INFO)).childHandler(new HttpServerInitializer(sslCtx));

			Channel ch = b.bind(AppServerProperties.getPort()).sync().channel();

			if (logger.isInfoEnabled()) {
				logger.info("Http Server Started!! with port :" + AppServerProperties.getPort());
			}

			ch.closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();

			logger.info("Http server close.");
		}
	}
}
