package com.mantoo.mtic.module.netty.websocket;

import com.mantoo.mtic.module.netty.tcp.TcpServerHandler;
import com.mantoo.mtic.module.system.entity.equip.EquipInfo;
import com.mantoo.mtic.module.system.exMapper.equip.EquipInfoMapper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * TextWebSocketFrame;表示一个文本帧
 *
 * @author keith
 * @version 1.0
 * @date 2021-01-26
 */
@Slf4j
@Component
public class WebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Resource
    private EquipInfoMapper equipInfoMapper;

    public static WebSocketServerHandler webSocketServerHandler;

    private boolean isInit = true;

    @PostConstruct
    public void init() {
        webSocketServerHandler = this;
    }

    /**
     * 定义一个channel组，管理所有的channel
     * GlobalEventExecutor 全局事件执行器，是一个单例
     */
    public static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    public static final ConcurrentMap<String, ChannelId> channelMap = new ConcurrentHashMap<>();

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        log.debug("WebSocket IP:" + channel.remoteAddress());
        //将该客户端加入聊天信息推送给其他客户端
        channelGroup.add(channel);
        channelMap.put(channel.id().asLongText(), channel.id());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "离线了");
        disconnect(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        Channel channel = ctx.channel();
        System.out.println("服务器端收到消息：" + msg.text());
        if (isInit) {
            isInit = false;
            //插入设备固件升级通道id
            //todo 后期可以改成像redis里面插入设备和通道的关系
            int i = webSocketServerHandler.equipInfoMapper.insertFirmwareChannelId(Long.parseLong(msg.text()), channel.id().asLongText());
            if (i == 0) {
                //对应网关没找到，直接断开连接
                ctx.disconnect();
                channelMap.remove(channel.id().asLongText());
                channelGroup.remove(channel);
                return;
            }
        } else {
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        System.out.println("客户端断开连接" + ctx.channel().id().asLongText());
        disconnect(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("异常发生" + cause.getMessage());
        disconnect(ctx);
        ctx.close();
    }

    /**
     * 断开连接逻辑
     *
     * @param ctx 上下文对象
     */
    public void disconnect(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        EquipInfo equipInfoObj = new EquipInfo();
        equipInfoObj.setFirmwareChannelId(channel.id().asLongText());
        EquipInfo equipInfo = webSocketServerHandler.equipInfoMapper.selectOne(equipInfoObj);
        if (equipInfo != null) {
            equipInfo.setFirmwareChannelId(null);
            webSocketServerHandler.equipInfoMapper.updateByPrimaryKeySelective(equipInfo);
        }
        //删除通道id
        channelMap.remove(channel.id().asLongText());
        channelGroup.remove(channel);
        ctx.disconnect();
    }
}
