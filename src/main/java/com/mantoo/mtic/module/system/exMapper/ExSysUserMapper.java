package com.mantoo.mtic.module.system.exMapper;

import com.mantoo.mtic.common.mapper.MyMapper;
import com.mantoo.mtic.module.system.data.ExSysUser;
import com.mantoo.mtic.module.system.entity.SysUser;

import java.util.List;

public interface ExSysUserMapper extends MyMapper<SysUser> {

    /**
     * @return java.util.List<com.mantoo.mtic.module.system.data.ExSysUser>
     * @description 分页查询用户列表
     * @Param [exSysUser]
     * @Author renjt
     * @Date 2019-11-27 13:19
     */
    List<ExSysUser> getPageList(ExSysUser exSysUser);

    /**
     * @return java.util.List<com.mantoo.mtic.module.system.data.ExSysUser>
     * @Description 根据用户id获取用户名字列表
     * @Param [exSysUser]
     * @Author loukw
     * @Date 2020/4/2 13:08
     */
    List<SysUser> getUserList(String userIds);
}