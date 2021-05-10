package com.mantoo.mtic.module.system.exMapper;

import com.mantoo.mtic.module.system.entity.Permission;

import java.util.List;

public interface ExPermissionMapper {

    /**
     * @return java.util.List<com.mantoo.mtic.module.system.entity.Permission>
     * @description 根据用户名获取权限list
     * @Param [userName]
     * @Author renjt
     * @Date 2019-11-25 17:24
     */
    List<Permission> getPermissionsByUserName(String userName);

    /**
     * @return java.util.List<com.mantoo.mtic.module.system.entity.Permission>
     * @Description 根据角色id获取权限list
     * @Param [roleId]
     * @Author renjt
     * @Date 2020-4-1 15:25
     */
    List<Permission> getPermissionsByRoleId(Long roleId);
}