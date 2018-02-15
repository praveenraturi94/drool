package com.drool.main;

import java.util.concurrent.Executors;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class NettyTest {

	private static final int SERVER_PORT = 8181;

	public static void main(String args[]) {
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup,workerGroup)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 100)
			.handler(new LoggingHandler(LogLevel.DEBUG))
			.childHandler(new ChannelInitializer<SocketChannel>() {
				
			@Override
			public void initChannel(SocketChannel channel) throws Exception{
				ChannelPipeline pipeline = channel.pipeline();
				pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));
//				pipeline.addLast("EchoHandler", new EchoPipe());
			}
			});
		} catch (Exception e) {

		} finally {
		}
	}
}
