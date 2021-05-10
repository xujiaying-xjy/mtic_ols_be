package com.mantoo.mtic.module.wechat.service.impl;

import com.mantoo.mtic.common.utils.AesCipherUtil;
import com.mantoo.mtic.common.utils.DateUtils;
import com.mantoo.mtic.common.utils.StringUtil;
import com.mantoo.mtic.module.system.data.LoginSysUser;
import com.mantoo.mtic.module.system.entity.SysUser;
import com.mantoo.mtic.module.system.mapper.SysUserMapper;
import com.mantoo.mtic.module.wechat.config.WechatProperties;
import com.mantoo.mtic.module.wechat.service.IWechatService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: WechatService
 * @Description: 微信Service
 * @Author: renjt
 * @Date: 2020-05-08 15:57
 */
@Service
@Slf4j
public class WechatServiceimpl implements IWechatService {

    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    WxMpService wxMpService;
    @Autowired
    WechatProperties wechatProperties;

    /**
     * @return void
     * @Description 发送模板消息
     * @Param [userList, templateId, state, templateDataList]
     * @Author renjt
     * @Date 2020-5-9 09:15
     */
    private void sendMessage(List<SysUser> userList, String templateId, String state, List<WxMpTemplateData> templateDataList) {
        // 构建详情url
        String redirectUrl = wechatProperties.getRedirectUrl();
        String detailUrl = redirectUrl.replace("STATE", state);
        // 给用户发送模板消息
        for (SysUser user : userList) {
            String openId = user.getOpenId();
            if (StringUtil.isBlank(openId)) {
                continue;
            }
            WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                    .toUser(openId)
                    .templateId(templateId)
                    .url(detailUrl)
                    .data(templateDataList)
                    .build();
            try {
                wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
                log.debug("微信模板消息发送：{}{}", user.getUserName(), state);
            } catch (WxErrorException e) {
                e.printStackTrace();
            }
        }
    }

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
        sysUser.setOpenId("");
        sysUserMapper.updateByPrimaryKeySelective(sysUser);
    }

    /**
     * @return com.mantoo.mtic.module.system.entity.SysUser
     * @Description 微信端注册用户
     * @Param [loginSysUser]
     * @Author renjt
     * @Date 2020-9-17 13:35
     */
    @Override
    public SysUser regist(LoginSysUser loginSysUser) {
        SysUser user = new SysUser();
        String mobile = loginSysUser.getMobile();
        user.setUserName(mobile);
        user.setMobile(mobile);
        user.setName(loginSysUser.getName());
        user.setOpenId(loginSysUser.getOpenId());
        // 密码以帐号+密码的形式进行AES加密
        String key = AesCipherUtil.enCrypto(loginSysUser.getMobile() + "123456");
        user.setPassword(key);
        Date now = DateUtils.getNowDate();
        user.setLoginTime(now);
        user.setCreateDate(now);
        user.setSex(1);
        user.setUserType(1); //业主端
        user.setRoleId(0L); //角色为微信端用户
        user.setReceiveWarning(0); //不接收报警
        user.setRemark("微信端注册用户");
        sysUserMapper.insertSelective(user);
        return user;
    }
}