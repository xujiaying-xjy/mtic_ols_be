package com.mantoo.mtic.module.system.service.equip;

import com.github.pagehelper.PageHelper;
import com.mantoo.mtic.common.entity.RestResult;
import com.mantoo.mtic.common.utils.UserUtil;
import com.mantoo.mtic.module.system.entity.equip.EquipInfo;
import com.mantoo.mtic.module.system.entity.equip.EquipInfoDetails;
import com.mantoo.mtic.module.system.entity.system.MaterialManage;
import com.mantoo.mtic.module.system.entity.vo.EquipInfoVo;

import java.util.List;

/**
 * @author keith
 * @version 1.0
 * @date 2021-04-20
 */
public interface IEquipInfoService {


    /**
     * 获取设备列表
     * @param equipInfoVo 设备信息
     * @return
     */
    List<EquipInfoVo> getPageList(EquipInfoVo equipInfoVo);

    /**
     * 更新固件信息
     * @param equipIdList
     * @param firmwareId
     */
    void updateFirmware(List<Long> equipIdList, Long firmwareId);

    /**
     * 更新设备状态
     * @param equipIdList
     * @param equipStatus
     * @param remark
     */
    void updateEquipStatus(List<Long> equipIdList, Integer equipStatus, String remark);

    /**
     * 查询设备操作记录
     *
     * @param equipInfo
     * @return
     */
    List<EquipInfoDetails> selectRecord(EquipInfo equipInfo);

    /**
     * 查询设备资料信息
     *
     * @param equipInfo
     * @return
     */
    List<MaterialManage> selectMaterial(EquipInfo equipInfo);

    /**
     * @return RestResult<String>
     * @Description 固件升级
     * @Param [equipInfoVo]
     * @Author xjy
     * @Date 2021/4/21 11:31
     */
    RestResult<String> firmwareUpgrade(EquipInfoVo equipInfoVo);

}
