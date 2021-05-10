package com.mantoo.mtic.module.miniapp.service;

import com.mantoo.mtic.module.system.entity.SysUser;

/**
 * @ClassName: IMiniAppService
 * @Description: 小程序service
 * @Author: renjt
 * @Date: 2020-06-22 13:53
 */
public interface IMiniAppService {

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
     * @return java.lang.String
     * @Description 通过code获取openid
     * @Param [code]
     * @Author renjt
     * @Date 2020-6-22 15:16
     */
    String getOpenidByCode(String code);
}
