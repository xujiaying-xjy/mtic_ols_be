package com.mantoo.mtic.module.app.config;

import cn.jpush.api.JPushClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: JiGuangConfig
 * @Description: 极光推送配置类
 * @Author: renjt
 * @Date: 2020-06-17 13:34
 */
@Configuration
public class JiGuangConfig {

    /**
     * 极光官网-个人管理中心-appkey
     */
    @Value("${push.appkey}")
    private String appkey;

    /**
     * 极光官网-个人管理中心-点击查看-secret
     */
    @Value("${push.secret}")
    private String secret;

    @Bean
    public JPushClient jPushClient() {
        return new JPushClient(secret, appkey);
    }
}