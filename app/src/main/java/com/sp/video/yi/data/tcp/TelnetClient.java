package com.sp.video.yi.data.tcp;

import android.util.Log;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by Administrator on 2016/4/12.
 */
public class TelnetClient {
    public static Bootstrap bootstrap;

    public TelnetClient(){
        bootstrap = getBootstrap();
    }
    /**
     * 初始化Bootstrap
     *
     * @return
     */
    public Bootstrap getBootstrap() {
        if(null != bootstrap){
            return bootstrap;
        }
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new TelnetClientInitializer());
// b.option(ChannelOption.SO_KEEPALIVE, true);
        return b;
    }

    public Channel getChannel(String host,int port) {
        Channel channel = null;
        try {
            Log.d("wwc","Thread: getChannel id = "+Thread.currentThread().getId());
            channel = bootstrap.connect(host, port).sync().channel();
        } catch (Exception e) {
           Log.e("wwc", String.format("连接Server(IP[%s],PORT[%s])失败 ", host, port) + e.getMessage());
            return null;
        }
        return channel;
    }

    public void sendMsg(Channel channel, String msg,ChannelFutureListener listener) throws Exception {
        if (channel != null) {
            channel.writeAndFlush(msg).addListener(listener);
        } else {
            Log.e("wwc","消息发送失败,连接尚未建立!");
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
