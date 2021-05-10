package com.mantoo.mtic.module.system.service.equip.impl;

import cn.hutool.json.JSONUtil;
import com.mantoo.mtic.common.entity.RestResult;
import com.mantoo.mtic.common.entity.ResultUtils;
import com.mantoo.mtic.common.entity.SocketResult;
import com.mantoo.mtic.common.utils.ByteUtil;
import com.mantoo.mtic.common.utils.CrcModbus;
import com.mantoo.mtic.common.utils.SendUtils;
import com.mantoo.mtic.module.netty.tcp.TcpServerHandler;
import com.mantoo.mtic.module.system.entity.equip.EquipInfo;
import com.mantoo.mtic.module.system.entity.equipconfig.*;
import com.mantoo.mtic.module.system.exMapper.equip.EquipInfoMapper;
import com.mantoo.mtic.module.system.service.equip.IEquipConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @ClassName: EquipConfigServiceImpl
 * @Description: 远程配置
 * @Author: xjy
 * @Date: 2021-04-23 09:53
 */
@Slf4j
@Service
public class EquipConfigServiceImpl implements IEquipConfigService {

    @Resource
    private EquipInfoMapper equipInfoMapper;

    /**
     * @return com.mantoo.mtic.common.entity.RestResult<java.lang.String>
     * @Description 重启设备
     * @Param [equipInfo]
     * @Author xjy
     * @Date 2021/4/23 9:56
     */
    @Override
    public RestResult<String> equipRestart(EquipInfo equipInfo) {
        EquipInfo equipInfoObj = equipInfoMapper.selectByPrimaryKey(equipInfo.getEquipId());
        //判断设备是否在线
        if (!"0".equals(equipInfoObj.getIsOnline())) {
            return ResultUtils.genErrorResult("设备未连接");
        }
        if (TcpServerHandler.equipWorkSet.contains(equipInfoObj.getEquipMac())) {
            return ResultUtils.genSuccesResult("当前设备已占用，请稍后再试");
        } else {
            TcpServerHandler.equipWorkSet.add(equipInfoObj.getEquipMac());
        }
        if (Optional.ofNullable(equipInfoObj.getChannelId()).isPresent()) {
            System.out.println("设备重启指令下发操作");
            String cmd = "#MT#AT+Z\r\n";
            if (Optional.ofNullable(equipInfoObj.getFirmwareChannelId()).isPresent()) {
                String jsonStr = JSONUtil.parse(SocketResult.getSocketResult(0, cmd, null, 0,1)).toString();
                SendUtils.sendWebSocketClient(jsonStr, equipInfoObj.getFirmwareChannelId());
            }
            String messageStorage = SendUtils.toStringSendClient(cmd, equipInfoObj.getChannelId(), "SET");
            if (Optional.ofNullable(messageStorage).isPresent()) {
                if (Optional.ofNullable(equipInfoObj.getFirmwareChannelId()).isPresent()) {
                    String jsonStr = JSONUtil.parse(SocketResult.getSocketResult(1, messageStorage, null, 0, 1)).toString();
                    SendUtils.sendWebSocketClient(jsonStr, equipInfoObj.getFirmwareChannelId());
                }
            } else {
                TcpServerHandler.equipWorkSet.remove(equipInfoObj.getEquipMac());
                return ResultUtils.genErrorResult("设备未回复");
            }
        } else {
            TcpServerHandler.equipWorkSet.remove(equipInfoObj.getEquipMac());
            return ResultUtils.genErrorResult("没有获取到网关对应的通道");
        }
        TcpServerHandler.equipWorkSet.remove(equipInfoObj.getEquipMac());
        return ResultUtils.genSuccesResult();
    }

    /**
     * @return com.mantoo.mtic.common.entity.RestResult<java.lang.String>
     * @Description 基础指令读取
     * @Param [basicReadList, equipId]
     * @Author xjy
     * @Date 2021/4/23 11:35
     */
    @Override
    public RestResult<String> basicRead(List<String> basicReadList, Long equipId) {
        EquipInfo equipInfoObj = equipInfoMapper.selectByPrimaryKey(equipId);
        //判断设备是否在线
        if (!"0".equals(equipInfoObj.getIsOnline())) {
            return ResultUtils.genErrorResult("设备未连接");
        }
        if (TcpServerHandler.equipWorkSet.contains(equipInfoObj.getEquipMac())) {
            return ResultUtils.genSuccesResult("当前设备已占用，请稍后再试");
        } else {
            TcpServerHandler.equipWorkSet.add(equipInfoObj.getEquipMac());
        }
        if (Optional.ofNullable(equipInfoObj.getChannelId()).isPresent()) {
            boolean errorFlag = false;
            int count = 0;
            for (String cmd : basicReadList) {
                count++;
                if (Optional.ofNullable(equipInfoObj.getFirmwareChannelId()).isPresent()) {
                    String jsonStr = JSONUtil.parse(SocketResult.getSocketResult(0, cmd, null,0, 1)).toString();
                    SendUtils.sendWebSocketClient(jsonStr, equipInfoObj.getFirmwareChannelId());
                }
                String messageStorage = SendUtils.toStringSendClient(cmd+"\r\n", equipInfoObj.getChannelId(), "READ");
                if (Optional.ofNullable(messageStorage).isPresent()) {
                    String validOrder = messageStorage.replaceAll("OK","").replaceAll("\\+","").replaceAll("\r\n","").trim();
                    if (Optional.ofNullable(equipInfoObj.getFirmwareChannelId()).isPresent()) {
                        String jsonStr = JSONUtil.parse(SocketResult.getSocketResult(1, messageStorage, validOrder, 1,1)).toString();
                        if (count == basicReadList.size()) {
                            int isSuccess = 2;
                            if (errorFlag) {
                                isSuccess = 0;
                            }
                            jsonStr = JSONUtil.parse(SocketResult.getSocketResult(1, messageStorage, validOrder,1, isSuccess)).toString();
                        }
                        SendUtils.sendWebSocketClient(jsonStr, equipInfoObj.getFirmwareChannelId());
                    }
                } else {
                    errorFlag = true;
                }
            }
        } else {
            TcpServerHandler.equipWorkSet.remove(equipInfoObj.getEquipMac());
            return ResultUtils.genErrorResult("没有获取到网关对应的通道");
        }
        TcpServerHandler.equipWorkSet.remove(equipInfoObj.getEquipMac());
        return ResultUtils.genSuccesResult();
    }

    /**
     * @return com.mantoo.mtic.common.entity.RestResult<java.lang.String>
     * @Description 基础参数指令下发
     * @Param [basicSetList, equipId]
     * @Author xjy
     * @Date 2021/4/23 14:28
     */
    @Override
    public RestResult<String> basicSet(List<String> basicSetList, Long equipId) {
        EquipInfo equipInfoObj = equipInfoMapper.selectByPrimaryKey(equipId);
        //判断设备是否在线
        if (!"0".equals(equipInfoObj.getIsOnline())) {
            return ResultUtils.genErrorResult("设备未连接");
        }
        if (TcpServerHandler.equipWorkSet.contains(equipInfoObj.getEquipMac())) {
            return ResultUtils.genSuccesResult("当前设备已占用，请稍后再试");
        } else {
            TcpServerHandler.equipWorkSet.add(equipInfoObj.getEquipMac());
        }
        if (Optional.ofNullable(equipInfoObj.getChannelId()).isPresent()) {
            boolean errorFlag = false;
            int count = 0;
            for (String cmd : basicSetList) {
                count++;
                if (Optional.ofNullable(equipInfoObj.getFirmwareChannelId()).isPresent()) {
                    String jsonStr = JSONUtil.parse(SocketResult.getSocketResult(0, cmd, null, 0,1)).toString();
                    SendUtils.sendWebSocketClient(jsonStr, equipInfoObj.getFirmwareChannelId());
                }
                String messageStorage = SendUtils.toStringSendClient(cmd+"\r\n", equipInfoObj.getChannelId(), "SET");
                if (Optional.ofNullable(messageStorage).isPresent()) {
                    if (Optional.ofNullable(equipInfoObj.getFirmwareChannelId()).isPresent()) {
                        String jsonStr = JSONUtil.parse(SocketResult.getSocketResult(1, messageStorage, null, 0,1)).toString();
                        if (count == basicSetList.size()) {
                            int isSuccess = 2;
                            if (errorFlag) {
                                isSuccess = 0;
                            }
                            jsonStr = JSONUtil.parse(SocketResult.getSocketResult(1, messageStorage, null,0, isSuccess)).toString();
                        }
                        SendUtils.sendWebSocketClient(jsonStr, equipInfoObj.getFirmwareChannelId());
                    }
                } else {
                    errorFlag = true;
                }
            }
        } else {
            TcpServerHandler.equipWorkSet.remove(equipInfoObj.getEquipMac());
            return ResultUtils.genErrorResult("没有获取到网关对应的通道");
        }
        TcpServerHandler.equipWorkSet.remove(equipInfoObj.getEquipMac());
        return ResultUtils.genSuccesResult();
    }

    /**
     * @return com.mantoo.mtic.common.entity.RestResult<java.lang.String>
     * @Description modbus指令读取
     * @Param [equipId]
     * @Author xjy
     * @Date 2021/4/23 15:10
     */
    @Override
    public RestResult<String> modbusRead(Long equipId) {
        EquipInfo equipInfoObj = equipInfoMapper.selectByPrimaryKey(equipId);
        //判断设备是否在线
        if (!"0".equals(equipInfoObj.getIsOnline())) {
            return ResultUtils.genErrorResult("设备未连接");
        }
        if (TcpServerHandler.equipWorkSet.contains(equipInfoObj.getEquipMac())) {
            return ResultUtils.genSuccesResult("当前设备已占用，请稍后再试");
        } else {
            TcpServerHandler.equipWorkSet.add(equipInfoObj.getEquipMac());
        }
        if (Optional.ofNullable(equipInfoObj.getChannelId()).isPresent()) {
            //modbus基础指令下发（轮询使能+协议类型+轮询周期+使能主动上传网络服务器+上报周期）
            String cmd = "534554177E7B";
            if (Optional.ofNullable(equipInfoObj.getFirmwareChannelId()).isPresent()) {
                String jsonStr = JSONUtil.parse(SocketResult.getSocketResult(0, cmd, null, 0,1)).toString();
                SendUtils.sendWebSocketClient(jsonStr, equipInfoObj.getFirmwareChannelId());
            }
            String messageStorage = SendUtils.sendClient(cmd, equipInfoObj.getChannelId(), "17");
            if (Optional.ofNullable(messageStorage).isPresent()) {
                String[] messageArr = ByteUtil.hexStringTOArr(messageStorage);
                ModbusBasic modbusBasic = new ModbusBasic();
                modbusBasic.setModpolln(messageArr[4]);
                modbusBasic.setAgreeType(messageArr[5]);
                modbusBasic.setModton(Integer.valueOf(messageArr[6]+messageArr[7],16));
                modbusBasic.setModpollupn(messageArr[8]);
                modbusBasic.setUploadton(Integer.valueOf(messageArr[10]+messageArr[11],16));
                if (Optional.ofNullable(equipInfoObj.getFirmwareChannelId()).isPresent()) {
                    String jsonStr = JSONUtil.parse(SocketResult.getSocketResult(1, messageStorage, JSONUtil.parse(modbusBasic),3, 1)).toString();
                    SendUtils.sendWebSocketClient(jsonStr, equipInfoObj.getFirmwareChannelId());
                }
                //modbusRtu指令下发
                cmd = "534554143E7A";
                if (Optional.ofNullable(equipInfoObj.getFirmwareChannelId()).isPresent()) {
                    String jsonStr = JSONUtil.parse(SocketResult.getSocketResult(0, cmd, null, 0,1)).toString();
                    SendUtils.sendWebSocketClient(jsonStr, equipInfoObj.getFirmwareChannelId());
                }
                messageStorage = SendUtils.sendClient(cmd, equipInfoObj.getChannelId(), "14");
                if (Optional.ofNullable(messageStorage).isPresent()) {
                    messageArr = ByteUtil.hexStringTOArr(messageStorage);
                    int num = Integer.valueOf(messageArr[4],16);//协议数量
                    if (num > 30) {
                        if (Optional.ofNullable(equipInfoObj.getFirmwareChannelId()).isPresent()) {
                            String jsonStr = JSONUtil.parse(SocketResult.getSocketResult(1, messageStorage, null, 0,1)).toString();
                            SendUtils.sendWebSocketClient(jsonStr, equipInfoObj.getFirmwareChannelId());
                        }
                        TcpServerHandler.equipWorkSet.remove(equipInfoObj.getEquipMac());
                        return ResultUtils.genErrorResult("轮询协议数量超过30个");
                    }
                    List<ModbusRtu> modbusRtuList = new ArrayList<>();
                    for(int i=0; i < messageArr.length; i=i+25){
                        ModbusRtu modbusRtu = new ModbusRtu();
                        modbusRtu.setNumber(Integer.valueOf(messageArr[i+5],16));//协议编号
                        modbusRtu.setSubAddr(Integer.valueOf(messageArr[i+6],16));//从机地址
                        modbusRtu.setDataAddr(Integer.valueOf(messageArr[i+7]+messageArr[i+8]+messageArr[i+9]+messageArr[i+10],16));//数据地址
                        modbusRtu.setDataType(messageArr[i+11]);//数据类型
                        modbusRtu.setFactor(messageArr[i+12]);//计算因素
                        //标识符
                        String remind = ByteUtil.hexStringToString(messageArr[i+13]+messageArr[i+14]+messageArr[i+15]+messageArr[i+16]+messageArr[i+17]+messageArr[i+18]+messageArr[i+19]+messageArr[i+20]+messageArr[i+21]+messageArr[i+22]).trim();
                        modbusRtu.setRemind(remind);
                        modbusRtuList.add(modbusRtu);
                    }
                    if (Optional.ofNullable(equipInfoObj.getFirmwareChannelId()).isPresent()) {
                        String jsonStr = JSONUtil.parse(SocketResult.getSocketResult(1, messageStorage, JSONUtil.parse(modbusRtuList),4, 1)).toString();
                        SendUtils.sendWebSocketClient(jsonStr, equipInfoObj.getFirmwareChannelId());
                    }
                    //自定义指令下发
                    cmd = "53455419FFBF";
                    if (Optional.ofNullable(equipInfoObj.getFirmwareChannelId()).isPresent()) {
                        String jsonStr = JSONUtil.parse(SocketResult.getSocketResult(0, cmd, null, 0,1)).toString();
                        SendUtils.sendWebSocketClient(jsonStr, equipInfoObj.getFirmwareChannelId());
                    }
                    messageStorage = SendUtils.sendClient(cmd, equipInfoObj.getChannelId(), "19");
                    if (Optional.ofNullable(messageStorage).isPresent()) {
                        messageArr = ByteUtil.hexStringTOArr(messageStorage);
                        num = Integer.valueOf(messageArr[4],16);//协议数量
                        if (num > 30) {
                            if (Optional.ofNullable(equipInfoObj.getFirmwareChannelId()).isPresent()) {
                                String jsonStr = JSONUtil.parse(SocketResult.getSocketResult(1, messageStorage, null, 0,1)).toString();
                                SendUtils.sendWebSocketClient(jsonStr, equipInfoObj.getFirmwareChannelId());
                            }
                            TcpServerHandler.equipWorkSet.remove(equipInfoObj.getEquipMac());
                            return ResultUtils.genErrorResult("自定义轮询协议数量超过30个");
                        }
                        List<ModbusDefault> defList = new ArrayList<>();
                        int n = 0;
                        for (int i=0; i < messageArr.length; i=i+9+n) {
                            ModbusDefault modbusDefault = new ModbusDefault();
                            modbusDefault.setNumber(Integer.valueOf(messageArr[i+5],16));//协议编号
                            n = Integer.valueOf(messageArr[i+6],16);//指令长度
                            String pollOrder = "";
                            for(int j = 0; j < n; j++){
                                pollOrder = pollOrder + messageArr[i+7+j];
                            }
                            modbusDefault.setOrder(pollOrder);//自定义指令
                            defList.add(modbusDefault);
                        }
                        if (Optional.ofNullable(equipInfoObj.getFirmwareChannelId()).isPresent()) {
                            String jsonStr = JSONUtil.parse(SocketResult.getSocketResult(1, messageStorage, JSONUtil.parse(defList),5, 2)).toString();
                            SendUtils.sendWebSocketClient(jsonStr, equipInfoObj.getFirmwareChannelId());
                        }
                    } else {
                        TcpServerHandler.equipWorkSet.remove(equipInfoObj.getEquipMac());
                        return ResultUtils.genErrorResult("设备未回复");
                    }
                } else {
                    TcpServerHandler.equipWorkSet.remove(equipInfoObj.getEquipMac());
                    return ResultUtils.genErrorResult("设备未回复");
                }
            } else {
                TcpServerHandler.equipWorkSet.remove(equipInfoObj.getEquipMac());
                return ResultUtils.genErrorResult("设备未回复");
            }
        } else {
            TcpServerHandler.equipWorkSet.remove(equipInfoObj.getEquipMac());
            return ResultUtils.genErrorResult("没有获取到网关对应的通道");
        }
        TcpServerHandler.equipWorkSet.remove(equipInfoObj.getEquipMac());
        return ResultUtils.genSuccesResult();
    }

    /**
     * @return com.mantoo.mtic.common.entity.RestResult<java.lang.String>
     * @Description modbus指令下发
     * @Param [basicData, equipId, modbusRtuList, defaultList]
     * @Author xjy
     * @Date 2021/4/25 14:19
     */
    @Override
    public RestResult<String> modbusSet(ModbusBasic basicData) {
        EquipInfo equipInfoObj = equipInfoMapper.selectByPrimaryKey(basicData.getEquipId());
        //判断设备是否在线
        if (!"0".equals(equipInfoObj.getIsOnline())) {
            return ResultUtils.genErrorResult("设备未连接");
        }
        if (TcpServerHandler.equipWorkSet.contains(equipInfoObj.getEquipMac())) {
            return ResultUtils.genSuccesResult("当前设备已占用，请稍后再试");
        } else {
            TcpServerHandler.equipWorkSet.add(equipInfoObj.getEquipMac());
        }
        if (Optional.ofNullable(equipInfoObj.getChannelId()).isPresent()) {
            //modbus基础指令下发（轮询使能+协议类型+轮询周期+使能主动上传网络服务器+上报周期）
            String cmd = "53455418";
            cmd = cmd + basicData.getModpolln() + basicData.getAgreeType();//轮询使能+协议类型
            // 轮询周期
            String modton = ByteUtil.intToHexString(basicData.getModton());
            if(modton.length() < 4){
                for(int i = 0; i < 4 - modton.length(); i++){
                    cmd = cmd + "0";
                }
            }
            cmd = cmd + modton;
            cmd = cmd + basicData.getModpollupn() + "00";//使能主动上传网络服务器+上报类型（已去除，随意赋值即可）
            //上报周期
            String uploadton = ByteUtil.intToHexString(basicData.getUploadton());
            if(uploadton.length() < 4){
                for(int i = 0; i < 4 - uploadton.length(); i++){
                    cmd = cmd + "0";
                }
            }
            cmd = cmd + uploadton;
            String crc = CrcModbus.DoCRCModBus(cmd);
            String hexstr = (cmd + crc).replaceAll(" ", "");
            if (Optional.ofNullable(equipInfoObj.getFirmwareChannelId()).isPresent()) {
                String jsonStr = JSONUtil.parse(SocketResult.getSocketResult(0, hexstr, null, 0,1)).toString();
                SendUtils.sendWebSocketClient(jsonStr, equipInfoObj.getFirmwareChannelId());
            }
            String messageStorage = SendUtils.sendClient(hexstr, equipInfoObj.getChannelId(), "18");
            if (Optional.ofNullable(messageStorage).isPresent()) {
                if (!"01".equals(messageStorage)) {
                    TcpServerHandler.equipWorkSet.remove(equipInfoObj.getEquipMac());
                    return ResultUtils.genErrorResult("轮询参数指令应答失败");
                }
            } else {
                TcpServerHandler.equipWorkSet.remove(equipInfoObj.getEquipMac());
                return ResultUtils.genErrorResult("设备未回复");
            }
            //modbusRtu指令
            if (basicData.getModbusRtuList().size() > 0) {
                //modbusRtu指令逐条下发
                for (ModbusRtu modbusRtu: basicData.getModbusRtuList()) {
                    cmd = "53455411" + ByteUtil.intToHexString(modbusRtu.getNumber());//协议头+功能码+编号
                    cmd = cmd + ByteUtil.intToHexString(modbusRtu.getSubAddr());//从机地址
                    //数据地址
                    String dataAddr = ByteUtil.intToHexString(modbusRtu.getDataAddr());
                    if(dataAddr.length() < 8){
                        for(int i = 0; i < 8 - dataAddr.length(); i++){
                            cmd = cmd + "0";
                        }
                    }
                    cmd = cmd + dataAddr;
                    cmd = cmd + modbusRtu.getDataType();//数据类型
                    cmd = cmd + modbusRtu.getFactor();//计算因素
                    //标识符
                    String remind = ByteUtil.stringToHexString(modbusRtu.getRemind());
                    if(remind.length() < 20){
                        for(int i = 0; i < 20 - remind.length(); i++){
                            cmd = cmd + "0";
                        }
                    }
                    cmd = cmd + remind;
                    crc = CrcModbus.DoCRCModBus(cmd);
                    hexstr = (cmd + crc).replaceAll(" ", "");
                    if (Optional.ofNullable(equipInfoObj.getFirmwareChannelId()).isPresent()) {
                        String jsonStr = JSONUtil.parse(SocketResult.getSocketResult(0, hexstr, null, 0,1)).toString();
                        SendUtils.sendWebSocketClient(jsonStr, equipInfoObj.getFirmwareChannelId());
                    }
                    messageStorage = SendUtils.sendClient(hexstr, equipInfoObj.getChannelId(), "11");
                    if (Optional.ofNullable(messageStorage).isPresent()) {
                        if (!"01".equals(messageStorage)) {
                            TcpServerHandler.equipWorkSet.remove(equipInfoObj.getEquipMac());
                            return ResultUtils.genErrorResult("编号" + modbusRtu.getNumber() + ":失败应答！");
                        }
                    }else {
                        TcpServerHandler.equipWorkSet.remove(equipInfoObj.getEquipMac());
                        return ResultUtils.genErrorResult("设备未回复");
                    }
                }
            }
            //自定义指令
            if (basicData.getDefaultList().size() > 0) {
                //自定义指令逐条下发
                for (ModbusDefault modbusDefault : basicData.getDefaultList()) {
                    cmd = "5345541A" + ByteUtil.intToHexString(modbusDefault.getNumber());//协议头+功能码+编号
                    String order = modbusDefault.getOrder();
                    int hex = modbusDefault.getHex();
                    if (hex == 0) {
                        order = ByteUtil.stringToHexString(order);
                    }
                    int len = order.length();
                    if (len % 2 != 0) {
                        TcpServerHandler.equipWorkSet.remove(equipInfoObj.getEquipMac());
                        return ResultUtils.genErrorResult("编号" + modbusDefault.getNumber() + "指令长度有误！");
                    }
                    if (len > 100) {
                        TcpServerHandler.equipWorkSet.remove(equipInfoObj.getEquipMac());
                        return ResultUtils.genErrorResult("编号" + modbusDefault.getNumber() + "指令过长！");
                    }
                    cmd = cmd + ByteUtil.intToHexString(len/2) + order;//指令长度 + 自定义指令
                    crc = CrcModbus.DoCRCModBus(cmd);
                    hexstr = (cmd + crc).replaceAll(" ", "");
                    if (Optional.ofNullable(equipInfoObj.getFirmwareChannelId()).isPresent()) {
                        String jsonStr = JSONUtil.parse(SocketResult.getSocketResult(0, hexstr, null, 0,1)).toString();
                        SendUtils.sendWebSocketClient(jsonStr, equipInfoObj.getFirmwareChannelId());
                    }
                    messageStorage = SendUtils.sendClient(hexstr, equipInfoObj.getChannelId(), "1A");
                    if (Optional.ofNullable(messageStorage).isPresent()) {
                        if (!"01".equals(messageStorage)) {
                            TcpServerHandler.equipWorkSet.remove(equipInfoObj.getEquipMac());
                            return ResultUtils.genErrorResult("编号" + modbusDefault.getNumber() + ":失败应答！");
                        }
                    }else {
                        TcpServerHandler.equipWorkSet.remove(equipInfoObj.getEquipMac());
                        return ResultUtils.genErrorResult("设备未回复");
                    }
                }
            }
        } else {
            TcpServerHandler.equipWorkSet.remove(equipInfoObj.getEquipMac());
            return ResultUtils.genErrorResult("没有获取到网关对应的通道");
        }
        TcpServerHandler.equipWorkSet.remove(equipInfoObj.getEquipMac());
        return ResultUtils.genSuccesResult();
    }

    /**
     * @return com.mantoo.mtic.common.entity.RestResult<java.lang.String>
     * @Description 本地逻辑指令读取
     * @Param [equipId]
     * @Author xjy
     * @Date 2021/4/26 10:22
     */
    @Override
    public RestResult<String> logicRead(Long equipId) {
        EquipInfo equipInfoObj = equipInfoMapper.selectByPrimaryKey(equipId);
        //判断设备是否在线
        if (!"0".equals(equipInfoObj.getIsOnline())) {
            return ResultUtils.genErrorResult("设备未连接");
        }
        if (TcpServerHandler.equipWorkSet.contains(equipInfoObj.getEquipMac())) {
            return ResultUtils.genSuccesResult("当前设备已占用，请稍后再试");
        } else {
            TcpServerHandler.equipWorkSet.add(equipInfoObj.getEquipMac());
        }
        if (Optional.ofNullable(equipInfoObj.getChannelId()).isPresent()) {
            //决策使能
            String cmd = "53455405FE76";
            if (Optional.ofNullable(equipInfoObj.getFirmwareChannelId()).isPresent()) {
                String jsonStr = JSONUtil.parse(SocketResult.getSocketResult(0, cmd, null, 0,1)).toString();
                SendUtils.sendWebSocketClient(jsonStr, equipInfoObj.getFirmwareChannelId());
            }
            String messageStorage = SendUtils.sendClient(cmd, equipInfoObj.getChannelId(), "05");
            if (Optional.ofNullable(messageStorage).isPresent()) {
                String logicEnable = messageStorage.substring(8, 10);
                LogicBasic logicBasic = new LogicBasic();
                logicBasic.setLogicEnable(logicEnable);
                if (Optional.ofNullable(equipInfoObj.getFirmwareChannelId()).isPresent()) {
                    String jsonStr = JSONUtil.parse(SocketResult.getSocketResult(1, messageStorage, JSONUtil.parse(logicBasic),6, 1)).toString();
                    SendUtils.sendWebSocketClient(jsonStr, equipInfoObj.getFirmwareChannelId());
                }
                //本地逻辑指令下发
                cmd = "534554043FB6";
                if (Optional.ofNullable(equipInfoObj.getFirmwareChannelId()).isPresent()) {
                    String jsonStr = JSONUtil.parse(SocketResult.getSocketResult(0, cmd, null, 0,1)).toString();
                    SendUtils.sendWebSocketClient(jsonStr, equipInfoObj.getFirmwareChannelId());
                }
                messageStorage = SendUtils.sendClient(cmd, equipInfoObj.getChannelId(), "04");
                if (Optional.ofNullable(messageStorage).isPresent()) {
                    String[] messageArr = ByteUtil.hexStringTOArr(messageStorage);
                    int num = Integer.valueOf(messageArr[4], 16);//协议数量
                    if (num > 10) {
                        if (Optional.ofNullable(equipInfoObj.getFirmwareChannelId()).isPresent()) {
                            String jsonStr = JSONUtil.parse(SocketResult.getSocketResult(1, messageStorage, null, 0, 1)).toString();
                            SendUtils.sendWebSocketClient(jsonStr, equipInfoObj.getFirmwareChannelId());
                        }
                        TcpServerHandler.equipWorkSet.remove(equipInfoObj.getEquipMac());
                        return ResultUtils.genErrorResult("协议数量超过10个");
                    }
                    List<LocalLogic> logicList = new ArrayList<>();
                    for(int i=0; i < messageArr.length; i=i+17) {
                        LocalLogic localLogic = new LocalLogic();
                        localLogic.setNumber(Integer.valueOf(messageArr[i+5],16));//协议编号
                        localLogic.setParameterType(messageArr[i+6]);//参数类型
                        localLogic.setParameterAddr(Integer.valueOf(messageArr[i+7],16));//参数地址
                        localLogic.setJudgeLogic(messageArr[i+8]);//判断逻辑
                        localLogic.setJudgeConditions(messageArr[i+9]);//判断条件
                        //数值
                        Long value = Long.valueOf(messageArr[i + 10] + messageArr[i + 11] + messageArr[i + 12] + messageArr[i + 13]);
                        localLogic.setValue(String.valueOf(value/100.0));
                        if ("04".equals(messageArr[i+9])) {
                            localLogic.setValue("-"+String.valueOf(value/100.0));
                        }
                        localLogic.setLogicOutput(messageArr[i+14]);//逻辑输出
                        logicList.add(localLogic);
                    }
                    if (Optional.ofNullable(equipInfoObj.getFirmwareChannelId()).isPresent()) {
                        String jsonStr = JSONUtil.parse(SocketResult.getSocketResult(1, messageStorage, JSONUtil.parse(logicList),7, 1)).toString();
                        SendUtils.sendWebSocketClient(jsonStr, equipInfoObj.getFirmwareChannelId());
                    }
                } else {
                    TcpServerHandler.equipWorkSet.remove(equipInfoObj.getEquipMac());
                    return ResultUtils.genErrorResult("设备未回复");
                }
            } else {
                TcpServerHandler.equipWorkSet.remove(equipInfoObj.getEquipMac());
                return ResultUtils.genErrorResult("设备未回复");
            }
        } else {
            TcpServerHandler.equipWorkSet.remove(equipInfoObj.getEquipMac());
            return ResultUtils.genErrorResult("没有获取到网关对应的通道");
        }
        TcpServerHandler.equipWorkSet.remove(equipInfoObj.getEquipMac());
        return ResultUtils.genSuccesResult();
    }

    /**
     * @return com.mantoo.mtic.common.entity.RestResult<java.lang.String>
     * @Description 本地逻辑指令下发
     * @Param [equipId, logicEnable, logicList]
     * @Author xjy
     * @Date 2021/4/26 13:29
     */
    @Override
    public RestResult<String> logicSet(LogicBasic logicData) {
        EquipInfo equipInfoObj = equipInfoMapper.selectByPrimaryKey(logicData.getEquipId());
        //判断设备是否在线
        if (!"0".equals(equipInfoObj.getIsOnline())) {
            return ResultUtils.genErrorResult("设备未连接");
        }
        if (TcpServerHandler.equipWorkSet.contains(equipInfoObj.getEquipMac())) {
            return ResultUtils.genSuccesResult("当前设备已占用，请稍后再试");
        } else {
            TcpServerHandler.equipWorkSet.add(equipInfoObj.getEquipMac());
        }
        if (Optional.ofNullable(equipInfoObj.getChannelId()).isPresent()) {
            //决策使能
            String cmd = "53455406" + logicData.getLogicEnable();
            String crc = CrcModbus.DoCRCModBus(cmd);
            String hexstr = (cmd + crc).replaceAll(" ", "");
            if (Optional.ofNullable(equipInfoObj.getFirmwareChannelId()).isPresent()) {
                String jsonStr = JSONUtil.parse(SocketResult.getSocketResult(0, hexstr, null, 0,1)).toString();
                SendUtils.sendWebSocketClient(jsonStr, equipInfoObj.getFirmwareChannelId());
            }
            String messageStorage = SendUtils.sendClient(hexstr, equipInfoObj.getChannelId(), "06");
            if (Optional.ofNullable(messageStorage).isPresent()) {
                if (!"01".equals(messageStorage)) {
                    TcpServerHandler.equipWorkSet.remove(equipInfoObj.getEquipMac());
                    return ResultUtils.genErrorResult("决策使能指令应答失败");
                }
            } else {
                TcpServerHandler.equipWorkSet.remove(equipInfoObj.getEquipMac());
                return ResultUtils.genErrorResult("设备未回复");
            }
            //本地逻辑
            if (logicData.getLocalLogicList().size() > 0) {
                //本地逻辑指令逐条下发
                for (LocalLogic logic : logicData.getLocalLogicList()) {
                    cmd = "53455401" + ByteUtil.intToHexString(logic.getNumber());//协议头+功能码+编号
                    cmd = cmd + logic.getParameterType();//参数类型
                    cmd = cmd + ByteUtil.intToHexString(logic.getParameterAddr());//参数地址
                    cmd = cmd + logic.getJudgeLogic();//判断逻辑
                    //判断条件
                    String judgeConditions = logic.getJudgeConditions();
                    cmd = cmd + judgeConditions;
                    //数值
                    String valueStr = logic.getValue();
                    if ("04".equals(judgeConditions)) {
                        valueStr = valueStr.substring(1);
                    }
                    String value = ByteUtil.longToHexString((long) (Float.valueOf(valueStr) * 100));
                    for(int i=0; i < 8-value.length(); i++){
                        cmd = cmd + "0";
                    }
                    cmd = cmd + value;
                    cmd = cmd + logic.getLogicOutput();//逻辑输出
                    crc = CrcModbus.DoCRCModBus(cmd);
                    hexstr = (cmd + crc).replaceAll(" ", "");
                    if (Optional.ofNullable(equipInfoObj.getFirmwareChannelId()).isPresent()) {
                        String jsonStr = JSONUtil.parse(SocketResult.getSocketResult(0, hexstr, null, 0,1)).toString();
                        SendUtils.sendWebSocketClient(jsonStr, equipInfoObj.getFirmwareChannelId());
                    }
                    messageStorage = SendUtils.sendClient(hexstr, equipInfoObj.getChannelId(), "01");
                    if (Optional.ofNullable(messageStorage).isPresent()) {
                        if (!"01".equals(messageStorage)) {
                            TcpServerHandler.equipWorkSet.remove(equipInfoObj.getEquipMac());
                            return ResultUtils.genErrorResult("编号" + logic.getNumber() + ":失败应答！");
                        }
                    }else {
                        TcpServerHandler.equipWorkSet.remove(equipInfoObj.getEquipMac());
                        return ResultUtils.genErrorResult("设备未回复");
                    }
                }
            }
        } else {
            TcpServerHandler.equipWorkSet.remove(equipInfoObj.getEquipMac());
            return ResultUtils.genErrorResult("没有获取到网关对应的通道");
        }
        TcpServerHandler.equipWorkSet.remove(equipInfoObj.getEquipMac());
        return ResultUtils.genSuccesResult();
    }
}
