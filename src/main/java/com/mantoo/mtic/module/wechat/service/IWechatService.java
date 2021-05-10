package com.mantoo.mtic.module.wechat.service;

import com.mantoo.mtic.module.system.data.LoginSysUser;
import com.mantoo.mtic.module.system.entity.SysUser;

/**
 * @ClassName: IWechatService
 * @Description: 微信service
 * @Author: renjt
 * @Date: 2020-05-09 09:42
 */
public interface IWechatService {

    /**
     * @return void
     * @Description 绑定openId
     * @Param [sysUser]
     * @Author renjt
     * @Date 2020-5-9 09:49
     */
    void bindOpenId(SysUser sysUser);

    /**
     * @return void
     * @Description 解除绑定openId
     * @Param [userId]
     * @Author renjt
     * @Date 2020-5-13 16:42
     */
    void unBindOpenId(Long userId);

    /**
     * @return com.mantoo.mtic.module.system.entity.SysUser
     * @Description 微信端注册用户
     * @Param [loginSysUser]
     * @Author renjt
     * @Date 2020-9-17 13:35
     */
    SysUser regist(LoginSysUser loginSysUser);
}
