package com.mantoo.mtic.module.system.data;

import com.mantoo.mtic.module.system.entity.SysRole;
import com.mantoo.mtic.module.system.entity.SysRolePermission;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;
import javax.validation.Valid;
import java.util.List;

/**
 * @ClassName: ExSysRole
 * @Description: TODO
 * @Author: renjt
 * @Date: 2019-11-27 16:42
 */
@Setter
@Getter
public class ExSysRole extends SysRole {

    /**
     * 菜单列表
     */
    @Transient
    @Valid
    private List<ExOpenMenu> menuList;

    /**
     * 权限列表(用于接收请求参数)
     */
    @Transient
    @Valid
    private List<SysRolePermission> rolePermissionList;

}
