package com.codechiev;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.codechiev.net.HttpRequestHandler;
import com.codechiev.net.StringEncoder;
import com.codechiev.service.HandlerService;

public class TestHttpLongPoll {
	private static Logger logger = LoggerFactory.getLogger(TestHttpLongPoll.class);
	private static ApplicationContext context;

	public void start() throws Exception {
		context = new ClassPathXmlApplicationContext("applicationContext.xml");
		Properties properties = (Properties) context.getBean("properties");
		int server = Integer.valueOf(properties.getProperty("server.port"));
		//do some initialization
		HandlerService hs = context.getBean(HandlerService.class);
		hs.init();

		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap boot = new ServerBootstrap();
			boot.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							// 都属于ChannelOutboundHandler，逆序执行
							ch.pipeline().addLast(new HttpResponseEncoder());
							ch.pipeline().addLast(new StringEncoder());

							// 都属于ChannelIntboundHandler，按照顺序执行
							ch.pipeline().addLast(new  HttpRequestDecoder());
							ch.pipeline().addLast(context.getBean(HttpRequestHandler.class));
						}
					}).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

			ChannelFuture f = boot.bind(server).sync();

			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		TestHttpLongPoll server = new TestHttpLongPoll();
		System.setProperty("spring.profiles.active", "home-dev" );//   "development"
		server.start();
		logger.debug("launching done");
	}
}