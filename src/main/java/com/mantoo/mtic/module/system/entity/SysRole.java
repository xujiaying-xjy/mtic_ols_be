package com.mantoo.mtic.module.system.entity;

import com.mantoo.mtic.common.entity.BaseEntity;
import java.util.Date;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "sys_role")
public class SysRole extends BaseEntity {
    /**
     * 角色id
     */
    @Id
    @Column(name = "role_id")
    private Long roleId;

    /**
     * 角色名称
     */
    @Column(name = "role_name")
    private String roleName;

    /**
     * 角色类型（预留）
     */
    @Column(name = "role_type")
    private Integer roleType;

    /**
     * 排序
     */
    @Column(name = "sort")
    private Integer sort;

    /**
     * 创建人
     */
    @Column(name = "create_by")
    private Long createBy;

    /**
     * 创建时间
     */
    @Column(name = "create_date")
    private Date createDate;

    /**
     * 更新人
     */
    @Column(name = "update_by")
    private Long updateBy;

    /**
     * 更新时间
     */
    @Column(name = "update_date")
    private Date updateDate;

    /**
     * 删除标识（1是，0否）
     */
    @Column(name = "delete_flag")
    private Integer deleteFlag;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;
}