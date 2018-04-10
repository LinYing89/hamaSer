package com.bairock.iot.hamaser.communication;

import com.bairock.iot.intelDev.device.OrderHelper;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class PadHandler extends ChannelInboundHandlerAdapter{
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		PadChannelBridge.channelGroup.add(ctx.channel());
		PadChannelBridgeHelper.getIns().setChannelId(ctx.channel().id().asShortText());
		ctx.writeAndFlush(Unpooled.copiedBuffer(OrderHelper.getOrderMsg("h2").getBytes()));
		super.channelActive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf m = (ByteBuf)msg;
		try{
			byte[] req = new byte[m.readableBytes()];
			m.readBytes(req);
			String str = new String(req, "GBK");
			PadChannelBridgeHelper.getIns().channelReceived(ctx.channel().id().asShortText(), str);
		}finally{
			m.release();
//			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		PadChannelBridgeHelper.getIns().channelUnRegistered(ctx.channel().id().asShortText());
		ctx.close();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		PadChannelBridgeHelper.getIns().channelUnRegistered(ctx.channel().id().asShortText());
		ctx.close();
	}
}
