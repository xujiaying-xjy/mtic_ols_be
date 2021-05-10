package com.mantoo.mtic.module.system.entity.agreement;

import com.mantoo.mtic.common.entity.BaseEntity;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "agreement_warehouse")
public class AgreementWarehouse extends BaseEntity {
    /**
     * 主键id
     */
    @Id
    @Column(name = "agreement_id")
    @GeneratedValue(generator = "JDBC")
    private Long agreementId;

    /**
     * 协议名称
     */
    @Column(name = "agreement_name")
    private String agreementName;

    /**
     * 协议编号
     */
    @Column(name = "agreement_code")
    private String agreementCode;

    /**
     * 品牌
     */
    @Column(name = "brand")
    private String brand;

    /**
     * 型号
     */
    @Column(name = "model_number")
    private String modelNumber;

    /**
     * 删除标识（1是，0否）
     */
    @Column(name = "delete_flag")
    private Integer deleteFlag;

    /**
     * 协议描述
     */
    @Column(name = "agreement_describe")
    private String agreementDescribe;

    /**
     * 创建人
     */
    @Column(name = "created_by")
    private Long createdBy;

    /**
     * 创建时间
     */
    @Column(name = "created_time")
    private Date createdTime;

    /**
     * 更新人
     */
    @Column(name = "updated_by")
    private Long updatedBy;

    /**
     * 更新时间
     */
    @Column(name = "updated_time")
    private Date updatedTime;

    /**
     * 脚本
     */
    @Column(name = "script_content")
    private String scriptContent;

    /**
     * 测试信息
     */
    @Transient
    private List<AgreementMeasuredValues> valuesList;


    /**
     * 说明
     */
    @Column(name = "remark")
    private String remark;


}