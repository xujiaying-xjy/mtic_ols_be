package com.mantoo.mtic.common.utils;

import com.mantoo.mtic.exception.ErrorInfo;
import com.mantoo.mtic.exception.MticException;
import com.mantoo.mtic.module.netty.tcp.SwitchNettyTools;
import com.mantoo.mtic.module.netty.tcp.TcpServerHandler;
import com.mantoo.mtic.module.netty.websocket.WebSocketServerHandler;
import com.mantoo.mtic.module.system.entity.equip.EquipInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

/**
 * @author keith
 * @version 1.0
 * @date 2021-03-01
 */
public class SendUtils {

    /**
     * 发送指令客户端(16进制)
     *
     * @param cmd           指令
     * @param channelLongId 通道
     * @return
     */
    public static String sendClient(String cmd, String channelLongId, String addressCode) {
        ChannelId channelId = TcpServerHandler.channelMap.get(channelLongId);
        if (channelId != null) {
            ByteBuf byteBuf = Unpooled.buffer().writeBytes(ByteUtil.hexString2Bytes(cmd.replaceAll(" ", "")));
            TcpServerHandler.channelGroup.find(channelId).writeAndFlush(byteBuf);
            SwitchNettyTools.initReceiveMsg(channelLongId + "-" + addressCode);
            byte[] bytes = SwitchNettyTools.waitReceiveMsg(channelLongId + "-" + addressCode, null);
            if (bytes == null){
                return null;
            }
            return ByteUtil.toHexString(bytes);
        } else {
            throw new MticException("网关离线", ErrorInfo.SERVICE_ERROR.getCode());
        }
    }

    /**
     * 发送指令客户端
     *
     * @param cmd           指令
     * @param channelLongId 通道
     * @return
     */
    public static String toStringSendClient(String cmd, String channelLongId, String addressCode) {
        ChannelId channelId = TcpServerHandler.channelMap.get(channelLongId);
        if (channelId != null) {
            ByteBuf byteBuf = Unpooled.buffer().writeBytes(cmd.getBytes());
            TcpServerHandler.channelGroup.find(channelId).writeAndFlush(byteBuf);
            SwitchNettyTools.initReceiveMsg(channelLongId + "-" + addressCode);
            byte[] bytes = SwitchNettyTools.waitReceiveMsg(channelLongId + "-" + addressCode, null);
            if (bytes == null){
                return null;
            }
            return new String(bytes);
        } else {
            throw new MticException("网关离线", ErrorInfo.SERVICE_ERROR.getCode());
        }
    }

    /**
     * 发送指令客户端不需要回复(16进制)
     *
     * @param cmd           指令
     * @param channelLongId 通道
     * @return
     */
    public static void sendClient(String cmd, String channelLongId) {
        ChannelId channelId = TcpServerHandler.channelMap.get(channelLongId);
        if (channelId != null) {
            ByteBuf byteBuf = Unpooled.buffer().writeBytes(ByteUtil.hexString2Bytes(cmd.replaceAll(" ", "")));
            TcpServerHandler.channelGroup.find(channelId).writeAndFlush(byteBuf);
        } else {
            throw new MticException("网关离线", ErrorInfo.SERVICE_ERROR.getCode());
        }
    }

    /**
     * 发送内容给webSocket客户端不需要回复
     *
     * @param content       内容
     * @param channelLongId 通道
     * @return
     */
    public static void sendWebSocketClient(String content, String channelLongId) {
        ChannelId channelId = WebSocketServerHandler.channelMap.get(channelLongId);
        if (channelId != null) {
            WebSocketServerHandler.channelGroup.find(channelId).writeAndFlush(new TextWebSocketFrame(content));
        } else {
            throw new MticException("客户端未连接", ErrorInfo.SERVICE_ERROR.getCode());
        }
    }
}
