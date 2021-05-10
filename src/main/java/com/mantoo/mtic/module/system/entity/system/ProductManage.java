package com.mantoo.mtic.module.system.entity.system;

import com.mantoo.mtic.common.entity.BaseEntity;
import java.util.Date;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

//产品
@Getter
@Setter
@Table(name = "product_manage")
public class ProductManage extends BaseEntity {
    /**
     * 主键id
     */
    @Id
    @Column(name = "product_id")
    @GeneratedValue(generator = "JDBC")
    private Long productId;

    /**
     * 产品名称
     */
    @Column(name = "product_name")
    private String productName;

    /**
     * 产品型号
     */
    @Column(name = "product_code")
    private String productCode;

    /**
     * 产品类型（1.网关设备；2.网关子设备；3.直连设备）
     */
    @Column(name = "product_type")
    private Integer productType;

    /**
     * 生产状态（1.正常；2.停产）
     */
    @Column(name = "produce_status")
    private Integer produceStatus;

    /**
     * 产品描述
     */
    @Column(name = "product_describe")
    private String productDescribe;

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
}