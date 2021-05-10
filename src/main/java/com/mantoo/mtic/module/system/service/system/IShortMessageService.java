package com.mantoo.mtic.module.system.service.system;

/**
 * @ClassName: ShortMessage
 * @Description: 短信
 * @Author: renjt
 * @Date: 2020-05-13 11:25
 */
public interface IShortMessageService {
    /**
     * @return void
     * @Description 发送短信验证码
     * @Param [mobile]
     * @Author renjt
     * @Date 2020-5-13 13:22
     */
    void sendMessageAuthCode(String mobile, String key);
}
