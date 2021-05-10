package com.mantoo.mtic.module.system.entity.agreement;

import com.mantoo.mtic.common.entity.BaseEntity;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "agreement_measured_values")
public class AgreementMeasuredValues extends BaseEntity {
    /**
     * 协议主键id
     */
    @Column(name = "agreement_id")
    private Long agreementId;

    /**
     * 序号
     */
    @Column(name = "serial_number")
    private Integer serialNumber;

    /**
     * 测值名称
     */
    @Column(name = "measured_name")
    private String measuredName;

    /**
     * 测值编号
     */
    @Column(name = "measured_code")
    private String measuredCode;
}