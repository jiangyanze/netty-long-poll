package com.codechiev.net;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//把String转换成httpResponse
public class StringEncoder extends ChannelOutboundHandlerAdapter {
	private Logger	logger	= LoggerFactory.getLogger(StringEncoder.class);

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		logger.info("StringEncoder response to client.");
		if(msg instanceof String){
			FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(((String) msg)
					.getBytes()));
			response.headers().set(CONTENT_TYPE, "text/plain");
			response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
			response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
			ChannelFuture f = ctx.writeAndFlush(response);
			f.addListener(ChannelFutureListener.CLOSE);
		}else if(msg instanceof ByteBuf){
			FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, (ByteBuf) msg);
			response.headers().set(CONTENT_TYPE, "text/html; charset=utf-8");
			response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
			response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
			ChannelFuture f = ctx.writeAndFlush(response);
			f.addListener(ChannelFutureListener.CLOSE);
		}
	}
}