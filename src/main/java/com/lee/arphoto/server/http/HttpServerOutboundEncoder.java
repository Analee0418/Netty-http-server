package com.lee.arphoto.server.http;

import java.nio.charset.Charset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lee.arphoto.exception.ErrorCode;
import com.lee.arphoto.httptools.response.HTTPResponseFactory;
import com.lee.arphoto.message.AbstractMessage;
import com.lee.arphoto.message.MessageAppError;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.FullHttpResponse;

/**
 * 
 * @author xiaofei.lee(13651027213@163.com)
 *
 *         始终相信天道酬勤!
 */
public class HttpServerOutboundEncoder extends ChannelOutboundHandlerAdapter {

	private static final Log logger = LogFactory.getLog(HttpServerOutboundEncoder.class);

	private static final Gson G = new Gson();

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		if (msg instanceof AbstractMessage) {

			AbstractMessage message = (AbstractMessage) msg;

			JsonObject obj = new JsonObject();
			ErrorCode code = ErrorCode.Success;
			if (message instanceof MessageAppError) {
				code = ((MessageAppError) message).getCode();
			}
			String msgStr = G.toJson(message);

			obj.addProperty("result", code.getCode());
			obj.add("msg", G.fromJson(msgStr, JsonElement.class));

			String jsonStr = obj.toString();

			byte[] outAll = jsonStr.getBytes(Charset.forName("utf-8"));
			// byte[] outAll = XXTEA.zipEncryptToBytes(message.getBytes());

			if (logger.isDebugEnabled()) {
				logger.debug("Response Game Message : " + message.getClass().getSimpleName() + ",   bytes' size : "
						+ outAll.length + ", " + message);
			}

			FullHttpResponse response = HTTPResponseFactory.buildHttpResponse(outAll);
			super.write(ctx, response, promise);

		} else if (msg instanceof FullHttpResponse) {

			FullHttpResponse response = (FullHttpResponse) msg;

			if (logger.isDebugEnabled()) {
				logger.debug(
						"Response Http Response content  : " + response.content().toString(Charset.forName("UTF-8")));
			}

			super.write(ctx, msg, promise);
		}
	}

}
