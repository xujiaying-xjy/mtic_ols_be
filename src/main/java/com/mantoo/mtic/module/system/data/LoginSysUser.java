package com.mantoo.mtic.module.system.data;

import com.mantoo.mtic.module.system.entity.Permission;
import com.mantoo.mtic.module.system.entity.SysUser;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;
import javax.validation.Valid;
import java.util.List;

/**
 * @ClassName: LoginSysUser
 * @Description: 登录用户
 * @Author: renjt
 * @Date: 2019-11-25 11:00
 */
@Setter
@Getter
public class LoginSysUser extends SysUser {

    /**
     * 菜单列表
     */
    @Transient
    @Valid
    List<ExOpenMenu> menuList;

    /**
     * 权限列表
     */
    @Transient
    @Valid
    private List<Permission> permissionList;

    /**
     * 短信验证码
     */
    @Transient
    private String messageAuthCode;

    /**
     * 项目id
     */
    @Transient
    private Long projectId;
}
