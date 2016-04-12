package com.sp.video.yi.demo;

import android.util.Log;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Created by Administrator on 2016/4/12.
 */
public enum TcpClient {
    INSTANCE;
    public static String HOST = "192.168.42.1";
    public static int PORT = 7878;
    public static Bootstrap bootstrap;


    public void init(){
        bootstrap = getBootstrap();
    }
    /**
     * 初始化Bootstrap
     *
     * @return
     */
    public Bootstrap getBootstrap() {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class);
        b.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
                pipeline.addLast("handler", new TcpClientHandler());
            }
        });
// b.option(ChannelOption.SO_KEEPALIVE, true);
        return b;
    }

    public Channel getChannel() {
        Channel channel = null;
        try {
            channel = bootstrap.connect(HOST, PORT).sync().channel();
        } catch (Exception e) {
           Log.e("wwc", String.format("连接Server(IP[%s],PORT[%s])失败 ", HOST, PORT) + e.getMessage());
            return null;
        }
        return channel;
    }

    public void sendMsg(Channel channel, Object msg) throws Exception {
        if (channel != null) {
            channel.writeAndFlush(msg).sync();
        } else {
            Log.e("wwc","消息发送失败,连接尚未建立!");
        }
    }

 /*   public static void main(String[] args) throws Exception {
        try {
            long t0 = System.nanoTime();
            byte[] value = null;
            Channel channel = null;
            for (int i = 0; i < 50000; i++) {
                channel = getChannel(HOST, PORT);
                value = (i + ",你好").getBytes();
                ByteBufAllocator alloc = channel.alloc();
                ByteBuf buf = alloc.buffer(value.length);
                buf.writeBytes(value);
                TcpClient.sendMsg(channel, buf);
            }
            long t1 = System.nanoTime();
            System.out.println((t1 - t0) / 1000000.0);
            Thread.sleep(5000);
            System.exit(0);
        } catch (Exception e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }

    }*/
}
