package com.codechiev.net;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author james
 *
 */
public class NettyConnection extends BaseConnection<Channel> {
	private static final Logger logger = LoggerFactory.getLogger(NettyConnection.class);

	// private static final long MAX_PKT_ACC_BYTES = 1048576L;
	// private static final long FINE_PKT_ACC_BYTES = 10240L;

	private String ipAddress;
	private long timeMark;
	//private Deque<Object> msgQueue = new ArrayDeque<Object>();
	//private int sent = 0;//only allow sent once

	public NettyConnection(Channel channel, String uuid) {
		
		this.channel = channel;
		this.uuid = uuid;
		this.ipAddress = this.channel.remoteAddress().toString();
	}

	public Channel getBindChannel() {
		return this.channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public String getIpAddress() {
		return this.ipAddress;
	}

	public long getTimeMark() {
		return timeMark;
	}

	public void markNow() {
		this.timeMark = System.currentTimeMillis();
	}

	public synchronized void close() {
		if (channel.isActive()) {
			channel.close();
		}
	}

	
	/* Request to write a message via this Channel through the ChannelPipeline. 
	 * This method will not request to actual flush, 
	 * so be sure to call flush() once you want to request to flush all pending data to the actual transport.
	 */
	@Override
	public ChannelFuture send(Object rsp) {
		if (channel.isActive()) {
			if (channel.isWritable()) {//
				return channel.write(rsp);
			} else {
				// Any write requests made when this method returns false are
				// queued until the I/O thread is ready to process the queued
				// write requests
				logger.warn("requests are queued");
				return channel.write(rsp);
			}
		} else {
			logger.warn("send rsp {} to unavailable session {}, rsp dropped", rsp, getUuid());
		}
		return null;
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("NettyConn{Name=").append(getUuid()).append(", IP=").append(getIpAddress());
		return buf.toString();
	}

	
	@Override
	public void deliver(Object message) {
		channel.writeAndFlush(message);
	}
	

	@Override
	public void sendAndClose(Object message) {
		if (this.channel.isActive()) {
			ChannelFuture cf = send(message);
			if (cf != null) {
				cf.addListener(new ChannelFutureListener() {
					@Override
					public void operationComplete(ChannelFuture future) throws Exception {
						close();
					}
				});
			}
		}
	}

	@Override
	public void deliverMsgCached() {
		// TODO Auto-generated method stub
		
	}

}