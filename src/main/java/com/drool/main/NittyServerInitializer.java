package com.drool.main;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NittyServerInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		System.out.println("22222222222222");
ChannelPipeline pipeline = channel.pipeline();
//			 pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));
pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192,Delimiters.lineDelimiter()));
pipeline.addLast("decoder", new StringDecoder());
 pipeline.addLast("encoder", new StringEncoder());
 pipeline.addLast("handler", new NittyServerHandler());
			
		
	}

}
