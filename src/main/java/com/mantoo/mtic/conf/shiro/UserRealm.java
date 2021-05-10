package com.mantoo.mtic.conf.shiro;

import com.mantoo.mtic.common.entity.Global;
import com.mantoo.mtic.common.utils.JwtUtil;
import com.mantoo.mtic.common.utils.StringUtil;
import com.mantoo.mtic.conf.redis.RedisService;
import com.mantoo.mtic.conf.shiro.jwt.JwtToken;
import com.mantoo.mtic.exception.ErrorInfo;
import com.mantoo.mtic.exception.MticException;
import com.mantoo.mtic.module.system.entity.Permission;
import com.mantoo.mtic.module.system.entity.SysUser;
import com.mantoo.mtic.module.system.service.system.IUserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: UserRealm
 * @Description: 自定义 realm
 * @Author: renjt
 * @Date: 2020-04-02 15:46
 */
@Service
public class UserRealm extends AuthorizingRealm {

    @Autowired
    IUserService userService;

    @Autowired
    RedisService redisService;

    /**
     * 大坑，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken authenticationToken) {
        return authenticationToken instanceof JwtToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        String account = JwtUtil.getClaim(principalCollection.toString(), Constant.ACCOUNT);
        // 查询权限
        List<Permission> permissions = userService.getPermissionsByUserName(account);
        if (!CollectionUtils.isEmpty(permissions)) {
            for (Permission permission : permissions) {
                if (permission != null) {
                    // 添加权限
                    simpleAuthorizationInfo.addStringPermission(permission.getShiroCode());
                }
            }
        }
        return simpleAuthorizationInfo;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getCredentials();
        // 解密获得account，用于和数据库进行对比
        String account = JwtUtil.getClaim(token, Constant.ACCOUNT);
        // 解密获取登录类型
        String loginType = JwtUtil.getClaim(token, "loginType");
        // 帐号为空
        if (StringUtil.isBlank(account)) {
            throw new AuthenticationException("Token中帐号为空(The account in Token is empty.)");
        }
        // 登录类型为空
        if (StringUtil.isBlank(loginType)) {
            throw new AuthenticationException("Token中登录类型为空(The loginType in Token is empty.)");
        }
        // 校验用户名是否存在，校验用户是否被锁定、拉黑等
        /*SysUser existUser = userService.getUserByUserName(account);
        if (existUser == null) {
            throw new MticException(ErrorInfo.USER_NOT_EXIST.getMsg(), ErrorInfo.USER_NOT_EXIST.getCode());
        }*/

        // 根据登录类型不同，做不同的认证
        AuthenticationInfo authenticationInfo;
        switch (loginType) {
            case Constant.LOGIN_TYPE_WEB:
                authenticationInfo = webVerify(token);
                break;
            case Constant.LOGIN_TYPE_APP:
                authenticationInfo = appVerify(token);
                break;
            case Constant.LOGIN_TYPE_WECHAT:
                authenticationInfo = wechatVerify(token);
                break;
            case Constant.LOGIN_TYPE_MINIAPP:
                authenticationInfo = miniappVerify(token);
                break;
            default:
                throw new AuthenticationException("Token中登录类型不存在(The loginType in Token is error.)");
        }
        return authenticationInfo;
    }

    /**
     * @return org.apache.shiro.authc.AuthenticationInfo
     * @Description web端token认证
     * @Param [token]
     * @Author renjt
     * @Date 2020-4-2 13:15
     */
    private AuthenticationInfo webVerify(String token) {
        // 解密获得account，用于和数据库进行对比
        String account = JwtUtil.getClaim(token, Constant.ACCOUNT);
        // 用以下代码代替上述代码，redis代替mysql，减轻数据库压力
        if (redisService.exists(Constant.PREFIX_CURRENT_USER + account)) {
            SysUser user = (SysUser) redisService.get(Constant.PREFIX_CURRENT_USER + account);
            if (Global.DISABLE.equals(user.getDoUse())) {
                redisService.remove(Constant.PREFIX_CURRENT_USER + account);
                throw new MticException(ErrorInfo.USER_NOT_AVAILABLE.getMsg(), ErrorInfo.USER_NOT_AVAILABLE.getCode());
            }
        } else {
            throw new MticException("获取当前用户信息失败，请重新登录", ErrorInfo.UN_AUTH.getCode());
        }

        // 判断当前帐号是否在RefreshToken过渡时间，是就放行
        if (redisService.exists(Constant.PREFIX_SHIRO_REFRESH_TOKEN_TRANSITION + account)) {
            // 获取RefreshToken过渡时间Key保存的旧Token
            String oldToken = redisService.get(Constant.PREFIX_SHIRO_REFRESH_TOKEN_TRANSITION + account).toString();
            // 判断旧Token是否一致
            if (token.equals(oldToken)) {
                return new SimpleAuthenticationInfo(oldToken, oldToken, "userRealm");
            }
        }
        // 开始认证，要AccessToken认证通过，且Redis中存在RefreshToken，且两个Token时间戳一致
        if (JwtUtil.verify(token) && redisService.exists(Constant.PREFIX_SHIRO_REFRESH_TOKEN + account)) {
            // 获取RefreshToken的时间戳
            String currentTimeMillisRedis = redisService.get(Constant.PREFIX_SHIRO_REFRESH_TOKEN + account).toString();
            // 获取AccessToken时间戳，与RefreshToken的时间戳对比
            if (JwtUtil.getClaim(token, Constant.CURRENT_TIME_MILLIS).equals(currentTimeMillisRedis)) {
                return new SimpleAuthenticationInfo(token, token, "userRealm");
            }
        }
        throw new AuthenticationException("Token已过期(Token expired or incorrect.)");
    }

    /**
     * @return org.apache.shiro.authc.AuthenticationInfo
     * @Description APP端token认证
     * @Param [token]
     * @Author renjt
     * @Date 2020-4-2 13:15
     */
    private AuthenticationInfo appVerify(String token) {
        // 解密获得account，用于和数据库进行对比
        String account = JwtUtil.getClaim(token, Constant.ACCOUNT);
        // 用以下代码代替上述代码，redis代替mysql，减轻数据库压力
        if (redisService.exists(Constant.PREFIX_APP_CURRENT_USER + account)) {
            SysUser user = (SysUser) redisService.get(Constant.PREFIX_APP_CURRENT_USER + account);
            if (Global.DISABLE.equals(user.getDoUse())) {
                redisService.remove(Constant.PREFIX_APP_CURRENT_USER + account);
                throw new MticException(ErrorInfo.USER_NOT_AVAILABLE.getMsg(), ErrorInfo.USER_NOT_AVAILABLE.getCode());
            }
        } else {
            throw new MticException("获取当前用户信息失败，请重新登录", ErrorInfo.UN_AUTH.getCode());
        }

        // 判断当前帐号是否在RefreshToken过渡时间，是就放行
        if (redisService.exists(Constant.PREFIX_SHIRO_APP_REFRESH_TOKEN_TRANSITION + account)) {
            // 获取RefreshToken过渡时间Key保存的旧Token
            String oldToken = redisService.get(Constant.PREFIX_SHIRO_APP_REFRESH_TOKEN_TRANSITION + account).toString();
            // 判断旧Token是否一致
            if (token.equals(oldToken)) {
                return new SimpleAuthenticationInfo(oldToken, oldToken, "userRealm");
            }
        }
        // 开始认证，要AccessToken认证通过，且Redis中存在RefreshToken，且两个Token时间戳一致
        if (JwtUtil.verify(token) && redisService.exists(Constant.PREFIX_SHIRO_APP_REFRESH_TOKEN + account)) {
            // 获取RefreshToken的时间戳
            String currentTimeMillisRedis = redisService.get(Constant.PREFIX_SHIRO_APP_REFRESH_TOKEN + account).toString();
            // 获取AccessToken时间戳，与RefreshToken的时间戳对比
            if (JwtUtil.getClaim(token, Constant.CURRENT_TIME_MILLIS).equals(currentTimeMillisRedis)) {
                return new SimpleAuthenticationInfo(token, token, "userRealm");
            }
        }
        throw new AuthenticationException("Token已过期(Token expired or incorrect.)");
    }

    /**
     * @return org.apache.shiro.authc.AuthenticationInfo
     * @Description 微信端token认证
     * @Param [token]
     * @Author renjt
     * @Date 2020-5-7 09:55
     */
    private AuthenticationInfo wechatVerify(String token) {
        // 解密获得account，用于和数据库进行对比
        String account = JwtUtil.getClaim(token, Constant.ACCOUNT);
        // 用以下代码代替上述代码，redis代替mysql，减轻数据库压力
        if (redisService.exists(Constant.PREFIX_WECHAT_CURRENT_USER + account)) {
            SysUser user = (SysUser) redisService.get(Constant.PREFIX_WECHAT_CURRENT_USER + account);
            if (Global.DISABLE.equals(user.getDoUse())) {
                redisService.remove(Constant.PREFIX_WECHAT_CURRENT_USER + account);
                throw new MticException(ErrorInfo.USER_NOT_AVAILABLE.getMsg(), ErrorInfo.USER_NOT_AVAILABLE.getCode());
            }
        } else {
            throw new MticException("获取当前用户信息失败，请重新登录", ErrorInfo.UN_AUTH.getCode());
        }

        // 判断当前帐号是否在RefreshToken过渡时间，是就放行
        if (redisService.exists(Constant.PREFIX_SHIRO_WECHAT_REFRESH_TOKEN_TRANSITION + account)) {
            // 获取RefreshToken过渡时间Key保存的旧Token
            String oldToken = redisService.get(Constant.PREFIX_SHIRO_WECHAT_REFRESH_TOKEN_TRANSITION + account).toString();
            // 判断旧Token是否一致
            if (token.equals(oldToken)) {
                return new SimpleAuthenticationInfo(oldToken, oldToken, "userRealm");
            }
        }
        // 开始认证，要AccessToken认证通过，且Redis中存在RefreshToken，且两个Token时间戳一致
        if (JwtUtil.verify(token) && redisService.exists(Constant.PREFIX_SHIRO_WECHAT_REFRESH_TOKEN + account)) {
            // 获取RefreshToken的时间戳
            String currentTimeMillisRedis = redisService.get(Constant.PREFIX_SHIRO_WECHAT_REFRESH_TOKEN + account).toString();
            // 获取AccessToken时间戳，与RefreshToken的时间戳对比
            if (JwtUtil.getClaim(token, Constant.CURRENT_TIME_MILLIS).equals(currentTimeMillisRedis)) {
                return new SimpleAuthenticationInfo(token, token, "userRealm");
            }
        }
        throw new AuthenticationException("Token已过期(Token expired or incorrect.)");
    }

    /**
     * @return org.apache.shiro.authc.AuthenticationInfo
     * @Description 小程序token认证
     * @Param [token]
     * @Author renjt
     * @Date 2020-6-22 11:36
     */
    private AuthenticationInfo miniappVerify(String token) {
        // 解密获得account，用于和数据库进行对比
        String account = JwtUtil.getClaim(token, Constant.ACCOUNT);
        // 用以下代码代替上述代码，redis代替mysql，减轻数据库压力
        if (redisService.exists(Constant.PREFIX_MINIAPP_CURRENT_USER + account)) {
            SysUser user = (SysUser) redisService.get(Constant.PREFIX_MINIAPP_CURRENT_USER + account);
            if (Global.DISABLE.equals(user.getDoUse())) {
                redisService.remove(Constant.PREFIX_MINIAPP_CURRENT_USER + account);
                throw new MticException(ErrorInfo.USER_NOT_AVAILABLE.getMsg(), ErrorInfo.USER_NOT_AVAILABLE.getCode());
            }
        } else {
            throw new MticException("获取当前用户信息失败，请重新登录", ErrorInfo.UN_AUTH.getCode());
        }

        // 判断当前帐号是否在RefreshToken过渡时间，是就放行
        if (redisService.exists(Constant.PREFIX_SHIRO_MINIAPP_REFRESH_TOKEN_TRANSITION + account)) {
            // 获取RefreshToken过渡时间Key保存的旧Token
            String oldToken = redisService.get(Constant.PREFIX_SHIRO_MINIAPP_REFRESH_TOKEN_TRANSITION + account).toString();
            // 判断旧Token是否一致
            if (token.equals(oldToken)) {
                return new SimpleAuthenticationInfo(oldToken, oldToken, "userRealm");
            }
        }
        // 开始认证，要AccessToken认证通过，且Redis中存在RefreshToken，且两个Token时间戳一致
        if (JwtUtil.verify(token) && redisService.exists(Constant.PREFIX_SHIRO_MINIAPP_REFRESH_TOKEN + account)) {
            // 获取RefreshToken的时间戳
            String currentTimeMillisRedis = redisService.get(Constant.PREFIX_SHIRO_MINIAPP_REFRESH_TOKEN + account).toString();
            // 获取AccessToken时间戳，与RefreshToken的时间戳对比
            if (JwtUtil.getClaim(token, Constant.CURRENT_TIME_MILLIS).equals(currentTimeMillisRedis)) {
                return new SimpleAuthenticationInfo(token, token, "userRealm");
            }
        }
        throw new AuthenticationException("Token已过期(Token expired or incorrect.)");
    }
}