package com.mantoo.mtic.module.netty.tcp;

import cn.hutool.json.JSONUtil;
import com.mantoo.mtic.common.entity.SocketResult;
import com.mantoo.mtic.common.utils.ByteUtil;
import com.mantoo.mtic.common.utils.DateUtils;
import com.mantoo.mtic.common.utils.SendUtils;
import com.mantoo.mtic.exception.ErrorInfo;
import com.mantoo.mtic.exception.MticException;
import com.mantoo.mtic.module.system.entity.equip.EquipInfo;
import com.mantoo.mtic.module.system.exMapper.equip.EquipInfoMapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author keith
 * @version 1.0
 * @date 2021-01-25
 */
@Slf4j
@Component
public class TcpServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Resource
    private EquipInfoMapper equipInfoMapper;


    public static TcpServerHandler tcpServerHandler;

    public static HashSet<String> equipWorkSet = new HashSet<>();

    private boolean isInit = true;
    //起始标志长度
    final static int BEGIN_FLAG_LENGTH = 2;
    //数据长度
    final static int DATA_FLAG_LENGTH = 1;
    //结束标志长度
    final static int END_FLAG_LENGTH = 2;

    //数据存储
    @PostConstruct
    public void init() {
        tcpServerHandler = this;
    }

    /**
     * 定义一个channel组，管理所有的channel
     * GlobalEventExecutor 全局事件执行器，是一个单例
     */
    public static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    public static final ConcurrentMap<String, ChannelId> channelMap = new ConcurrentHashMap<>();

    /**
     * 连接建立，第一个被执行
     *
     * @param ctx 上下文对象
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        log.debug("Connected IP:" + channel.remoteAddress());
        //将该客户端加入聊天信息推送给其他客户端
        channelGroup.add(channel);
        channelMap.put(channel.id().asLongText(), channel.id());
    }

    /**
     * 表示channel处于活动状态
     *
     * @param ctx 上下文对象
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println(ctx.channel().remoteAddress() + "上线了");
    }

    /**
     * 表示channel处于非活动状态
     *
     * @param ctx 上下文对象
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println(ctx.channel().remoteAddress() + "离线了");
        disconnect(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        //todo 需要考虑拆包粘包问题
        Channel channel = ctx.channel();
        ByteBuf cmd = msg.copy();
        byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        if (isInit) {
            isInit = false;
            String content = new String(bytes, CharsetUtil.UTF_8).trim();
            log.debug("注册包，网关编号为：{}", content);
            EquipInfo equipInfoObj = new EquipInfo();
            equipInfoObj.setEquipMac(content);
            EquipInfo equipInfo = tcpServerHandler.equipInfoMapper.selectOne(equipInfoObj);
            //注册包逻辑：插入通道和网关关系
            if (!Optional.ofNullable(equipInfo).isPresent()) {
                //对应网关没找到，直接断开连接
                ctx.disconnect();
                channelMap.remove(channel.id().asLongText());
                channelGroup.remove(channel);
                return;
            }
            //更新网关状态
            equipInfo.setIsOnline("0");
            equipInfo.setLastOntime(DateUtils.getNowDate());
            equipInfo.setChannelId(channel.id().asLongText());
            tcpServerHandler.equipInfoMapper.updateByPrimaryKeySelective(equipInfo);
            equipWorkSet.remove(equipInfo.getEquipMac());
        } else {
            String content = new String(bytes, CharsetUtil.UTF_8).trim();
            //1.判断是否是心跳包
            if ("mantoo".equals(content)) {
                //不做处理
                log.debug("心跳包发送时间：{}", LocalDateTime.now());
            } else if (content.contains("CCID")) {
                log.debug("SIM卡，{}", content);
                String simCard = content.substring(6, 26);
                SwitchNettyTools.setReceiveMsg(channel.id().asLongText() + "-" + "SIM", simCard.getBytes());
            } else if ("+OK".equals(content) || "OK".equals(content)) {
                //基础参数设置回复
                SwitchNettyTools.setReceiveMsg(channel.id().asLongText() + "-" + "SET", content.getBytes());
            } else if (content.contains("OK")){
                //基础参数读取指令回复
                SwitchNettyTools.setReceiveMsg(channel.id().asLongText() + "-" + "READ", content.getBytes());
            } else {
                String byteString = ByteUtil.toHexString(bytes);
                //根据功能码判断指令功能
                EquipInfo equipInfo = getGatewayCodeByChannelId(channel.id().asLongText());
                if (!Optional.ofNullable(equipInfo).isPresent()) {
                    return;
                }
                log.error("{}===接收指令为：{}", equipInfo.getEquipSn(), byteString);

                ctx.channel().eventLoop().execute(() -> {
                    if ("4D54".equals(byteString.substring(0, 4))) {
                        //截取功能码
                        String functionCode = byteString.substring(16, 18);
                        if ("D2".equals(functionCode)) {
                            //设备请求固件下载链接
                            SwitchNettyTools.setReceiveMsg(channel.id().asLongText() + "-" + functionCode, bytes);
                        }
                        if ("D4".equals(functionCode)) {
                            //更新固件状态通知(进度为FF即失败，100为成功，大于100则为失败)
                            int progress = Integer.parseInt(byteString.substring(20, 22), 16);
                            String validOrder = null;
                            int isSuccess = 1;
                            if ("FF".equals(byteString.substring(20, 22))) {
                                isSuccess = 0;
                                equipWorkSet.remove(equipInfo.getEquipMac());
                            }
                            if (progress >= 0 && progress <= 99) {
                                validOrder = String.valueOf(progress);
                            } else if (progress == 100) {
                                validOrder = String.valueOf(progress);
                                isSuccess = 2;
                                equipWorkSet.remove(equipInfo.getEquipMac());
                            } else {
                                isSuccess = 0;
                                equipWorkSet.remove(equipInfo.getEquipMac());
                            }
                            if (Optional.ofNullable(equipInfo.getFirmwareChannelId()).isPresent()) {
                                String jsonStr = JSONUtil.parse(SocketResult.getSocketResult(1, byteString, validOrder, 2, isSuccess)).toString();
                                //像websocket发送升级情况
                                SendUtils.sendWebSocketClient(jsonStr, equipInfo.getFirmwareChannelId());
                            }
                        }
                    }
                    if ("534554".equals(byteString.substring(0, 6))) {
                        //截取功能码
                        String functionCode = byteString.substring(6, 8);
                        if ("17".equals(functionCode) || "14".equals(functionCode) || "19".equals(functionCode) || "05".equals(functionCode) || "04".equals(functionCode)) {
                            //modbus基础指令、modbusRtu指令、自定义指令、决策使能---------（读取指令回复）
                            SwitchNettyTools.setReceiveMsg(channel.id().asLongText() + "-" + functionCode, bytes);
                        }
                        if ("18".equals(functionCode) || "11".equals(functionCode) || "1A".equals(functionCode) || "06".equals(functionCode) || "01".equals(functionCode)) {
                            //modbus基础指令、modbusRtu指令、自定义指令、决策使能、本地逻辑---------（下发指令回复）
                            if (Optional.ofNullable(equipInfo.getFirmwareChannelId()).isPresent()) {
                                String jsonStr = JSONUtil.parse(SocketResult.getSocketResult(1, byteString, null, 0, 1)).toString();
                                //像websocket发送升级情况
                                SendUtils.sendWebSocketClient(jsonStr, equipInfo.getFirmwareChannelId());
                            }
                            String successFlag = byteString.substring(8, 10);
                            SwitchNettyTools.setReceiveMsg(channel.id().asLongText() + "-" + functionCode, successFlag.getBytes());
                        }
                    }
                });
            }
        }
    }

    /**
     * 表示channel断开连接
     *
     * @param ctx 上下文对象
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        disconnect(ctx);
    }

    /**
     * 长时间没接收消息，自动断开
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        Channel channel = ctx.channel();
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                log.warn("{},长时间没接收数据，自动与服务端断开连接", channel.id().asLongText());
                disconnect(ctx);
                ctx.disconnect();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println(cause.getMessage());
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
        EquipInfo equipInfo = getGatewayCodeByChannelId(channel.id().asLongText());
        if (equipInfo != null) {
            equipInfo.setIsOnline("1");
            equipInfo.setChannelId(null);
            tcpServerHandler.equipInfoMapper.updateByPrimaryKeySelective(equipInfo);
            equipWorkSet.remove(equipInfo.getEquipMac());
        }
        channelMap.remove(channel.id().asLongText());
        channelGroup.remove(channel);
        ctx.disconnect();
    }

    /**
     * 根据网关通道id查询网关编号
     *
     * @param channelId 网关通道id
     * @return
     */
    public EquipInfo getGatewayCodeByChannelId(String channelId) {
        EquipInfo equipInfoObj = new EquipInfo();
        equipInfoObj.setChannelId(channelId);
        return tcpServerHandler.equipInfoMapper.selectOne(equipInfoObj);
    }

    /**
     * 获取数据，解决粘包问题
     *
     * @param msg
     * @return
     */
    public Command getCommand(ByteBuf msg) {
        Command command = new Command();
        try {
            byte[] flag = new byte[BEGIN_FLAG_LENGTH];
            msg.readBytes(flag);
            byte[] dataLength = new byte[DATA_FLAG_LENGTH];
            msg.readBytes(dataLength);
            int length = ByteUtil.byte2Int(dataLength);
            byte[] tempBytes = new byte[length + END_FLAG_LENGTH];
            System.out.println(tempBytes.length + "--------");
            msg.readBytes(tempBytes);
            SubCommand subCommand = new SubCommand();
            subCommand.setLength(length);
            subCommand.setData(tempBytes);
            //封装一个完整的消息
            command.setFlag(flag);
            command.setLength(length);
            command.setSubCommand(subCommand);
            byte[] cmdByte = new byte[BEGIN_FLAG_LENGTH + DATA_FLAG_LENGTH + length + END_FLAG_LENGTH];
            System.arraycopy(flag, 0, cmdByte, 0, flag.length);
            System.arraycopy(dataLength, 0, cmdByte, flag.length, dataLength.length);
            System.arraycopy(tempBytes, 0, cmdByte, flag.length + dataLength.length, tempBytes.length);
            command.setCmd(cmdByte);
            System.out.println(command);
        } catch (Exception e) {
            throw new MticException("CMD_ERROR", ErrorInfo.CMD_ERROR.getCode());
        }
        return command;
    }
}
