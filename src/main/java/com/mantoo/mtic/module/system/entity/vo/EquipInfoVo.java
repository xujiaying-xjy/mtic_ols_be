package com.mantoo.mtic.module.system.entity.vo;

import com.mantoo.mtic.module.system.entity.equip.EquipInfo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;
import java.util.Date;

/**
 * @author keith
 * @version 1.0
 * @date 2021-04-21
 */
@Getter
@Setter
public class EquipInfoVo extends EquipInfo {
    /**
     * 设备组名称
     */
    @Transient
    private String equipGroupName;

    /**
     * 设备组编号
     */
    @Transient
    private String equipGroupCode;

    /**
     * 产品名称
     */
    @Transient
    private String productName;
    /**
     * 产品类型
     */
    @Transient
    private Integer productType;

    /**
     * 固件名称
     */
    @Transient
    private String firmwareName;

    /**
     * 入库开始时间
     */
    @Transient
    private Date inStartDate;

    /**
     * 入库结束时间
     */
    @Transient
    private Date inEndDate;

    /**
     * 出库开始时间
     */
    @Transient
    private Date outStartDate;

    /**
     * 出库结束时间
     */
    @Transient
    private Date outEndDate;

    /**
     * 待升级版本
     */
    @Transient
    private Long waitFirmwareId;

    /**
     * 固件版本
     */
    @Transient
    private String firmwareVersion;
}
