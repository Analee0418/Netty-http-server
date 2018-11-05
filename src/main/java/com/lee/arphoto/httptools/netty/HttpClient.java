/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.lee.arphoto.httptools.netty;

import java.net.URI;
import java.nio.charset.Charset;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

/**
 * A simple HTTP client that prints out the content of the HTTP response to
 * {@link System#out} to test {@link HttpSnoopServer}.
 */
public final class HttpClient {

	public static void main(String[] args) {

	}

	private final static HttpClientHandlerDesc DEFAULT = new HttpClientHandlerDesc() {
		@Override
		public void process(byte[] bytes) {
			System.out.println(new String(bytes, Charset.forName("UTF-8")));
		}
	};

	public static void startF(boolean ssl, HttpMethod method, String host, int port, byte[] bytes) throws Exception {
		startF(ssl, method, host, port, bytes, null);
	}

	public static void startF(boolean ssl, HttpMethod method, String host, int port, byte[] bytes,
			HttpClientHandlerDesc desc) throws Exception {
		long cur = System.currentTimeMillis();

		String URL = (ssl ? "https" : "http") + "://" + host + ":" + port;
		URI uri = new URI(URL);

		// Configure SSL context if necessary.
		final SslContext sslCtx;
		if (ssl) {
			sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
		} else {
			sslCtx = null;
		}

		if (null == desc) {
			desc = DEFAULT;
		}

		// Configure the client.
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).handler(new HttpClientInitializer(sslCtx, desc));

			// Make the connection attempt.
			Channel ch = b.connect(host, port).sync().channel();

			// Content byte buffer
			ByteBuf buffer = Unpooled.wrappedBuffer(bytes);

			// Prepare the HTTP request.
			FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, method, uri.getRawPath(),
					buffer);
			request.headers().set(HttpHeaderNames.HOST, host);
			request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
			request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());

			// Send the HTTP request.
			ch.writeAndFlush(request);

			// Wait for the server to close the connection.
			ch.closeFuture().sync();
		} finally {
			// Shut down executor threads to exit.
			group.shutdownGracefully();
		}

		long diff = System.currentTimeMillis() - cur;

		System.out.println("共使用了 " + diff + "毫秒");

	}
}
