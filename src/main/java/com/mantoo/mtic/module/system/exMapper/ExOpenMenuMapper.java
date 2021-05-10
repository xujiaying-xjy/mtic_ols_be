package com.mantoo.mtic.module.system.exMapper;

import com.mantoo.mtic.module.system.data.ExOpenMenu;

import java.util.List;

public interface ExOpenMenuMapper {

    /**
     * @return java.util.List<com.mantoo.mtic.module.system.data.ExOpenMenu>
     * @Description 根据角色获取菜单权限列表（传null为获取所有）,角色管理页面调用
     * @Param [roleId]
     * @Author renjt
     * @Date 2019-11-28 14:49
     */
    List<ExOpenMenu> getMenuListByRole(Long roleId);

    /**
     * @return java.util.List<com.mantoo.mtic.module.system.data.ExOpenMenu>
     * @Description 根据角色获取菜单列表（没有权限列表），登录时调用
     * @Param [roleId]
     * @Author renjt
     * @Date 2020-4-1 15:35
     */
    List<ExOpenMenu> getMenuListByLogin(Long roleId);
}