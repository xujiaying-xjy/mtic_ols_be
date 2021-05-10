package com.mantoo.mtic.module.netty.websocket;

import com.mantoo.mtic.module.netty.tcp.TcpServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author keith
 * @version 1.0
 * @date 2021-04-22
 */
@Slf4j
public class WebSocketServer {

    /**
     * 创建两个线程组
     */
    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    /**
     * 启动服务
     *
     * @param port
     * @throws InterruptedException
     */
    public void run(Integer port) throws InterruptedException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();

                        //基于http协议，使用http编码和解码器
                        pipeline.addLast(new HttpServerCodec());
                        //是已快方式写，添加ChunkedWriteHandler处理器
                        pipeline.addLast(new ChunkedWriteHandler());
                        //http协议在传输过程中是分段的，HttpObjectAggregator可以将多个段聚合起来
                        pipeline.addLast(new HttpObjectAggregator(8192));
                        //对于websocket，它的数据是以帧的形式传递的
                        //WebSocketServerProtocolHandler将http协议升级为ws协议，保持长连接
                        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
                        //自定义handler，处理业务
                        pipeline.addLast(new WebSocketServerHandler());
                    }
                });
        //绑定端口
        ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
        if (channelFuture.isSuccess()) {
            log.info("WebSocket服务器启动完成");
        }
    }

    /**
     * 停止服务
     *
     * @throws InterruptedException
     */
    public void destroy() throws InterruptedException {
        bossGroup.shutdownGracefully().sync();
        workerGroup.shutdownGracefully().sync();
        log.info("关闭WebSocket服务器");
    }
}
