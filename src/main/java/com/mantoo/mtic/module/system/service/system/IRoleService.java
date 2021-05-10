package com.mantoo.mtic.module.system.service.system;

import com.mantoo.mtic.module.system.data.ExOpenMenu;
import com.mantoo.mtic.module.system.data.ExSysRole;
import com.mantoo.mtic.module.system.entity.Permission;
import com.mantoo.mtic.module.system.entity.SysRole;

import java.util.List;

/**
 * @ClassName: IRoleService
 * @Description: 系统角色service
 * @Author: renjt
 * @Date: 2019-11-27 16:34
 */
public interface IRoleService {

    /**
     * @return java.util.List<com.mantoo.mtic.module.system.entity.SysRole>
     * @Description 查询角色列表
     * @Param [sysRole]
     * @Author renjt
     * @Date 2019-11-27 16:52
     */
    List<SysRole> getPageList(SysRole sysRole);

    /**
     * @return int
     * @Description 新增角色
     * @Param [sysRole]
     * @Author renjt
     * @Date 2019-11-27 16:49
     */
    int addRole(SysRole sysRole, Long userId);

    /**
     * @return int
     * @Description 编辑角色
     * @Param [sysRole]
     * @Author renjt
     * @Date 2019-11-27 16:52
     */
    int updateRole(SysRole sysRole, Long userId);

    /**
     * @return int
     * @Description 删除角色
     * @Param [sysRole]
     * @Author renjt
     * @Date 2019-11-27 16:52
     */
    int delRole(SysRole sysRole, Long userId);

    /**
     * @return java.util.List<com.mantoo.mtic.module.system.data.ExOpenMenu>
     * @Description 根据角色获取菜单权限列表（传null为获取所有）
     * @Param [roleId]
     * @Author renjt
     * @Date 2019-11-28 13:09
     */
    List<ExOpenMenu> getMenuListByRole(Long roleId);

    /**
     * @return java.util.List<com.mantoo.mtic.module.system.data.ExOpenMenu>
     * @Description 根据角色获取菜单列表（没有权限列表），登录时调用
     * @Param [roleId]
     * @Author renjt
     * @Date 2020-4-1 15:51
     */
    List<ExOpenMenu> getMenuListByLogin(Long roleId);

    /**
     * @return void
     * @Description 权限配置
     * @Param [exSysRole]
     * @Author renjt
     * @Date 2019-11-28 16:17
     */
    void permissionConf(ExSysRole exSysRole);

    /**
     * @return java.util.List<com.mantoo.mtic.module.system.entity.Permission>
     * @Description 根据角色id获取权限list
     * @Param [roleId]
     * @Author renjt
     * @Date 2020-4-1 15:25
     */
    List<Permission> getPermissionsByRoleId(Long roleId);

}
