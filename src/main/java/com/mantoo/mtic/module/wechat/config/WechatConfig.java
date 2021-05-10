package com.mantoo.mtic.module.wechat.config;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: WechatConfig
 * @Description: 微信配置
 * @Author: renjt
 * @Date: 2020-05-07 17:36
 */
@Configuration
public class WechatConfig {

    @Autowired
    WechatProperties wechatProperties;

    @Bean
    public WxMpService wxMpService() {
        WxMpService wxMpService = new WxMpServiceImpl();
        WxMpDefaultConfigImpl wxMpDefaultConfig = new WxMpDefaultConfigImpl();
        wxMpDefaultConfig.setAppId(wechatProperties.getAppId());
        wxMpDefaultConfig.setSecret(wechatProperties.getSecret());
        wxMpDefaultConfig.setToken(wechatProperties.getToken());
        wxMpDefaultConfig.setAesKey(wechatProperties.getAesKey());
        wxMpService.setWxMpConfigStorage(wxMpDefaultConfig);
        return wxMpService;
    }
}