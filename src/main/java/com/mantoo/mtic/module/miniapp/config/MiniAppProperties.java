package com.mantoo.mtic.module.miniapp.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @ClassName: MiniAppProperties
 * @Description: 小程序配置
 * @Author: renjt
 * @Date: 2020-06-18 16:48
 */
@Component
@ConfigurationProperties(prefix = "miniapp")
@PropertySource(value = "classpath:miniapp.properties", encoding = "utf-8")
@Setter
@Getter
public class MiniAppProperties {
    /**
     * 设置微信小程序的appid
     */
    private String appid;

    /**
     * 设置微信小程序的Secret
     */
    private String secret;

    /**
     * 设置微信小程序消息服务器配置的token
     */
    private String token;

    /**
     * 设置微信小程序消息服务器配置的EncodingAESKey
     */
    private String aesKey;

    /**
     * 消息格式，XML或者JSON
     */
    private String msgDataFormat;
}
