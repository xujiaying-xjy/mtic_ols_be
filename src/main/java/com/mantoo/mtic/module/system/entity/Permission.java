package com.mantoo.mtic.module.system.entity;

import com.mantoo.mtic.common.entity.BaseEntity;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "permission")
public class Permission extends BaseEntity {
    /**
     * 权限id
     */
    @Id
    @Column(name = "permission_id")
    private Long permissionId;

    /**
     * 菜单ID
     */
    @Column(name = "menu_id")
    private String menuId;

    /**
     * 权限名称
     */
    @Column(name = "permission_name")
    private String permissionName;

    /**
     * 排序（预留）
     */
    @Column(name = "sort")
    private Integer sort;

    /**
     * shiro权限标识
     */
    @Column(name = "shiro_code")
    private String shiroCode;
}