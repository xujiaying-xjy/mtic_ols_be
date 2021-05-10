package com.mantoo.mtic.module.system.data;

import com.mantoo.mtic.module.system.entity.SysUser;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 * @ClassName: ExSysUser
 * @Description: 系统用户
 * @Author: renjt
 * @Date: 2019-11-25 11:18
 */
@Getter
@Setter
public class ExSysUser extends SysUser {
    /**
     * 角色名称
     */
    @Transient
    private String roleName;

    /**
     * 旧密码
     */
    @Transient
    private String oldPassWord;
}
