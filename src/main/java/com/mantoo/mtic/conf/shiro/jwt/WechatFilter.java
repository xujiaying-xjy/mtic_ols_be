package com.mantoo.mtic.conf.shiro.jwt;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.mantoo.mtic.common.entity.ResultUtils;
import com.mantoo.mtic.common.utils.JsonConvertUtil;
import com.mantoo.mtic.common.utils.JwtUtil;
import com.mantoo.mtic.common.utils.PropertiesUtil;
import com.mantoo.mtic.conf.redis.RedisService;
import com.mantoo.mtic.conf.shiro.Constant;
import com.mantoo.mtic.exception.ErrorInfo;
import com.mantoo.mtic.exception.MticException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @ClassName: WechatFilter
 * @Description: 微信过滤器
 * @Author: renjt
 * @Date: 2020-05-07 15:46
 */
@Component
public class WechatFilter extends BasicHttpAuthenticationFilter {

    private static final Logger logger = LoggerFactory.getLogger(WechatFilter.class);

    private static RedisService redisService;

    @Autowired
    public void WechatFilter(RedisService redisService) {
        WechatFilter.redisService = redisService;
    }

    /**
     * 这里我们详细说明下为什么最终返回的都是true，即允许访问
     * 例如我们提供一个地址 GET /article
     * 登入用户和游客看到的内容是不同的
     * 如果在这里返回了false，请求会被直接拦截，用户看不到任何东西
     * 所以我们在这里返回true，Controller中可以通过 subject.isAuthenticated() 来判断用户是否登入
     * 如果有些资源只有登入用户才能访问，我们只需要在方法上面加上 @RequiresAuthentication 注解即可
     * 但是这样做有一个缺点，就是不能够对GET,POST等请求进行分别过滤鉴权(因为我们重写了官方的方法)，但实际上对应用影响不大
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        // 查看当前Header中是否携带Authorization属性(Token)，有的话就进行登录认证授权
        if (this.isLoginAttempt(request, response)) {
            try {
                // 校验登录类型
                verifyLoginType(request, response);
                // 进行Shiro的登录UserRealm
                this.executeLogin(request, response);
            } catch (Exception e) {
                // 认证出现异常，传递错误信息msg
                String msg = e.getMessage();
                // 获取应用异常(该Cause是导致抛出此throwable(异常)的throwable(异常))
                Throwable throwable = e.getCause();
                if (throwable instanceof SignatureVerificationException) {
                    // 该异常为JWT的AccessToken认证失败(Token或者密钥不正确)
                    msg = "Token或者密钥不正确(" + throwable.getMessage() + ")";
                } else if (throwable instanceof TokenExpiredException) {
                    // 该异常为JWT的AccessToken已过期，判断RefreshToken未过期就进行AccessToken刷新
                    if (this.refreshToken(request, response)) {
                        return true;
                    } else {
                        msg = "Token已过期(" + throwable.getMessage() + ")";
                    }
                } else {
                    // 应用异常不为空
                    if (throwable != null) {
                        // 获取应用异常msg
                        msg = throwable.getMessage();
                    }
                }
                // Token认证失败直接返回Response信息
                this.response401(response, msg);
                return false;
            }
        } else {
            // 没有携带Token
            HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
            // 获取当前请求类型
            String httpMethod = httpServletRequest.getMethod();
            // 获取当前请求URI
            String requestURI = httpServletRequest.getRequestURI();
            logger.info("当前请求 {} Authorization属性(Token)为空 请求类型 {}", requestURI, httpMethod);
            // mustLoginFlag = true 开启任何请求必须登录才可访问
            Boolean mustLoginFlag = false;
            if (mustLoginFlag) {
                this.response401(response, "Authorization属性(Token)为空，请先登录！");
                return false;
            }
        }
        return true;
    }

    /**
     * 这里我们详细说明下为什么重写
     * 可以对比父类方法，只是将executeLogin方法调用去除了
     * 如果没有去除将会循环调用doGetAuthenticationInfo方法
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        this.sendChallenge(request, response);
        return false;
    }

    /**
     * 检测Header里面是否包含Authorization字段，有就进行Token登录认证授权
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        // 拿到当前Header中Authorization的AccessToken(Shiro中getAuthzHeader方法已经实现)
        String token = this.getAuthzHeader(request);
        return token != null;
    }

    /**
     * 进行AccessToken登录认证授权
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        // 拿到当前Header中Authorization的AccessToken(Shiro中getAuthzHeader方法已经实现)
        JwtToken token = new JwtToken(this.getAuthzHeader(request));
        // 提交给UserRealm进行认证，如果错误他会抛出异常并被捕获
        this.getSubject(request, response).login(token);
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        // 跨域已经在OriginFilter处全局配置
        /*HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }*/
        return super.preHandle(request, response);
    }

    /**
     * 此处为AccessToken刷新，进行判断RefreshToken是否过期，未过期就返回新的AccessToken且继续正常访问
     */
    private boolean refreshToken(ServletRequest request, ServletResponse response) {
        // 拿到当前Header中Authorization的AccessToken(Shiro中getAuthzHeader方法已经实现)
        String token = this.getAuthzHeader(request);
        // 获取当前Token的帐号信息
        String account = JwtUtil.getClaim(token, Constant.ACCOUNT);
        // 刷新Token需要进行同步防止出现问题，synchronized为单机版，需要集群请使用Redission分布式锁
        synchronized (this) {
            // 判断Redis中RefreshToken是否存在
            if (redisService.exists(Constant.PREFIX_SHIRO_WECHAT_REFRESH_TOKEN + account)) {
                // Redis中RefreshToken还存在，获取RefreshToken的时间戳
                String currentTimeMillisRedis = redisService.get(Constant.PREFIX_SHIRO_WECHAT_REFRESH_TOKEN + account).toString();
                // 获取当前AccessToken中的时间戳，与RefreshToken的时间戳对比，如果当前时间戳一致，进行AccessToken刷新
                if (JwtUtil.getClaim(token, Constant.CURRENT_TIME_MILLIS).equals(currentTimeMillisRedis)) {
                    // 获取当前最新时间戳
                    String currentTimeMillis = String.valueOf(System.currentTimeMillis());
                    // 读取配置文件，获取refreshTokenExpireTime属性
                    PropertiesUtil.readProperties("shiro.properties");
                    String wechatRefreshTokenExpireTime = PropertiesUtil.getProperty("wechatRefreshTokenExpireTime");
                    // 设置RefreshToken中的时间戳为当前最新时间戳，且刷新过期时间重新为30分钟过期(配置文件可配置refreshTokenExpireTime属性)
                    redisService.set(Constant.PREFIX_SHIRO_WECHAT_REFRESH_TOKEN + account, currentTimeMillis, Long.parseLong(wechatRefreshTokenExpireTime));
                    // 将当前用户信息存入redis，方便其他接口获取
                    Object currentUser = redisService.get(Constant.PREFIX_WECHAT_CURRENT_USER + account);
                    redisService.set(Constant.PREFIX_WECHAT_CURRENT_USER + account, currentUser, Long.parseLong(wechatRefreshTokenExpireTime));
                    // 刷新AccessToken，设置时间戳为当前最新时间戳
                    token = JwtUtil.sign(account, currentTimeMillis, Constant.LOGIN_TYPE_WECHAT);
                    // 将新刷新的AccessToken再次进行Shiro的登录
                    JwtToken jwtToken = new JwtToken(token);
                    // 提交给UserRealm进行认证，如果错误他会抛出异常并被捕获，如果没有抛出异常则代表登入成功，返回true
                    this.getSubject(request, response).login(jwtToken);
                    // 刷新AccessToken成功，Redis设置RefreshToken过渡时间，value为旧Token，这个是为了解决Token刷新并发的问题
                    String refreshTokenTransitionExpireTime = PropertiesUtil.getProperty("refreshTokenTransitionExpireTime");
                    // 保存当前旧的Token
                    redisService.set(Constant.PREFIX_SHIRO_WECHAT_REFRESH_TOKEN_TRANSITION + account, this.getAuthzHeader(request), Long.parseLong(refreshTokenTransitionExpireTime));
                    // 最后将刷新的AccessToken存放在Response的Header中的Authorization字段返回
                    HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
                    httpServletResponse.setHeader("Authorization", token);
                    httpServletResponse.setHeader("Access-Control-Expose-Headers", "Authorization");
                    return true;
                } else {
                    // 说明当前Token已经被刷新，判断当前帐号是否在RefreshToken过渡时间，是就放行
                    if (redisService.exists(Constant.PREFIX_SHIRO_WECHAT_REFRESH_TOKEN_TRANSITION + account)) {
                        // 获取RefreshToken过渡时间Key保存的新旧Token
                        String oldToken = redisService.get(Constant.PREFIX_SHIRO_WECHAT_REFRESH_TOKEN_TRANSITION + account).toString();
                        // 判断旧Token是否一致
                        if (this.getAuthzHeader(request).equals(oldToken)) {
                            // 提交给UserRealm进行认证，如果错误他会抛出异常并被捕获，如果没有抛出异常则代表登入成功，返回true
                            this.getSubject(request, response).login(new JwtToken(oldToken));
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 无需转发，直接返回Response信息
     */
    private void response401(ServletResponse response, String msg) {
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        try (PrintWriter out = httpServletResponse.getWriter()) {
            String data = JsonConvertUtil.objectToJson(ResultUtils.genErrorResult(msg, ErrorInfo.TOKEN_EXPIRY.getCode()));
            out.append(data);
        } catch (IOException e) {
            logger.error("直接返回Response信息出现IOException异常:" + e.getMessage());
            throw new MticException("直接返回Response信息出现IOException异常:" + e.getMessage(), ErrorInfo.SERVICE_ERROR.getCode());
        }
    }

    /**
     * @return void
     * @Description 校验登录类型
     * @Param [request, response]
     * @Author renjt
     * @Date 2020-4-2 15:05
     */
    private void verifyLoginType(ServletRequest request, ServletResponse response) {
        // 拿到当前Header中Authorization的AccessToken(Shiro中getAuthzHeader方法已经实现)
        String token = this.getAuthzHeader(request);
        String loginType = JwtUtil.getClaim(token, "loginType");
        if (!Constant.LOGIN_TYPE_WECHAT.equals(loginType)) {
            this.response401(response, "登录类型与接口不匹配");
        }
    }
}