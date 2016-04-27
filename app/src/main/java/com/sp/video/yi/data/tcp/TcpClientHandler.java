package com.sp.video.yi.data.tcp;

import android.util.Log;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

/**
 * Created by Administrator on 2016/4/12.
 */
public class TcpClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        Log.d("wwc", "channelActive");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        Log.d("wwc", "channelInactive");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Log.d("wwc", "channelRead");
        ByteBuf buf = (ByteBuf) msg;
        byte[] dst = new byte[buf.capacity()];
        buf.readBytes(dst);
        Log.d("wwc", "client接收到服务器返回的消息:" + new String(dst));
        ReferenceCountUtil.release(msg);

    }

}
