package com.bairock.iot.hamaser.communication;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * upload and download server
 * @author Administrator
 *
 */
public class UpDownloadServer {

	public static int PORT = 4046;

	private ServerBootstrap b;
	private ChannelFuture f;
	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;
	
	public void run() throws Exception {
		bossGroup = new NioEventLoopGroup(); // (1)
		workerGroup = new NioEventLoopGroup();
		try {
			b = new ServerBootstrap(); // (2)
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class) // (3)
					.childHandler(new ChannelInitializer<SocketChannel>() { // (4)
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new UpDownloadHandler());
						}
					}).option(ChannelOption.SO_BACKLOG, 128) // (5)
					.childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

			// Bind and start to accept incoming connections.
//			ChannelFuture f = b.bind(port).sync(); // (7)
			f = b.bind(PORT); // (7)

			// Wait until the server socket is closed.
			// In this example, this does not happen, but you can do that to
			// gracefully
			// shut down your server.
//			f.channel().closeFuture().sync();
			f.channel().closeFuture();
		} finally {
//			workerGroup.shutdownGracefully();
//			bossGroup.shutdownGracefully();
		}
	}
	
	public void close() {
		if(null != bossGroup) {
			bossGroup.shutdownGracefully();
		}
		if(null != workerGroup) {
			workerGroup.shutdownGracefully();
		}
	}
}
