package com.mantoo.mtic.module.system.entity.equip;

import com.mantoo.mtic.common.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

@Getter
@Setter
@Table(name = "equip_group_customer")
public class EquipGroupCustomer extends BaseEntity {
    /**
     * id
     */
    @Column(name = "equip_group_id")
    private Long equipGroupId;

    /**
     * 客户id
     */
    @Column(name = "customer_id")
    private Long customerId;

    /**
     * 客户名称
     */
    @Transient
    private String customerName;
}
