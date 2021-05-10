package com.mantoo.mtic.module.system.mapper;

import com.mantoo.mtic.common.mapper.MyMapper;
import com.mantoo.mtic.module.system.entity.SysUser;

public interface SysUserMapper extends MyMapper<SysUser> {
    //按照用户姓名查询用户
    SysUser selectUserByUserName(String userName);
}