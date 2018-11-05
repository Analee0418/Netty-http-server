package com.lee.arphoto.server.disruptor.consumer;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lee.arphoto.action.AbstractAction;
import com.lee.arphoto.action.cache.ActionMappingCache;
import com.lee.arphoto.exception.AppErrorException;
import com.lee.arphoto.message.MessageAppError;
import com.lee.arphoto.server.disruptor.event.DisruptorEvent;
import com.lee.arphoto.server.disruptor.pojo.RequestPojo;
import com.lee.arphoto.spring.config.process.ProcessorProperties;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

/**
 * 
 * Disruptor 事件处理器
 * 
 * @author xiaofei.lee(13651027213@163.com)
 *
 *         始终相信天道酬勤!
 */
public class DisruptorEventHandler extends AbstractDisruptorEventHandler<RequestPojo> {

	private static Log Log = LogFactory.getLog(AbstractDisruptorEventHandler.class);

	/**
	 * 线程池-处理时间过长的请求转发到该线程处理
	 */
	private static final ExecutorService executor = new ThreadPoolExecutor(
			com.lee.arphoto.spring.config.process.ProcessorProperties.getCorePoolSize(),
			ProcessorProperties.getMaximumPoolSize(), ProcessorProperties.getKeepAliveTimeSeconds(), TimeUnit.SECONDS,
			new SynchronousQueue<Runnable>(), new ThreadFactory() {
				@Override
				public Thread newThread(Runnable r) {
					return new Thread(r, "Request-Message-Executor");
				}
			});

	public void onEvent(DisruptorEvent<RequestPojo> event, long sequence, boolean endOfBatch) {
		try {

			RequestPojo pojo = event.getPojo();
			// if (Log.isDebugEnabled()) {
			// Log.debug("Handle Disruptor Event : " + event.getPojo());
			// }

			final ChannelHandlerContext ctx = pojo.getChannelContext();

			String actionName = pojo.getActionName();
			final AbstractAction handler = ActionMappingCache.getActionHandler(actionName);
			if (null == handler) { // 非法请求
				Log.error("illegal request@" + actionName);
				return;
			}

			final Map<String, Object> actionParam = pojo.getParams();
			if (Log.isDebugEnabled()) {
				Log.debug("Process action [" + actionName + ", " + (handler.isEasy() ? "easy" : "difficulty") + " , "
						+ actionParam + "]");
			}
			final Map<String, File> files = pojo.getFiles();

			// 耗时不长的Disruptor直接处理，如果耗时较长或易出错的由各自线程池处理
			if (handler.isEasy()) {

				process(ctx, handler, actionParam, files);

			} else {

				executor.submit(new Runnable() {
					@Override
					public void run() {
						process(ctx, handler, actionParam, files);
					}
				});

			}

		} catch (Exception e) {
			Log.error("System Processor Error: ", e);
		}
	}

	private static void process(final ChannelHandlerContext ctx, final AbstractAction handler,
			final Map<String, Object> actionParam, final Map<String, File> uploadFiles) {
		try {
			Object response = handler.handle(actionParam, uploadFiles);
			ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);

		} catch (AppErrorException e) {

			ctx.writeAndFlush(new MessageAppError(e.getCode(), e.getArgs())).addListener(ChannelFutureListener.CLOSE);

		} catch (Exception e) {
			Log.error("Force Close Channel, Handle HTTP request ERROR: ", e);
			ctx.channel().close();
		}
	}

}
