package com.mantoo.mtic.conf.shiro;

/**
 * 常量
 * @author dolyw.com
 * @date 2018/9/3 16:03
 */
public class Constant {

    /**
     * redis-key-前缀-shiro:cache:
     */
    public final static String PREFIX_SHIRO_CACHE = "shiro:cache:";

    /**
     * redis-key-前缀-shiro:refresh_token:
     */
    public final static String PREFIX_SHIRO_REFRESH_TOKEN = "shiro:refresh_token:";

    /**
     * redis-key-前缀-shiro:refresh_token:transition:
     */
    public static final String PREFIX_SHIRO_REFRESH_TOKEN_TRANSITION = "shiro:refresh_token_transition:";

    /**
     * redis-key-前缀 当前登录用户
     */
    public final static String PREFIX_CURRENT_USER = "current_user:";

    /**
     * JWT-account:
     */
    public final static String ACCOUNT = "account";

    /**
     * JWT-currentTimeMillis:
     */
    public final static String CURRENT_TIME_MILLIS = "currentTimeMillis";

    /**
     * 登录类型
     */
    public final static String LOGIN_TYPE_WEB = "0";
    public final static String LOGIN_TYPE_APP = "1";
    public final static String LOGIN_TYPE_WECHAT = "2";
    public final static String LOGIN_TYPE_MINIAPP = "3";

    /**
     * redis-key-前缀 APP当前登录用户
     */
    public final static String PREFIX_APP_CURRENT_USER= "current_user:app:";

    /**
     * APP-redis-key-前缀-shiro:refresh_token:
     */
    public final static String PREFIX_SHIRO_APP_REFRESH_TOKEN = "shiro:app:refresh_token:";

    /**
     * APP-redis-key-前缀-shiro:refresh_token:transition:
     */
    public static final String PREFIX_SHIRO_APP_REFRESH_TOKEN_TRANSITION = "shiro:app:refresh_token_transition:";


    /**
     * redis-key-前缀 微信当前登录用户
     */
    public final static String PREFIX_WECHAT_CURRENT_USER= "current_user:wechat:";

    /**
     * 微信-redis-key-前缀-shiro:refresh_token:
     */
    public final static String PREFIX_SHIRO_WECHAT_REFRESH_TOKEN = "shiro:wechat:refresh_token:";

    /**
     * 微信-redis-key-前缀-shiro:refresh_token:transition:
     */
    public static final String PREFIX_SHIRO_WECHAT_REFRESH_TOKEN_TRANSITION = "shiro:wechat:refresh_token_transition:";

    /**
     * redis-key-前缀 小程序当前登录用户
     */
    public final static String PREFIX_MINIAPP_CURRENT_USER= "current_user:miniapp:";

    /**
     * 小程序-redis-key-前缀-shiro:refresh_token:
     */
    public final static String PREFIX_SHIRO_MINIAPP_REFRESH_TOKEN = "shiro:miniapp:refresh_token:";

    /**
     * 小程序-redis-key-前缀-shiro:refresh_token:transition:
     */
    public static final String PREFIX_SHIRO_MINIAPP_REFRESH_TOKEN_TRANSITION = "shiro:miniapp:refresh_token_transition:";

    // redis登录验证码前缀
    public static final String MESSAGEAUTHCODE_LOGIN_WEB = "messageAuthCode:login:web:";
    public static final String MESSAGEAUTHCODE_LOGIN_APP = "messageAuthCode:login:app:";
    public static final String MESSAGEAUTHCODE_LOGIN_WECHAT = "messageAuthCode:login:wechat:";
    public static final String MESSAGEAUTHCODE_REGIST_WECHAT = "messageAuthCode:login:regist:";
    public static final String MESSAGEAUTHCODE_LOGIN_MINIAPP = "messageAuthCode:login:miniapp:";
    // redis绑定手机号码验证码前缀
    public static final String MESSAGEAUTHCODE_BIND_WEB = "messageAuthCode:bind:web:";
    public static final String MESSAGEAUTHCODE_BIND_APP = "messageAuthCode:bind:app:";
    public static final String MESSAGEAUTHCODE_BIND_WECHAT = "messageAuthCode:bind:wechat:";
    public static final String MESSAGEAUTHCODE_BIND_MINIAPP = "messageAuthCode:bind:miniapp:";
}
