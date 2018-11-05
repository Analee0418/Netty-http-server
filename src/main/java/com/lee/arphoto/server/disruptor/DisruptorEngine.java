package com.lee.arphoto.server.disruptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lee.arphoto.server.disruptor.event.DisruptorEvent;
import com.lee.arphoto.server.disruptor.pojo.RequestPojo;
import com.lee.arphoto.server.disruptor.producer.DisruptorEventProducer;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import io.netty.util.concurrent.DefaultThreadFactory;

/**
 * Disruptor 队列引擎(不知道该怎么命名 先这么叫吧)
 * 
 * @author xiaofei.lee(13651027213@163.com)
 *
 *         始终相信天道酬勤!
 */
public class DisruptorEngine {
	private static Disruptor<DisruptorEvent<RequestPojo>> disruptor = null;

	private static final Log logger = LogFactory.getLog(DisruptorEngine.class);

	private final static EventFactory<DisruptorEvent<RequestPojo>> factory = new EventFactory<DisruptorEvent<RequestPojo>>() {
		public DisruptorEvent<RequestPojo> newInstance() {
			return new DisruptorEvent<RequestPojo>();
		}
	};

	private static DisruptorEventProducer producer;

	/**
	 * startup
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void initialization(EventHandler handler) {
		// disruptor = new Disruptor<DisruptorEvent<RequestPojo>>(factory, 4096,
		// new DefaultThreadFactory("Disruptor Thread Factory"));

		/*
		 * 使用锁和条件变量。CPU资源的占用少，延迟大。
		 */
		disruptor = new Disruptor<>(factory, 2048,
				new DefaultThreadFactory("Disruptor Thread Factory", Thread.MAX_PRIORITY), ProducerType.MULTI,
				new BlockingWaitStrategy());

		disruptor.handleEventsWith(handler);

		disruptor.start();

		producer = new DisruptorEventProducer(disruptor.getRingBuffer());

		if (logger.isInfoEnabled()) {
			logger.info("Disruptor startup!");
		}
	}

	/**
	 * push to queue
	 * 
	 * @param pojo
	 */
	public static void pushEvent(RequestPojo pojo) {
		producer.onData(pojo);
	}

	public static String getBufferSize() {
		return disruptor.toString();
	}
}