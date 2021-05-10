package com.mantoo.mtic.module.miniapp.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: MiniAppConfig
 * @Description: 小程序配置类
 * @Author: renjt
 * @Date: 2020-06-18 16:43
 */
@Configuration
public class MiniAppConfig {

    @Autowired
    MiniAppProperties miniAppProperties;

    @Bean
    public WxMaService wxMaService(){
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid(miniAppProperties.getAppid());
        config.setSecret(miniAppProperties.getSecret());
        config.setToken(miniAppProperties.getToken());
        config.setAesKey(miniAppProperties.getAesKey());
        config.setMsgDataFormat(miniAppProperties.getMsgDataFormat());

        WxMaService service = new WxMaServiceImpl();
        service.setWxMaConfig(config);
        return service;
    }
}