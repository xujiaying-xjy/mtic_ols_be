package com.mantoo.mtic.module.system.entity.system;

import com.mantoo.mtic.common.entity.BaseEntity;
import java.util.Date;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;


//固件
@Getter
@Setter
@Table(name = "firmware_manage")
public class FirmwareManage extends BaseEntity {
    /**
     * 主键id
     */
    @Id
    @Column(name = "firmware_id")
    @GeneratedValue(generator = "JDBC")
    private Long firmwareId;

    /**
     * 固件名称
     */
    @Column(name = "firmware_name")
    private String firmwareName;

    /**
     * 固件版本号
     */
    @Column(name = "firmware_version")
    private String firmwareVersion;

    /**
     * 所属产品
     */
    @Column(name = "product_id")
    private Long productId;

    /**
     * 固件文件
     */
    @Column(name = "firmware_file")
    private String firmwareFile;

    /**
     * 固件描述
     */
    @Column(name = "firmware_describe")
    private String firmwareDescribe;

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
     * 固件大小
     */
    @Column(name = "firmware_size")
    private Integer firmwareSize;
}