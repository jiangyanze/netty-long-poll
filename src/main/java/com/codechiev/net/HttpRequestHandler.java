package com.codechiev.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.codechiev.common.GeneralRequest;
import com.codechiev.service.ConnectionService;
import com.codechiev.service.HandlerService;
import com.codechiev.service.RequestService;
import com.codechiev.utils.HttpUriUtils;

@Component
@Scope("prototype")
public class HttpRequestHandler extends ChannelInboundHandlerAdapter {
	private static Logger logger = LoggerFactory.getLogger(HttpRequestHandler.class);
	@Autowired
	private RequestService msgService;
	@Autowired
	private HandlerService handlerService;
	@Autowired
	private ConnectionService connectionService;
	@Autowired
	private ResourceLoader resourceLoader;
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        
		logger.debug("msg type: {}", msg.getClass());
		if (msg instanceof DefaultHttpRequest) {
			processClientMessage(msg, ctx);
		}
		//If you prefer not to receive HttpContent in your handler,
		//place HttpObjectAggregator after HttpObjectDecoder in the ChannelPipeline.
		else if (msg instanceof HttpContent) {
			HttpContent content = (HttpContent) msg;
			logger.debug("content: {}",new String( content.content().array()));
			//processClientMessage((GeneralRequest) msg, ctx);
		}
		else{
			ctx.channel().write("ok");
		}
			
		 ReferenceCountUtil.release(msg);
	}
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
    
	private void processClientMessage(Object msg, ChannelHandlerContext ctx) {
		HttpRequest request = (HttpRequest) msg;
		HttpHeaders headers = request.headers();
		HttpMethod method = request.getMethod();
		String uriStr = request.getUri();
		for(Entry<String, String> entry :headers.entries()){
			if(entry.getKey()!=null && entry.getValue()!=null){
				logger.debug("header {}:{}", entry.getKey(), entry.getValue());
			}
		}
		logger.debug("method :{}",method.name());
		logger.debug("uriStr :{}",uriStr);

		Map<String, String> map = HttpUriUtils.parseUri(uriStr);
		String token = map.get("token");
		
		//FIXME some token verification
		if(null != token){
			//long-poll request
			if(token.indexOf("long-poll")==0){
				BaseConnection<?> conn = connectionService.onCreateConnection(ctx.channel());
				msgService.receive(new GeneralRequest(map), conn);
			}else{
				ctx.channel().write("undefine-token");
			}
		}else {
			Resource rs = resourceLoader.getResource("classpath:test.html");  	
	    	try{
				InputStream is = rs.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				StringBuffer sb = new StringBuffer();
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line).append("\r\n");
				}
				br.close();
				ByteBuf content = Unpooled.copiedBuffer(sb.toString(), CharsetUtil.UTF_8);
				ctx.channel().write(content);
	    	}catch(IOException e){
	    		e.printStackTrace();
	    	}
		}
	}
}