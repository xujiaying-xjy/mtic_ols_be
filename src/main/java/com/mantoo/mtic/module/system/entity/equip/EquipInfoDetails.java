package com.mantoo.mtic.module.system.entity.equip;

import com.mantoo.mtic.common.entity.BaseEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * @author gemi
 */
@Getter
@Setter
@Table(name = "equip_info_details")
public class EquipInfoDetails extends BaseEntity {
    /**
     * 设备id
     */
    @Column(name = "equip_id")
    private Long equipId;

    /**
     * 操作人
     */
    @Column(name = "operation_id")
    private Long operationId;

    /**
     * 操作内容（0.恢复正常；1.退回；2.返修；3.报废；4.出库；5.入库；）
     */
    @Column(name = "operation_status")
    private Integer operationStatus;

    /**
     * 操作时间
     */
    @Column(name = "operation_time")
    private Date operationTime;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

    /**
     * 操作人名
     */
    @Transient
    private String name;
}