package com.sp.video.yi.data.tcp;

import android.os.Looper;
import android.util.Log;


import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by Administrator on 2016/4/12.
 */
public class TelnetClient {
    public static Bootstrap bootstrap;
    public static EventLoopGroup group;

    public TelnetClient() {
        bootstrap = getBootstrap();
    }

    /**
     * 初始化Bootstrap
     *
     * @return
     */
    public Bootstrap getBootstrap() {
        group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)//5秒超时
                .handler(new TelnetClientInitializer());
// b.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap = b;
        return b;
    }

    public void closeAllChannel() {
        group.shutdownGracefully();
    }

    public Channel connectChannel(String host, int port) {
        if (null == bootstrap || null ==group || group.isShutdown()) {
            bootstrap = getBootstrap();
        }
        Channel channel = null;
        try {
            Log.d("wwc", "Thread: connectChannel id = " + Thread.currentThread().getId());
            channel = bootstrap.connect(host, port).addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture f) throws Exception {
                    if (f.isSuccess()) {
                        Log.e("wwc", String.format("连接Server(IP[%s],PORT[%s])成功: ", host, port) + f.channel().toString() + " channel id = " + f.channel().id());
                    } else {
                        Log.e("wwc", String.format("连接Server(IP[%s],PORT[%s])失败,重试中 ", host, port));
                        f.channel().eventLoop().schedule(new Callable<Channel>() {
                            @Override
                            public Channel call() throws Exception {
                                return connectChannel(host, port);
                            }
                        }, 1, TimeUnit.SECONDS);
                    }
                }
            }).sync().channel();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return channel;
    }

    public void sendMsg(Channel channel, String msg) throws Exception {
        if (channel != null) {
            channel.writeAndFlush(msg).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    Log.d("wwc", "Thread: sendMsg id = " + Thread.currentThread().getId());
                    Looper.prepare();
                    if (!future.isSuccess()) {
                        future.cause().printStackTrace();
                        Log.d("wwc", "发送不成功");
                    } else {
                        Log.d("wwc", "发送成功");
                    }
                }
            });
        } else {
            Log.e("wwc", "消息发送失败,连接尚未建立!");
        }
    }


    /*
    *  public static void main(String[] args) throws Exception {
        // Configure SSL.
        final SslContext sslCtx;
        if (SSL) {
            sslCtx = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        } else {
            sslCtx = null;
        }

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioSocketChannel.class)
             .handler(new TelnetClientInitializer(sslCtx));

            // Start the connection attempt.
            Channel ch = b.connect(HOST, PORT).sync().channel();

            // Read commands from the stdin.
            ChannelFuture lastWriteFuture = null;
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            for (;;) {
                String line = in.readLine();
                if (line == null) {
                    break;
                }

                // Sends the received line to the server.
                lastWriteFuture = ch.writeAndFlush(line + "\r\n");

                // If user typed the 'bye' command, wait until the server closes
                // the connection.
                if ("bye".equals(line.toLowerCase())) {
                    ch.closeFuture().sync();
                    break;
                }
            }

            // Wait until all messages are flushed before closing the channel.
            if (lastWriteFuture != null) {
                lastWriteFuture.sync();
            }
        } finally {
            group.shutdownGracefully();
        }
    }
    * */
}
