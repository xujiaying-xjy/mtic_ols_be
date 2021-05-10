package com.mantoo.mtic.module.system.entity.vo;

import com.mantoo.mtic.module.system.entity.system.FirmwareManage;
import lombok.*;

import java.util.Date;

/**
 * @Description:
 * @ClassName: FirmwareManageVO
 * @Author: ghy
 * @Date: 2021-04-23 13:46
 */
@Getter
@Setter
@ToString
public class FirmwareManageVO {
    /**
     * 产品id
     */
    private Long productId;

    /**
     * 固件Id
     */
    private  Long firmwareId;

    /**
     * 固件名称
     */
    private String firmwareName;

    /**
     * 产品型号
     */
    private String productCode;

    /**
     * 固件版本
     */
    private String firmwareVersion;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 固件文件
     */
    private String firmwareFile;

    /**
     * 设备数量
     */
    private Integer equipNum;

    /**
     * 固件描述
     */
    private String description;

}
