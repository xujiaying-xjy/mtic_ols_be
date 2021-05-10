package com.mantoo.mtic.module.system.controller.equip;

import com.github.pagehelper.PageInfo;
import com.mantoo.mtic.common.entity.RestResult;
import com.mantoo.mtic.common.entity.ResultUtils;
import com.mantoo.mtic.common.utils.StringUtil;
import com.mantoo.mtic.module.system.entity.equip.EquipInfo;
import com.mantoo.mtic.module.system.entity.equip.EquipInfoDetails;
import com.mantoo.mtic.module.system.entity.system.MaterialManage;
import com.mantoo.mtic.module.system.entity.vo.EquipInfoVo;
import com.mantoo.mtic.module.system.service.equip.IEquipInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author keith
 * @version 1.0
 * @date 2021-04-20
 */
@RestController
@RequestMapping("/web/equip")
public class EquipInfoController {

    @Resource
    private IEquipInfoService iEquipInfoService;

    /**
     * 获取设备列表
     *
     * @param equipInfoVo 设备信息
     * @return 设备信息
     */
    @PostMapping("/getPageList")
    //@RequiresPermissions()
    public RestResult<PageInfo<EquipInfoVo>> getPageList(@RequestBody EquipInfoVo equipInfoVo) {
        return ResultUtils.genSuccesResult(ResultUtils.toPageInfo(iEquipInfoService.getPageList(equipInfoVo)));
    }

    /**
     * 更新固件信息
     *
     * @param equipIdList 设备id
     * @param firmwareId  固件id
     */
    @PostMapping("/updateFirmware")
    //@RequiresPermissions("")
    public RestResult<String> updateFirmware(@RequestParam(value = "equipIdList") List<Long> equipIdList, Long firmwareId) {
        iEquipInfoService.  updateFirmware(equipIdList, firmwareId);
        return ResultUtils.genSuccesResult();
    }

    /**
     * 更新设备状态
     *
     * @param equipIdList 设备id
     * @param equipStatus 设备状态
     * @param remark      备注
     */
    @PostMapping("/updateEquipStatus")
    //@RequiresPermissions("")
    public RestResult<String> updateEquipStatus(@RequestParam(value = "equipIdList") List<Long> equipIdList, Integer equipStatus, String remark) {
        iEquipInfoService.updateEquipStatus(equipIdList, equipStatus, remark);
        return ResultUtils.genSuccesResult();
    }

    /**
     * 查询设备操作记录
     *
     * @param equipInfo 设备信息
     * @return 设备操作纪律
     */
    @PostMapping("/selectRecord")
    //@RequiresPermissions("")
    public RestResult<List<EquipInfoDetails>> selectRecord(@RequestBody EquipInfo equipInfo) {
        return ResultUtils.genSuccesResult(iEquipInfoService.selectRecord(equipInfo));
    }

    /**
     * 查询设备资料信息
     *
     * @param equipInfo 设备信息
     * @return 设备资料信息
     */
    @PostMapping("/selectMaterial")
    //@RequiresPermissions("")
    public RestResult<List<MaterialManage>> selectMaterial(@RequestBody EquipInfo equipInfo) {
        return ResultUtils.genSuccesResult(iEquipInfoService.selectMaterial(equipInfo));
    }

    /**
     * @return com.mantoo.mtic.common.entity.RestResult<java.lang.String>
     * @Description 固件升级
     * @Param [equipInfo]
     * @Author xjy
     * @Date 2021/4/21 11:26
     */
    @PostMapping("/firmwareUpgrade")
    //@RequiresPermissions("")
    public RestResult<String> firmwareUpgrade(@RequestBody EquipInfoVo equipInfoVo){
        if (equipInfoVo.getEquipId() == null) {
            return ResultUtils.genErrorResult("equipId不能为空");
        }
        //待升级固件id
        if (equipInfoVo.getWaitFirmwareId() == null) {
            return ResultUtils.genErrorResult("waitFirmwareId不能为空");
        }
        //当前固件版本
        if (StringUtil.isBlank(equipInfoVo.getFirmwareVersion())) {
            return ResultUtils.genErrorResult("firmwareVersion不能为空");
        }
        return iEquipInfoService.firmwareUpgrade(equipInfoVo);
    }
}
