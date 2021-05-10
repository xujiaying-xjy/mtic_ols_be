package com.mantoo.mtic.module.system.service.system;

import com.mantoo.mtic.module.system.data.ExSysUser;
import com.mantoo.mtic.module.system.entity.Permission;
import com.mantoo.mtic.module.system.entity.SysUser;

import java.util.List;

/**
 * @ClassName: IUserService
 * @Description: 用户service接口
 * @Author: renjt
 * @Date: 2019-11-25 10:41
 */
public interface IUserService {

    /**
     * @return java.util.List<com.mantoo.mtic.module.system.data.ExSysUser>
     * @description 分页查询用户列表
     * @Param [sysUser]
     * @Author renjt
     * @Date 2019-11-25 11:24
     */
    List<ExSysUser> getPageList(ExSysUser sysUser);

    /**
     * @return int
     * @description 新增用户
     * @Param [sysUser]
     * @Author renjt
     * @Date 2019-11-25 11:25
     */
    int addUser(ExSysUser sysUser, Long userId);

    /**
     * @return int
     * @description 编辑用户
     * @Param [sysUser]
     * @Author renjt
     * @Date 2019-11-25 11:25
     */
    int updateUser(ExSysUser sysUser, Long userId);

    /**
     * @return void
     * @Description 修改手机号码
     * @Param [mobile, userId]
     * @Author renjt
     * @Date 2020-5-13 15:47
     */
    void updateMobile(String mobile, Long userId);

    /**
     * @return int
     * @description 删除用户
     * @Param [sysUser]
     * @Author renjt
     * @Date 2019-11-25 11:25
     */
    int delUser(ExSysUser sysUser, Long userId);

    /**
     * @return int
     * @description 重置密码
     * @Param [sysUser]
     * @Author renjt
     * @Date 2019-11-25 11:25
     */
    int restPwd(ExSysUser sysUser, Long userId);

    /**
     * @return int
     * @description 启用或停止用户
     * @Param [sysUser]
     * @Author renjt
     * @Date 2019-11-25 11:25
     */
    int updateDoUse(ExSysUser sysUser, Long userId);

    /**
     * @return com.mantoo.mtic.module.system.entity.SysUser
     * @description 根据用户名查询用户
     * @Param [userName]
     * @Author renjt
     * @Date 2019-11-25 13:33
     */
    SysUser getUserByUserName(String userName);

    /**
     * @return com.mantoo.mtic.module.system.entity.SysUser
     * @Description 根据openId查询用户
     * @Param [openId]
     * @Author renjt
     * @Date 2020-5-7 16:28
     */
    SysUser getUserByOpenId(String openId);

    /**
     * @return com.mantoo.mtic.module.system.entity.SysUser
     * @Description 根据小程序openId查询用户
     * @Param [miniappOpenId]
     * @Author renjt
     * @Date 2020-6-22 14:04
     */
    SysUser getUserByMiniAppOpenId(String miniappOpenId);

    /**
     * @return com.mantoo.mtic.module.system.entity.SysUser
     * @Description 根据手机号码查询用户
     * @Param [openId]
     * @Author renjt
     * @Date 2020-5-13 14:06
     */
    SysUser getUserByMobile(String mobile);

    /**
     * @return com.mantoo.mtic.module.system.data.LoginSysUser
     * @description 根据用户名获取权限
     * @Param [userName]
     * @Author renjt
     * @Date 2019-11-25 17:17
     */
    List<Permission> getPermissionsByUserName(String userName);

    /**
     * @Description 更新登录时间
     * @Param [loginSysUser]
     * @Author renjt
     * @Date 2019-11-29 18:46
     */
    void updateLoginTime (Long userId);

    /**
     * @return void
     * @Description 根据用户名清除redis中的指定用户信息
     * @Param []
     * @Author renjt
     * @Date 2020/3/23 19:11
     */
    void removeUserInfoInRedis(String userName);
}
