package com.mantoo.mtic.common.utils;

import com.mantoo.mtic.conf.redis.RedisService;
import com.mantoo.mtic.conf.shiro.Constant;
import com.mantoo.mtic.module.system.entity.SysUser;
import com.mantoo.mtic.module.system.service.system.IUserService;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 获取当前登录用户工具类
 *
 * @author wliduo[i@dolyw.com]
 * @date 2019/3/15 11:45
 */
@Component
public class UserUtil {

//    private static IUserService userService = SpringContextHolder.getBean(IUserService.class);

    public static UserUtil userUtil;
    @PostConstruct
    public void init() {
        userUtil = this;
    }

    @Resource
    private RedisService redisService;

    /**
     * @return com.mantoo.mtic.module.system.entity.SysUser
     * @Description web端获取当前登录用户
     * @Param []
     * @Author renjt
     * @Date 2020-4-2 13:21
     */
    public static SysUser getUser() {
        /*String token = SecurityUtils.getSubject().getPrincipal().toString();
        // 解密获得Account
        String account = JwtUtil.getClaim(token, Constant.ACCOUNT);
        SysUser user = userService.getUserByUserName(account);
        // 用户是否存在
        if (user == null) {
            throw new MticException("该帐号不存在(The account does not exist.)", ErrorInfo.USER_NOT_EXIST.getCode());
        }*/
        return (SysUser) userUtil.redisService.get(Constant.PREFIX_CURRENT_USER + getAccount());
    }

    /**
     * @return java.lang.Long
     * @Description web端获取当前登录用户Id
     * @Param []
     * @Author renjt
     * @Date 2020-4-2 13:21
     */
    public static Long getUserId() {
        return getUser().getUserId();
    }

    /**
     * @return com.mantoo.mtic.module.system.entity.SysUser
     * @Description APP端获取当前登录用户
     * @Param []
     * @Author renjt
     * @Date 2020-4-2 13:22
     */
    public static SysUser getAppUser(){
        return (SysUser) userUtil.redisService.get(Constant.PREFIX_APP_CURRENT_USER + getAccount());
    }

    /**
     * @return java.lang.Long
     * @Description APP端获取当前登录用户Id
     * @Param []
     * @Author renjt
     * @Date 2020-4-2 13:23
     */
    public static Long getAppUserId(){
        return getAppUser().getUserId();
    }

    /**
     * @return com.mantoo.mtic.module.system.entity.SysUser
     * @Description 微信端获取当前登录用户
     * @Param []
     * @Author renjt
     * @Date 2020-5-7 10:59
     */
    public static SysUser getWechatUser(){
        return (SysUser) userUtil.redisService.get(Constant.PREFIX_WECHAT_CURRENT_USER + getAccount());
    }

    /**
     * @return java.lang.Long
     * @Description 微信端获取当前登录用户id
     * @Param []
     * @Author renjt
     * @Date 2020-5-7 11:00
     */
    public static Long getWechatUserId(){
        return getWechatUser().getUserId();
    }

    /**
     * @return com.mantoo.mtic.module.system.entity.SysUser
     * @Description 小程序获取当前登录用户
     * @Param []
     * @Author renjt
     * @Date 2020-5-7 10:59
     */
    public static SysUser getMiniAppUser(){
        return (SysUser) userUtil.redisService.get(Constant.PREFIX_MINIAPP_CURRENT_USER + getAccount());
    }

    /**
     * @return java.lang.Long
     * @Description 小程序获取当前登录用户id
     * @Param []
     * @Author renjt
     * @Date 2020-5-7 11:00
     */
    public static Long getMiniAppUserId(){
        return getMiniAppUser().getUserId();
    }

    /**
     * @return java.lang.String
     * @Description 获取当前登录用户Token
     * @Param []
     * @Author renjt
     * @Date 2020/3/21 20:55
     */
    public static String getToken() {
        return SecurityUtils.getSubject().getPrincipal().toString();
    }

    /**
     * 获取当前登录用户Account
     *
     * @param
     * @return com.wang.model.UserDto
     * @author wliduo[i@dolyw.com]
     * @date 2019/3/15 11:48
     */
    public static String getAccount() {
        String token = SecurityUtils.getSubject().getPrincipal().toString();
        // 解密获得Account
        return JwtUtil.getClaim(token, Constant.ACCOUNT);
    }
}

