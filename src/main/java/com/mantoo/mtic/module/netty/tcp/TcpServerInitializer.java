package com.mantoo.mtic.module.netty.tcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * netty服务初始化类
 *
 * @author keith
 * @version 1.0
 * @date 2021-02-06
 */
public class TcpServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new IdleStateHandler(1800, 0, 0, TimeUnit.SECONDS));
        pipeline.addLast(new TcpServerHandler());
    }
}
