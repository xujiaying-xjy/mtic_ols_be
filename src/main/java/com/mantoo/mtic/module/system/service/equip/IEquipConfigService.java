package com.mantoo.mtic.module.system.service.equip;

import com.mantoo.mtic.common.entity.RestResult;
import com.mantoo.mtic.module.system.entity.equip.EquipInfo;
import com.mantoo.mtic.module.system.entity.equipconfig.LogicBasic;
import com.mantoo.mtic.module.system.entity.equipconfig.ModbusBasic;
import com.mantoo.mtic.module.system.entity.vo.EquipInfoVo;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: IEquipConfigService
 * @Description: 远程配置
 * @Author: xjy
 * @Date: 2021-04-23 09:51
 */
public interface IEquipConfigService {

    /**
     * @return com.mantoo.mtic.common.entity.RestResult<java.lang.String>
     * @Description 设备重启
     * @Param [equipInfo]
     * @Author xjy
     * @Date 2021/4/23 9:55
     */
    RestResult<String> equipRestart(EquipInfo equipInfo);

    /**
     * @return com.mantoo.mtic.common.entity.RestResult<java.lang.String>
     * @Description 基础指令读取
     * @Param [basicReadList, equipId]
     * @Author xjy
     * @Date 2021/4/23 11:24
     */
    RestResult<String> basicRead(List<String> basicReadList, Long equipId);

    /**
     * @return com.mantoo.mtic.common.entity.RestResult<java.lang.String>
     * @Description 基础指令设置
     * @Param [basicSetList, equipId]
     * @Author xjy
     * @Date 2021/4/23 14:00
     */
    RestResult<String> basicSet(List<String> basicSetList, Long equipId);

    /**
     * @return com.mantoo.mtic.common.entity.RestResult<java.lang.String>
     * @Description modbus指令读取
     * @Param [equipId]
     * @Author xjy
     * @Date 2021/4/23 15:08
     */
    RestResult<String> modbusRead(Long equipId);

    /**
     * @return com.mantoo.mtic.common.entity.RestResult<java.lang.String>
     * @Description modbus指令下发
     * @Param [basicData]
     * @Author xjy
     * @Date 2021/4/25 11:30
     */
    RestResult<String> modbusSet(ModbusBasic basicData);

    /**
     * @return com.mantoo.mtic.common.entity.RestResult<java.lang.String>
     * @Description 本地逻辑指令读取
     * @Param [equipId]
     * @Author xjy
     * @Date 2021/4/26 9:19
     */
    RestResult<String> logicRead(Long equipId);

    /**
     * @return com.mantoo.mtic.common.entity.RestResult<java.lang.String>
     * @Description 本地逻辑指令下发
     * @Param [logicData]
     * @Author xjy
     * @Date 2021/4/26 10:55
     */
    RestResult<String> logicSet(LogicBasic logicData);
}
