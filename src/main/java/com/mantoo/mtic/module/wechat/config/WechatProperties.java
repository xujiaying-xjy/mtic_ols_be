package com.mantoo.mtic.module.wechat.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @ClassName: WechatProperties
 * @Description: 微信公众号配置
 * @Author: renjt
 * @Date: 2020-05-08 09:56
 */
@Component
@ConfigurationProperties(prefix = "wechat")
@PropertySource(value = "classpath:wechat.properties", encoding = "utf-8")
@Setter
@Getter
public class WechatProperties {
    /**
     * 设置微信公众号的appid
     */
    private String appId;

    /**
     * 设置微信公众号的app secret
     */
    private String secret;

    /**
     * 设置微信公众号的token
     */
    private String token;

    /**
     * 设置微信公众号的EncodingAESKey
     */
    private String aesKey;

    /**
     * 点击报警详情跳转的url
     */
    private String redirectUrl;

    /**
     * 网关离线报警模板id
     */
    private String gatewayWarnTemplateId;

    /**
     * 传感器测值报警模板id
     */
    private String measureWarnTemplateId;

    /**
     * 变量报警模板id
     */
    private String variableWarnTemplateId;

    /**
     * 网关年限报警模板id
     */
    private String gatewayYearWarnTemplateId;

    /**
     * 传感器年限报警模板id
     */
    private String sensorYearWarnTemplateId;

    /**
     * 开关量传感器报警模板id
     */
    private String switchSensorWarnTemplateId;
}
