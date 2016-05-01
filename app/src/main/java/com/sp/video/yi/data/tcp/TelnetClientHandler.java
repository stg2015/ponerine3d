/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.sp.video.yi.data.tcp;

import android.util.Log;

import com.sp.video.yi.data.model.GetTokenMsg;
import com.sp.video.yi.data.model.connection.ConnectionResponseProvider;

import java.net.InetSocketAddress;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Handles a client-side channel.
 */
@Sharable
public class TelnetClientHandler extends SimpleChannelInboundHandler<String> {

    /**
     * 通过连接id(ip:port),回调接口
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        InetSocketAddress inetSocketAddress = ((NioSocketChannel) ctx.channel()).remoteAddress();
        String connectionId = inetSocketAddress.getAddress().getHostAddress()+":"+inetSocketAddress.getPort();
        Log.d("wwc", "Thread: channelRead0 id = " + Thread.currentThread().getId() + "  msg = " + msg + "   from " + connectionId);
        ConnectionResponseProvider.INSTANCE.onMsgResponse(connectionId, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        Log.d("wwc", "exceptionCaught " + cause.getMessage());
        ctx.close();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        Log.d("wwc", "channelRegistered");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        Log.d("wwc", "channelUnregistered");
        InetSocketAddress inetSocketAddress = ((NioSocketChannel) ctx.channel()).remoteAddress();
        if(null != inetSocketAddress){
            String connectionId = inetSocketAddress.getAddress().getHostAddress() + ":" + inetSocketAddress.getPort();
            ConnectionResponseProvider.INSTANCE.deleteConnection(connectionId);
        }
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Log.d("wwc", "channelActive");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Log.d("wwc", "channelInactive");
        InetSocketAddress inetSocketAddress = ((NioSocketChannel) ctx.channel()).remoteAddress();
        if(null != inetSocketAddress){
            String connectionId = inetSocketAddress.getAddress().getHostAddress() + ":" + inetSocketAddress.getPort();
            ConnectionResponseProvider.INSTANCE.deleteConnection(connectionId);
        }
        super.channelInactive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        Log.d("wwc", "channelReadComplete");
        super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {
//                Log.d("wwc", "userEventTriggered = " + "READ");
//                ctx.close();
            } else if (e.state() == IdleState.WRITER_IDLE) {
                Log.d("wwc", "userEventTriggered = " + "WRIT");
                ctx.writeAndFlush(new GetTokenMsg().toString());
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

}
