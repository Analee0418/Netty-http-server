package com.lee.arphoto.server.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;

public class HttpServerOutboundHandler extends ChannelOutboundHandlerAdapter {

	@Override
	public void flush(ChannelHandlerContext ctx) throws Exception {
		super.flush(ctx);
	}

}
