package com.mantoo.mtic.module.system.service.equip.impl;

import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageHelper;
import com.mantoo.mtic.common.entity.RestResult;
import com.mantoo.mtic.common.entity.ResultUtils;
import com.mantoo.mtic.common.entity.SocketResult;
import com.mantoo.mtic.common.utils.ByteUtil;
import com.mantoo.mtic.common.utils.CrcModbus;
import com.mantoo.mtic.common.utils.SendUtils;
import com.mantoo.mtic.common.utils.UserUtil;
import com.mantoo.mtic.module.netty.tcp.TcpServerHandler;
import com.mantoo.mtic.module.system.entity.equip.EquipInfo;
import com.mantoo.mtic.module.system.entity.equip.EquipInfoDetails;
import com.mantoo.mtic.module.system.entity.system.FirmwareManage;
import com.mantoo.mtic.module.system.entity.system.MaterialManage;
import com.mantoo.mtic.module.system.entity.vo.EquipInfoVo;
import com.mantoo.mtic.module.system.exMapper.equip.EquipInfoMapper;
import com.mantoo.mtic.module.system.exMapper.system.FirmwareManageMapper;
import com.mantoo.mtic.module.system.service.equip.IEquipInfoService;
import io.netty.channel.ChannelId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @author keith
 * @version 1.0
 * @date 2021-04-20
 */
@Slf4j
@Service
public class EquipInfoServiceImpl implements IEquipInfoService {

    @Resource
    private EquipInfoMapper equipInfoMapper;
    @Resource
    private FirmwareManageMapper firmwareManageMapper;

    @Value("${file.nginxPath}")
    private String nginxPath;

    @Override
    public List<EquipInfoVo> getPageList(EquipInfoVo equipInfoVo) {
        if (equipInfoVo.getPageNum() != null && equipInfoVo.getPageSize() != null) {
            PageHelper.startPage(equipInfoVo.getPageNum(), equipInfoVo.getPageSize());
        }
        return equipInfoMapper.getPageList(equipInfoVo);
    }

    @Override
    public void updateFirmware(List<Long> equipIdList, Long firmwareId) {
        equipInfoMapper.updateFirmware(equipIdList, firmwareId);
    }

    @Override
    public void updateEquipStatus(List<Long> equipIdList, Integer equipStatus, String remark) {
        equipInfoMapper.updateEquipStatus(equipIdList, equipStatus);
        //??????????????????
        equipInfoMapper.insertRecord(equipIdList, equipStatus, UserUtil.getUserId(), remark);
    }

    @Override
    public List<EquipInfoDetails> selectRecord(EquipInfo equipInfo) {
        return equipInfoMapper.selectRecord(equipInfo.getEquipId());
    }

    @Override
    public List<MaterialManage> selectMaterial(EquipInfo equipInfo) {
        return equipInfoMapper.selectMaterial(equipInfo.getEquipId());
    }

    /**
     * @return void
     * @Description ????????????
     * @Param [equipInfo]
     * @Author xjy
     * @Date 2021/4/21 11:31
     */
    @Override
    public RestResult<String> firmwareUpgrade(EquipInfoVo equipInfo) {
        EquipInfo equipInfoObj = equipInfoMapper.selectByPrimaryKey(equipInfo.getEquipId());
        if ("0".equals(equipInfoObj.getIsOnline())) {
            if (TcpServerHandler.equipWorkSet.contains(equipInfoObj.getEquipMac())) {
                return ResultUtils.genErrorResult("???????????????????????????????????????");
            } else {
                TcpServerHandler.equipWorkSet.add(equipInfoObj.getEquipMac());
            }
            if (Optional.ofNullable(equipInfoObj.getChannelId()).isPresent()) {
                System.out.println("????????????????????????????????????");
                String cmd = "4D54D10301";
                String crc = CrcModbus.DoCRCModBus(cmd);
                String hexstr = (cmd + crc).replaceAll(" ", "");
                if (Optional.ofNullable(equipInfoObj.getFirmwareChannelId()).isPresent()) {
                    String jsonStr = JSONUtil.parse(SocketResult.getSocketResult(0, hexstr, null,0, 1)).toString();
                    SendUtils.sendWebSocketClient(jsonStr, equipInfoObj.getFirmwareChannelId());
                }
                String messageStorage = SendUtils.sendClient(hexstr, equipInfoObj.getChannelId(), "D2");
                if (Optional.ofNullable(messageStorage).isPresent()) {
                    if (Optional.ofNullable(equipInfoObj.getFirmwareChannelId()).isPresent()) {
                        String jsonStr = JSONUtil.parse(SocketResult.getSocketResult(1, messageStorage, null,0, 1)).toString();
                        SendUtils.sendWebSocketClient(jsonStr, equipInfoObj.getFirmwareChannelId());
                    }
                    int versionNow = Integer.valueOf(messageStorage.substring(20, 24),16);//?????????????????????????????????
                    //??????????????????
                    String version = "v"+String.valueOf(versionNow).substring(0,1)+"."+String.valueOf(versionNow).substring(1,2)+"."+String.valueOf(versionNow).substring(2,3);
                    if (!version.equals(equipInfo.getFirmwareVersion())) {
                        FirmwareManage firmwareManageObj = new FirmwareManage();
                        firmwareManageObj.setFirmwareVersion(version);
                        firmwareManageObj.setProductId(equipInfoObj.getProductId());
                        firmwareManageObj.setDeleteFlag(0);
                        FirmwareManage firmwareManage = firmwareManageMapper.selectOne(firmwareManageObj);
                        if (firmwareManage != null) {
                            equipInfoObj.setFirmwareId(firmwareManage.getFirmwareId());
                            equipInfoMapper.updateByPrimaryKeySelective(equipInfoObj);
                        }
                    }
                    //??????????????????id
                    long firmwareIdNew = equipInfo.getWaitFirmwareId();
                    FirmwareManage firmwareManageNew = firmwareManageMapper.selectByPrimaryKey(firmwareIdNew);
                    int versionNew = Integer.valueOf(firmwareManageNew.getFirmwareVersion().replaceAll("," ,"").replace("v" ,""));
                    //?????????????????????????????????
                    if (versionNow >= versionNew){
                        cmd = "4D54D20301";
                        crc = CrcModbus.DoCRCModBus(cmd);
                        hexstr = (cmd + crc).replaceAll(" ", "");
                        if (Optional.ofNullable(equipInfoObj.getFirmwareChannelId()).isPresent()) {
                            String jsonStr = JSONUtil.parse(SocketResult.getSocketResult(0, hexstr, null, 0,1)).toString();
                            SendUtils.sendWebSocketClient(jsonStr, equipInfoObj.getFirmwareChannelId());
                        }
                        SendUtils.sendClient(hexstr, equipInfoObj.getChannelId());
                        TcpServerHandler.equipWorkSet.remove(equipInfoObj.getEquipMac());
                        return ResultUtils.genSuccesResult("?????????????????????????????????????????????,????????????");
                    } else {
                        //???????????????????????????????????????
                        cmd = "4D54D2";//?????????+????????????3???
                        String ver = ByteUtil.intToHexString(versionNew);
                        String verPrefix = "";
                        if(ver.length() < 4){//????????????2?????????????????????0
                            for(int i=0; i < 4-ver.length(); i++){
                                verPrefix = verPrefix + "0";
                            }
                        }
                        ver = verPrefix + ver;//?????????????????????????????????2???
                        //????????????(4)
                        String firmwareSize = ByteUtil.longToHexString(firmwareManageNew.getFirmwareSize());
                        String sizePrefix="";
                        if(firmwareSize.length() < 8){//????????????4?????????????????????0
                            for(int i = 0; i < 8-firmwareSize.length(); i++){
                                sizePrefix = sizePrefix + "0";
                            }
                        }
                        firmwareSize = sizePrefix + firmwareSize;//??????????????????4????????????
                        //?????????URL??????(n)
                        String url = ByteUtil.stringToHexString(nginxPath + firmwareManageNew.getFirmwareFile()).replaceAll(" ", "");
                        //?????????URL????????????(1)
                        String urlLen = ByteUtil.intToHexString(url.length()/2);
                        //????????????(1)
                        String len = ByteUtil.intToHexString(9+url.length()/2);
                        //????????????
                        cmd = cmd + len + ver + firmwareSize + urlLen + url;
                        crc = CrcModbus.DoCRCModBus(cmd);
                        hexstr = (cmd + crc).replaceAll(" ", "");
                        if (Optional.ofNullable(equipInfoObj.getFirmwareChannelId()).isPresent()) {
                            String jsonStr = JSONUtil.parse(SocketResult.getSocketResult(0, hexstr, null, 0,1)).toString();
                            SendUtils.sendWebSocketClient(jsonStr, equipInfoObj.getFirmwareChannelId());
                        }
                        SendUtils.sendClient(hexstr, equipInfoObj.getChannelId());
                    }
                    return ResultUtils.genSuccesResult("???????????????");
                } else {
                    TcpServerHandler.equipWorkSet.remove(equipInfoObj.getEquipMac());
                    return ResultUtils.genErrorResult("???????????????");
                }
            } else {
                TcpServerHandler.equipWorkSet.remove(equipInfoObj.getEquipMac());
                return ResultUtils.genErrorResult("????????????????????????????????????");
            }
        } else {
            return ResultUtils.genErrorResult("???????????????");
        }
    }
}
