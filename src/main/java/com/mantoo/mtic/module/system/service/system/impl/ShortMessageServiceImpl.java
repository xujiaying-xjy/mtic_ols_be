package com.mantoo.mtic.module.system.service.system.impl;

import com.mantoo.mtic.common.utils.StringUtil;
import com.mantoo.mtic.conf.redis.RedisService;
import com.mantoo.mtic.module.system.service.system.IShortMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * @ClassName: ShortMessageImpl
 * @Description: 短信
 * @Author: renjt
 * @Date: 2020-05-13 11:39
 */
@Service
@Slf4j
public class ShortMessageServiceImpl implements IShortMessageService {

    @Autowired
    RedisService redisService;
    @Autowired
    private RestTemplate restTemplate;
    /**
     * 发送短信账户
     */
    @Value("${shortMessage.username}")
    private String username;

    /**
     * 发送短信密码
     */
    @Value("${shortMessage.password}")
    private String password;

    /**
     * 发短信前缀
     */
    @Value("${shortMessage.prefix}")
    private String prefix;

    /**
     * 发送短信url
     */
    private static final String URL = "http://114.215.196.145/sendSmsApi?content=CONTENT&username=USERNAME&password=PASSWORD&xh=&mobile=MOBILE";

    /**
     * @return boolean
     * @Description 发送短信
     * @Param [content, mobile]
     * @Author renjt
     * @Date 2020-5-13 11:44
     */
    private boolean sendMessage(String content, String mobile) {
        try {
            String url = URL.replace("CONTENT", URLEncoder.encode(content, "UTF-8"))
                    .replace("USERNAME", username)
                    .replace("PASSWORD", password)
                    .replace("MOBILE", mobile);
            String result = restTemplate.getForObject(url, String.class);
            if (StringUtil.isBlank(result)) {
                log.debug("短信发送失败");
                return false;
            }
            log.debug("短信发送：手机号码：{}，内容：{}", mobile, content);
            if (result.split(",")[0].equals("1")) {
                log.debug("短信发送成功");
                return true;
            } else {
                log.debug("短信发送失败");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @return void
     * @Description 发送短信验证码
     * @Param [mobile]
     * @Author renjt
     * @Date 2020-5-13 13:22
     */
    @Override
    public void sendMessageAuthCode(String mobile, String key) {
        // 生成验证码
        StringBuilder authCode = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int num = (int) (10 * (Math.random()));
            authCode.append(num);
        }
        String content = prefix + "您的验证码是" + authCode.toString() + "，请勿向其他任何人泄露避免造成损失，谢谢。（云平台验证码，5分钟内有效）";
//        String content = "您的验证码是" + authCode.toString() + "，请勿向其他任何人泄露避免造成损失，谢谢。（云平台验证码，5分钟内有效）";
        boolean success = sendMessage(content, mobile);
        // 将验证码存入redis
        if (success) {
            redisService.set(key + mobile, authCode.toString(), 300L);
        }
    }
}