package com.mantoo.mtic.module.miniapp.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.WxMaUserService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.mantoo.mtic.exception.ErrorInfo;
import com.mantoo.mtic.exception.MticException;
import com.mantoo.mtic.module.miniapp.service.IMiniAppService;
import com.mantoo.mtic.module.system.entity.SysUser;
import com.mantoo.mtic.module.system.mapper.SysUserMapper;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: MiniAppServiceImpl
 * @Description: 小程序
 * @Author: renjt
 * @Date: 2020-06-22 13:55
 */
@Service
public class MiniAppServiceImpl implements IMiniAppService {

    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    WxMaService wxMaService;

    /**
     * @return void
     * @Description 绑定openId
     * @Param [sysUser]
     * @Author renjt
     * @Date 2020-5-9 09:49
     */
    @Override
    public void bindOpenId(SysUser sysUser) {
        sysUserMapper.updateByPrimaryKeySelective(sysUser);
    }

    /**
     * @return void
     * @Description 解除绑定openId
     * @Param [userId]
     * @Author renjt
     * @Date 2020-5-13 16:42
     */
    @Override
    public void unBindOpenId(Long userId) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setMiniappOpenId("");
        sysUserMapper.updateByPrimaryKeySelective(sysUser);
    }

    /**
     * @return java.lang.String
     * @Description 通过code获取openid
     * @Param [code]
     * @Author renjt
     * @Date 2020-6-22 15:16
     */
    @Override
    public String getOpenidByCode(String code) {
        String miniappOpenId;
        // String sessionKey;
        try {
            WxMaUserService userService = wxMaService.getUserService();
            WxMaJscode2SessionResult session = userService.getSessionInfo(code);
            miniappOpenId = session.getOpenid();
            // sessionKey = session.getSessionKey();
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new MticException(e.getMessage(), ErrorInfo.USER_INDEFINITE.getCode());
        }
        return miniappOpenId;
    }
}
