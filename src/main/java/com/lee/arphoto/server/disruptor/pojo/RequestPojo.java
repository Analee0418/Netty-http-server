package com.lee.arphoto.server.disruptor.pojo;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.netty.channel.ChannelHandlerContext;

/**
 * 网络请求数据结构
 * 
 * @author xiaofei.lee(13651027213@163.com)
 *
 *         始终相信天道酬勤!
 */
public class RequestPojo {

	// params
	private Map<String, Object> params;

	// file list - fileUpload
	private Map<String, File> files;

	// actionName
	private String actionName;

	private ChannelHandlerContext channelContext;

	public RequestPojo(ChannelHandlerContext channelContext, Map<String, Object> params) {
		super();
		this.channelContext = channelContext;
		this.params = params;
		this.files = new HashMap<>();
		this.actionName = params.get("action").toString();
	}

	public RequestPojo(ChannelHandlerContext channelContext, Map<String, Object> params, Map<String, File> files) {
		super();
		this.channelContext = channelContext;
		this.params = params;
		this.files = files;
		this.actionName = params.get("action").toString();
	}

	public ChannelHandlerContext getChannelContext() {
		return channelContext;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public String getActionName() {
		return actionName;
	}

	public Map<String, File> getFiles() {
		return files;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [channelContext=" + channelContext + ", params=" + params
				+ ", files=" + this.files + "]";
	}

}
