package com.mantoo.mtic.module.system.entity.equip;

import com.mantoo.mtic.common.entity.BaseEntity;
import java.util.Date;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "equip_group")
public class EquipGroup extends BaseEntity {
    /**
     * 主键id
     */
    @Id
    @Column(name = "equip_group_id")
    @GeneratedValue(generator = "JDBC")
    private Long equipGroupId;

    /**
     * 设备组名称
     */
    @Column(name = "equip_group_name")
    private String equipGroupName;

    /**
     * 设备组编号
     */
    @Column(name = "equip_group_code")
    private String equipGroupCode;

    /**
     * 漫途负责人
     */
    @Column(name = "person_in_charge")
    private String personInCharge;

    /**
     * 漫途负责人名称
     */
    @Transient
    private String personInChargeName;

    /**
     * 客户id
     */
    @Transient
    private String customerId;

    /**
     * 客戶名称
     */
    @Transient
    private String customerName;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

    /**
     * 删除标识（1是，0否）
     */
    @Column(name = "delete_flag")
    private Integer deleteFlag;

    /**
     * 创建人
     */
    @Column(name = "create_by")
    private Long createBy;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新人
     */
    @Column(name = "update_by")
    private Long updateBy;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;


    /**
     * 设备数量
     */
    @Transient
    private int equipNum;

}