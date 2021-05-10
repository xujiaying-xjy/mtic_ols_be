package com.dahantc.iot.dto;

import com.dahantc.iot.util.encrypt.SHA1;

import java.util.HashMap;
import java.util.Map;

/**
 * 签名对象
 *
 * @author zdq
 * @create 2018/4/10
 */
public class SignDto {
    private String appid;
    private String timestamp;
    private String sign;

    private String appkey;
    private String path;

    public Map<String, String> signData() {
        Map<String, String> data = new HashMap<>();
        this.timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        data.put("appid", appid);
        data.put("timestamp", timestamp);
//        System.out.println("----------------签名信息------------start----------------start----");
//        System.out.println("pathurl:" + path);
//        System.out.println("appid:" + appid);
//        System.out.println("timestamp:" + timestamp);
//        System.out.println("appkey:" + appkey);
        String sign = path + timestamp + appid + appkey;
//        System.out.println("signstr:" + sign);
        data.put("sign", SHA1.encode(sign));
//        System.out.println("sign:" + data.get("sign"));
//        System.out.println("----------------签名信息-------------end-----------------end----\n");
        return data;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
