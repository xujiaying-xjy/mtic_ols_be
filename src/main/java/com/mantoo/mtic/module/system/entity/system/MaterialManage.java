package com.mantoo.mtic.module.system.entity.system;

import com.mantoo.mtic.common.entity.BaseEntity;
import java.util.Date;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

//资料
@Getter
@Setter
@Table(name = "material_manage")
public class MaterialManage extends BaseEntity {
    /**
     * 主键id
     */
    @Id
    @Column(name = "material_id")
    @GeneratedValue(generator = "JDBC")
    private Long materialId;

    /**
     * 资料名称
     */
    @Column(name = "material_name")
    private String materialName;

    /**
     * 所属产品
     */
    @Column(name = "product_id")
    private Long productId;

    /**
     * 资料文件
     */
    @Column(name = "material_file")
    private String materialFile;

    /**
     * 删除标识（1是，0否）
     */
    @Column(name = "delete_flag")
    private Integer deleteFlag;

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
}