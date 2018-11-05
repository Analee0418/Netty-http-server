package com.lee.arphoto.server.disruptor.producer;

import com.lee.arphoto.server.disruptor.event.DisruptorEvent;
import com.lee.arphoto.server.disruptor.pojo.RequestPojo;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

/**
 * Disruptor Event 生产者
 * 
 * @author xiaofei.lee(13651027213@163.com)
 */
public class DisruptorEventProducer {
	private final RingBuffer<com.lee.arphoto.server.disruptor.event.DisruptorEvent<com.lee.arphoto.server.disruptor.pojo.RequestPojo>> ringBuffer;

	public DisruptorEventProducer(RingBuffer<DisruptorEvent<RequestPojo>> ringBuffer) {
		this.ringBuffer = ringBuffer;
	}

	private static final EventTranslatorOneArg<DisruptorEvent<RequestPojo>, RequestPojo> TRANSLATOR = new EventTranslatorOneArg<DisruptorEvent<RequestPojo>, RequestPojo>() {
		public void translateTo(DisruptorEvent<RequestPojo> event, long sequence, RequestPojo pojo) {
			event.setPojo(pojo);
		}
	};

	public void onData(RequestPojo pojo) {
		ringBuffer.publishEvent(TRANSLATOR, pojo);
	}
}
