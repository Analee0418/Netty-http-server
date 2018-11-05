package com.lee.arphoto.server.disruptor.consumer;

import com.lmax.disruptor.EventHandler;

/**
 * 
 * Disruptor 事件处理器
 * 
 * @author xiaofei.lee(13651027213@163.com)
 *
 *         始终相信天道酬勤!
 */
public abstract class AbstractDisruptorEventHandler<T extends com.lee.arphoto.server.disruptor.pojo.RequestPojo>
		implements EventHandler<com.lee.arphoto.server.disruptor.event.DisruptorEvent<T>> {
	// TODO
}
