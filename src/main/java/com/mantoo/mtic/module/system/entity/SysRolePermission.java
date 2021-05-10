package com.mantoo.mtic.module.system.entity;

import com.mantoo.mtic.common.entity.BaseEntity;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "sys_role_permission")
public class SysRolePermission extends BaseEntity {
    /**
     * 角色与权限对应关系ID
     */
    @Id
    @Column(name = "role_permission_id")
    private Long rolePermissionId;

    /**
     * 角色id
     */
    @Column(name = "role_id")
    private Long roleId;

    /**
     * 权限id
     */
    @Column(name = "permission_id")
    private Long permissionId;
}