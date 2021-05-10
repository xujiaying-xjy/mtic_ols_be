package com.mantoo.mtic.module.system.controller.equip;

import com.mantoo.mtic.common.entity.RestResult;
import com.mantoo.mtic.common.entity.ResultUtils;
import com.mantoo.mtic.common.utils.StringUtil;
import com.mantoo.mtic.exception.ErrorInfo;
import com.mantoo.mtic.exception.MticException;
import com.mantoo.mtic.module.system.entity.equip.EquipInfo;
import com.mantoo.mtic.module.system.entity.equipconfig.LogicBasic;
import com.mantoo.mtic.module.system.entity.equipconfig.ModbusBasic;
import com.mantoo.mtic.module.system.entity.equipconfig.ModbusDefault;
import com.mantoo.mtic.module.system.entity.equipconfig.ModbusRtu;
import com.mantoo.mtic.module.system.service.equip.IEquipConfigService;
import com.mantoo.mtic.module.system.service.equip.IEquipInfoService;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: EquipConfigController
 * @Description: 远程配置
 * @Author: xjy
 * @Date: 2021-04-23 09:44
 */
@RestController
@RequestMapping("/web/equipConfig")
public class EquipConfigController {

    @Resource
    private IEquipConfigService iEquipConfigService;

    /**
     * @return com.mantoo.mtic.common.entity.RestResult<java.lang.String>
     * @Description 设备重启
     * @Param [equipInfo]
     * @Author xjy
     * @Date 2021/4/23 9:54
     */
    @PostMapping("/equipRestart")
    //@RequiresPermissions("")
    public RestResult<String> equipRestart(@RequestBody EquipInfo equipInfo){
        if (equipInfo.getEquipId() == null) {
            return ResultUtils.genErrorResult("equipId不能为空");
        }
        return iEquipConfigService.equipRestart(equipInfo);
    }

    /**
     * @return com.mantoo.mtic.common.entity.RestResult<java.lang.String>
     * @Description 基础指令读取
     * @Param [basicReadList, equipId]
     * @Author xjy
     * @Date 2021/4/23 11:25
     */
    @PostMapping("/basicRead")
    //@RequiresPermissions("")
    public RestResult<String> basicRead(@RequestParam(value = "basicReadList") List<String> basicReadList, Long equipId) {
        if (equipId == null) {
            return ResultUtils.genErrorResult("equipId不能为空");
        }
        if (basicReadList == null || basicReadList.size() == 0) {
            return ResultUtils.genErrorResult("basicReadList不能为空");
        }
        return iEquipConfigService.basicRead(basicReadList, equipId);
    }

    /**
     * @return com.mantoo.mtic.common.entity.RestResult<java.lang.String>
     * @Description 基础指令设置
     * @Param [basicSetList, equipId]
     * @Author xjy
     * @Date 2021/4/23 14:01
     */
    @PostMapping("/basicSet")
    //@RequiresPermissions("")
    public RestResult<String> basicSet(@RequestParam(value = "basicSetList") List<String> basicSetList, Long equipId){
        if (equipId == null) {
            return ResultUtils.genErrorResult("equipId不能为空");
        }
        if (basicSetList == null || basicSetList.size() == 0) {
            return ResultUtils.genErrorResult("basicSetList不能为空");
        }
        return iEquipConfigService.basicSet(basicSetList, equipId);
    }

    /**
     * @return com.mantoo.mtic.common.entity.RestResult<java.lang.String>
     * @Description modbus指令读取
     * @Param [equipId]
     * @Author xjy
     * @Date 2021/4/23 15:08
     */
    @PostMapping("/modbusRead")
    //@RequiresPermissions("")
    public RestResult<String> modbusRead(Long equipId){
        if (equipId == null) {
            return ResultUtils.genErrorResult("equipId不能为空");
        }
        return iEquipConfigService.modbusRead(equipId);
    }

    /**
     * @return com.mantoo.mtic.common.entity.RestResult<java.lang.String>
     * @Description modbus指令下发
     * @Param [basicData]
     * @Author xjy
     * @Date 2021/4/25 11:31
     */
    @PostMapping("/modbusSet")
    //@RequiresPermissions("")
    public RestResult<String> modbusSet(@RequestBody ModbusBasic basicData){
        if (basicData.getEquipId() == null) {
            return ResultUtils.genErrorResult("equipId不能为空");
        }
        if (StringUtil.isBlank(basicData.getModpolln())) {
            return ResultUtils.genErrorResult("modpolln不能为空");
        }
        if (StringUtil.isBlank(basicData.getAgreeType())) {
            return ResultUtils.genErrorResult("agreeType不能为空");
        }
        if (basicData.getModton() == null) {
            return ResultUtils.genErrorResult("modton不能为空");
        }
        if (StringUtil.isBlank(basicData.getModpollupn())) {
            return ResultUtils.genErrorResult("modpollupn不能为空");
        }
        if (basicData.getUploadton() == null) {
            return ResultUtils.genErrorResult("uploadton不能为空");
        }
        if (basicData.getModbusRtuList() == null) {
            return ResultUtils.genErrorResult("modbusRtuList不能为空");
        }
        if (basicData.getDefaultList() == null) {
            return ResultUtils.genErrorResult("defaultList不能为空");
        }
        return iEquipConfigService.modbusSet(basicData);
    }

    /**
     * @return com.mantoo.mtic.common.entity.RestResult<java.lang.String>
     * @Description 本地逻辑指令读取
     * @Param [equipId]
     * @Author xjy
     * @Date 2021/4/26 9:19
     */
    @PostMapping("/logicRead")
    //@RequiresPermissions("")
    public RestResult<String> logicRead(Long equipId){
        if (equipId == null) {
            return ResultUtils.genErrorResult("equipId不能为空");
        }
        return iEquipConfigService.logicRead(equipId);
    }

    /**
     * @return com.mantoo.mtic.common.entity.RestResult<java.lang.String>
     * @Description 本地逻辑指令下发
     * @Param [logicData]
     * @Author xjy
     * @Date 2021/4/26 13:29
     */
    @PostMapping("/logicSet")
    //@RequiresPermissions("")
    public RestResult<String> logicSet(@RequestBody LogicBasic logicData){
        if (logicData.getEquipId() == null) {
            return ResultUtils.genErrorResult("equipId不能为空");
        }
        if (StringUtil.isBlank(logicData.getLogicEnable())) {
            return ResultUtils.genErrorResult("logicEnable不能为空");
        }
        if (logicData.getLocalLogicList() == null) {
            return ResultUtils.genErrorResult("logicList不能为空");
        }
        return iEquipConfigService.logicSet(logicData);
    }
}
