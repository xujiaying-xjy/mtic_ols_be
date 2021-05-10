package com.mantoo.mtic.module.system.entity.equip;

import com.mantoo.mtic.common.entity.BaseEntity;

import java.util.Date;
import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

/**
 * @author gemi
 */
@Getter
@Setter
@Table(name = "equip_info")
public class EquipInfo extends BaseEntity {
    /**
     * 主键id
     */
    @Id
    @Column(name = "equip_id")
    @GeneratedValue(generator = "JDBC")
    private Long equipId;

    /**
     * 序列号
     */
    @Column(name = "equip_sn")
    private String equipSn;

    /**
     * 型号
     */
    @Column(name = "equip_type")
    private String equipType;

    /**
     * 批次号
     */
    @Column(name = "batch_number")
    private String batchNumber;

    /**
     * 序列号生成日期
     */
    @Column(name = "equip_date")
    private String equipDate;

    /**
     * 流水号
     */
    @Column(name = "serial_number")
    private String serialNumber;

    /**
     * 连接状态（0-在线，1-离线，2-未激活）
     */
    @Column(name = "is_online")
    private String isOnline;

    /**
     * 最后上线时间
     */
    @Column(name = "last_ontime")
    private Date lastOntime;

    /**
     * mac
     */
    @Column(name = "equip_mac")
    private String equipMac;

    /**
     * 设备出售状态（0-售出，1-待售）
     */
    @Column(name = "equip_sale_status")
    private Integer equipSaleStatus;

    /**
     * 入库时间
     */
    @Column(name = "in_time")
    private String inTime;

    /**
     * 出库时间
     */
    @Column(name = "out_time")
    private String outTime;

    /**
     * 入库备注
     */
    @Column(name = "in_remark")
    private String inRemark;

    /**
     * 出库备注
     */
    @Column(name = "out_remark")
    private String outRemark;

    /**
     * 设备组id
     */
    @Column(name = "equip_group_id")
    private Long equipGroupId;

    /**
     * 固件id
     */
    @Column(name = "firmware_id")
    private Long firmwareId;

    /**
     * 设备状态（0-正常；1-退回；2-返修；3-报废）
     */
    @Column(name = "equip_status")
    private Integer equipStatus;

    /**
     * 产品id
     */
    @Column(name = "product_id")
    private Long productId;

    /**
     * 设备入库时间
     */
    @Column(name = "equip_in_date")
    private Date equipInDate;

    /**
     * 设备出库时间
     */
    @Column(name = "equip_out_date")
    private Date equipOutDate;

    /**
     * 通道id
     */
    @Column(name = "channel_id")
    private String channelId;

    /**
     * 升级进度
     */
    @Column(name = "upgrade_progress")
    private Integer upgradeProgress;

    /**
     * 固件升级通道id
     */
    @Column(name = "firmware_channel_id")
    private String firmwareChannelId;

}