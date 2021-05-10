package com.mantoo.mtic.module.system.exMapper.equip;

import com.mantoo.mtic.common.mapper.MyMapper;
import com.mantoo.mtic.module.system.entity.equip.EquipInfo;
import com.mantoo.mtic.module.system.entity.equip.EquipInfoDetails;
import com.mantoo.mtic.module.system.entity.system.MaterialManage;
import com.mantoo.mtic.module.system.entity.vo.EquipInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author gemi
 */
public interface EquipInfoMapper extends MyMapper<EquipInfo> {

    /**
     * 查询所有设备
     *
     * @param equipInfoVo 设备信息
     * @return
     */
    List<EquipInfoVo> getPageList(EquipInfoVo equipInfoVo);

    /**
     * 查询设备操作纪律
     *
     * @param equipId 设备id
     * @return
     */
    List<EquipInfoDetails> selectRecord(Long equipId);

    /**
     * 批量更新设备固件信息
     *
     * @param equipIdList 设备id
     * @param firmwareId  骨架id
     */
    void updateFirmware(@Param("equipIdList") List<Long> equipIdList, @Param("firmwareId") Long firmwareId);

    /**
     * 批量更新设备状态信息
     *
     * @param equipIdList 设备id
     * @param equipStatus 设备状态
     */
    void updateEquipStatus(@Param("equipIdList") List<Long> equipIdList, @Param("equipStatus") Integer equipStatus);

    /**
     * 插入操作纪律
     * @param equipIdList 设备id
     * @param operationStatus 状态
     * @param operationId 操作人id
     * @param remark 备注
     */
    void insertRecord(@Param("equipIdList") List<Long> equipIdList, @Param("operationStatus") Integer operationStatus
            ,@Param("operationId") Long operationId,@Param("remark") String remark);

    /**
     * 查询设备资料
     * @param equipId
     * @return
     */
    List<MaterialManage> selectMaterial(Long equipId);

    /**
     * 固件升级通道id
     * @param equipId 设备id
     * @param firmwareChannelId websocket通道id
     */
    int insertFirmwareChannelId(@Param("equipId") Long equipId,@Param("firmwareChannelId") String firmwareChannelId);
}