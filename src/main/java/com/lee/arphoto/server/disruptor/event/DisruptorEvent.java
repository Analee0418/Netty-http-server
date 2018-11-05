package com.lee.arphoto.server.disruptor.event;

/**
 * RingBuffer 处理队列中的元素
 * 
 * {@link com.lmax.disruptor.EventHandler}
 * 
 * @author xiaofei.lee(13651027213@163.com)
 *
 *         始终相信天道酬勤!
 */
public class DisruptorEvent<T extends com.lee.arphoto.server.disruptor.pojo.RequestPojo> {
	private T pojo;

	public T getPojo() {
		return pojo;
	}

	public void setPojo(T pojo) {
		this.pojo = pojo;
	}

}
