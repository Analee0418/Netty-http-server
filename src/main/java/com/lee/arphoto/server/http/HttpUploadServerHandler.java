
package com.lee.arphoto.server.http;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lee.arphoto.exception.ErrorCode;
import com.lee.arphoto.message.MessageAppError;
import com.lee.arphoto.server.disruptor.DisruptorEngine;
import com.lee.arphoto.server.disruptor.pojo.RequestPojo;
import com.lee.arphoto.spring.config.server.AppServerProperties;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.DiskAttribute;
import io.netty.handler.codec.http.multipart.DiskFileUpload;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpData;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.EndOfDataDecoderException;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.ErrorDataDecoderException;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;

/**
 * 
 * @author xiaofei.lee(13651027213@163.com)
 *
 */
public class HttpUploadServerHandler extends SimpleChannelInboundHandler<HttpObject> {

	private static final Log logger = LogFactory.getLog(HttpUploadServerHandler.class);

	private HttpData partialContent;

	private Map<String, Object> params = new HashMap<>();
	private Map<String, File> files = new HashMap<>();

	// Disk if size exceed
	private static final HttpDataFactory factory = new DefaultHttpDataFactory(0);

	private HttpPostRequestDecoder decoder;

	static {
		// should delete file on exit (in normal exit)
		DiskFileUpload.deleteOnExitTemporaryFile = true;
		// system temp directory
		DiskFileUpload.baseDirectory = "../uploadFile/tmp/";
		// should delete file on exit (in normal exit)
		DiskAttribute.deleteOnExitTemporaryFile = true;
		// system temp directory
		DiskAttribute.baseDirectory = "../uploadFile/attr/";
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		if (decoder != null) {
			decoder.cleanFiles();
			decoder.destroy();
		}
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
		if (msg instanceof HttpRequest) {
			HttpRequest request = (HttpRequest) msg;

			// 文件http传输只能用post协议
			if (request.method().equals(HttpMethod.GET)) {
				ErrorCode e = ErrorCode.ErrorFileUploadGetOnly;
				ctx.writeAndFlush(new MessageAppError(e)).addListener(ChannelFutureListener.CLOSE);
				return;
			}

			try {
				decoder = new HttpPostRequestDecoder(factory, request);
			} catch (ErrorDataDecoderException e1) {
				logger.error("", e1);
				ctx.writeAndFlush(new MessageAppError(e1.getMessage())).addListener(ChannelFutureListener.CLOSE);
				return;
			}
		}

		if (decoder != null) {
			if (msg instanceof HttpContent) {
				// New chunk is received
				HttpContent chunk = (HttpContent) msg;
				try {
					decoder.offer(chunk);
				} catch (ErrorDataDecoderException e1) {
					logger.error("", e1);
					ctx.writeAndFlush(new MessageAppError(e1.getMessage())).addListener(ChannelFutureListener.CLOSE);
					return;
				}
				_readHttpDataChunkByChunk();

				// Disruptor 处理
				if (chunk instanceof LastHttpContent) {
					this.params.put("action", "SaveAlbumToCloudAction");
					RequestPojo requestPojo = new RequestPojo(ctx, this.params, this.files);
					DisruptorEngine.pushEvent(requestPojo);
					reset();
				}
			}
		} else {
			ctx.close();
		}
	}

	private void reset() {
		// destroy the decoder to release all resources
		// decoder.destroy();
		// decoder = null;
		files = new HashMap<>();
		params = new HashMap<>();
	}

	private void _readHttpDataChunkByChunk() {
		try {
			while (decoder.hasNext()) {
				InterfaceHttpData data = decoder.next();
				if (data != null) {
					// check if current HttpData is a FileUpload and previously
					// set as partial
					if (partialContent == data) {
						logger.info("上传完成 100% (文件大小: " + partialContent.length() + ")");
						partialContent = null;
					}
					try {
						// new value
						_writeHttpData(data);
					} finally {
						// data.release();
					}
				}
			}
			// Check partial decoding for a FileUpload
			InterfaceHttpData data = decoder.currentPartialHttpData();
			if (data != null) {
				StringBuilder builder = new StringBuilder();
				if (partialContent == null) {
					partialContent = (HttpData) data;
					if (partialContent instanceof FileUpload) {
						builder.append("开始长传文件: ").append(((FileUpload) partialContent).getFilename()).append(" ");
					} else {
						builder.append("开始解析Multipart协议参数: ").append(partialContent.getName()).append(" ");
					}
					builder.append("(内容定义长度: ").append(partialContent.definedLength()).append(")");
				}

				if (partialContent.definedLength() > 0) {
					builder.append(" ").append(partialContent.length() * 100 / partialContent.definedLength())
							.append("% ");
					logger.info(builder.toString());
				} else {
					builder.append(" ").append(partialContent.length()).append(" ");
					logger.info(builder.toString());
				}
			}
		} catch (EndOfDataDecoderException e1) {
			// end
			// logger.error("", e1);
		}
	}

	private void _writeHttpData(InterfaceHttpData data) {
		if (data.getHttpDataType() == HttpDataType.Attribute) {
			Attribute attribute = (Attribute) data;
			try {
				this.params.put(attribute.getName(), attribute.getValue());
			} catch (IOException e1) {
				logger.error("", e1);
			}
		} else {
			if (data.getHttpDataType() == HttpDataType.FileUpload) {
				FileUpload fileUpload = (FileUpload) data;
				if (fileUpload.isCompleted()) {
					if (fileUpload.length() < AppServerProperties.getFileUploadMaxBytes()) { // 最多上传5m
						try {
							this.files.put(fileUpload.getName(), fileUpload.getFile());
							// FileUtil.writeBytesFile(_c, "../1.text");
						} catch (Exception e1) {
							logger.error("", e1);
						}
					}
				}
			}
		}
	}

	// private void writeResponse(Channel channel) {
	// // Convert the response content to a ChannelBuffer.
	//
	// // Decide whether to close the connection or not.
	// boolean close = request.headers().contains(HttpHeaderNames.CONNECTION,
	// HttpHeaderValues.CLOSE, true)
	// || request.protocolVersion().equals(HttpVersion.HTTP_1_0)
	// && !request.headers().contains(HttpHeaderNames.CONNECTION,
	// HttpHeaderValues.KEEP_ALIVE, true);
	//
	// // Build the response object.
	// FullHttpResponse response = new
	// DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
	// buf);
	// response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;
	// charset=UTF-8");
	//
	// if (!close) {
	// // There's no need to add 'Content-Length' header
	// // if this is the last response.
	// response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH,
	// buf.readableBytes());
	// }
	//
	// Set<Cookie> cookies;
	// String value = request.headers().get(HttpHeaderNames.COOKIE);
	// if (value == null) {
	// cookies = Collections.emptySet();
	// } else {
	// cookies = ServerCookieDecoder.STRICT.decode(value);
	// }
	// if (!cookies.isEmpty()) {
	// // Reset the cookies if necessary.
	// for (Cookie cookie : cookies) {
	// response.headers().add(HttpHeaderNames.SET_COOKIE,
	// ServerCookieEncoder.STRICT.encode(cookie));
	// }
	// }
	// // Write the response.
	// ChannelFuture future = channel.writeAndFlush(response);
	// // Close the connection after the write operation is done if necessary.
	// if (close) {
	// future.addListener(ChannelFutureListener.CLOSE);
	// }
	// }

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("", cause);
		ctx.channel().close();
	}
}
