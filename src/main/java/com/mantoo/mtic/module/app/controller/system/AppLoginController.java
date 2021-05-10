package com.mantoo.mtic.module.app.controller.system;

import com.mantoo.mtic.common.entity.Global;
import com.mantoo.mtic.common.entity.RestResult;
import com.mantoo.mtic.common.entity.ResultUtils;
import com.mantoo.mtic.common.utils.AesCipherUtil;
import com.mantoo.mtic.common.utils.JwtUtil;
import com.mantoo.mtic.common.utils.StringUtil;
import com.mantoo.mtic.common.utils.UserUtil;
import com.mantoo.mtic.conf.redis.RedisService;
import com.mantoo.mtic.conf.shiro.Constant;
import com.mantoo.mtic.exception.ErrorInfo;
import com.mantoo.mtic.module.system.data.LoginSysUser;
import com.mantoo.mtic.module.system.entity.SysUser;
import com.mantoo.mtic.module.system.service.system.IRoleService;
import com.mantoo.mtic.module.system.service.system.IShortMessageService;
import com.mantoo.mtic.module.system.service.system.IUserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @ClassName: LoginController
 * @Description: 登录 Controller
 * @Author: renjt
 * @Date: 2019-11-25 15:46
 */
@RestController
@RequestMapping("/app")
public class AppLoginController {

    private Logger logger = LoggerFactory.getLogger(AppLoginController.class);

    /**
     * RefreshToken过期时间
     */
    @Value("${appRefreshTokenExpireTime}")
    private String appRefreshTokenExpireTime;
    @Autowired
    IUserService userService;
    @Autowired
    RedisService redisService;
    @Autowired
    IRoleService roleService;
    @Autowired
    IShortMessageService shortMessageService;

    /**
     * @return com.mantoo.mtic.common.entity.RestResult<com.mantoo.mtic.module.system.data.LoginSysUser>
     * @Description 登录接口
     * @Param [loginSysUser, result, httpServletResponse]
     * @Author renjt
     * @Date 2019-11-29 19:02
     */
    @PostMapping("/login")
    public RestResult<LoginSysUser> login(@Valid @RequestBody LoginSysUser loginSysUser,
                                          BindingResult result, HttpServletResponse httpServletResponse) {
        //简单参数校验
        if (result.hasErrors()) {
            return ResultUtils.genErrorResult(result.getAllErrors().get(0).getDefaultMessage());
        }
        // 非空校验
        if (StringUtil.isBlank(loginSysUser.getUserName())) {
            return ResultUtils.genErrorResult("userName不能为空！");
        }
        if (StringUtil.isBlank(loginSysUser.getPassword())) {
            return ResultUtils.genErrorResult("password不能为空！");
        }

        logger.debug("APP用户登录：" + loginSysUser.getUserName());

        // 校验用户名是否存在
        SysUser existUser = userService.getUserByUserName(loginSysUser.getUserName());
        if (existUser == null) {
            return ResultUtils.genErrorResult(ErrorInfo.USER_NOT_EXIST);
        }
        // 校验用户是否停用
        if (Global.DISABLE.equals(existUser.getDoUse())) {
            return ResultUtils.genErrorResult(ErrorInfo.USER_NOT_AVAILABLE);
        }
        // 密码进行AES解密
        String key = AesCipherUtil.deCrypto(existUser.getPassword());
        // 因为密码加密是以帐号+密码的形式进行加密的，所以解密后的对比是帐号+密码
        if (key.equals(loginSysUser.getUserName() + loginSysUser.getPassword())) {
            // 清除可能存在的Shiro权限信息缓存
//            if (redisService.exists(Constant.PREFIX_SHIRO_CACHE + loginSysUser.getUserName())) {
//                redisService.remove(Constant.PREFIX_SHIRO_CACHE + loginSysUser.getUserName());
//            }
            // 执行登录操作
            String currentTimeMillis = String.valueOf(System.currentTimeMillis());
            LoginSysUser resultUser = login(existUser, currentTimeMillis, httpServletResponse);

            return ResultUtils.genSuccesResult(resultUser);
        } else {
            return ResultUtils.genErrorResult(ErrorInfo.LOGIN_INFO_ERROR);
        }
    }

    /**
     * @return com.mantoo.mtic.common.entity.RestResult
     * @Description 用户退出（只要登录了就可以退出，无权限校验）
     * @Param []
     * @Author renjt
     * @Date 2019-11-28 17:55
     */
    @PostMapping("/logout")
    @RequiresAuthentication
    public RestResult<String> logout() {
        String userName = UserUtil.getAccount();
        // 清除Shiror登录信息缓存
        if (redisService.exists(Constant.PREFIX_SHIRO_APP_REFRESH_TOKEN + userName)) {
            redisService.remove(Constant.PREFIX_SHIRO_APP_REFRESH_TOKEN + userName);
        }
        // 清除refreshToken过渡时间信息缓存
        if (redisService.exists(Constant.PREFIX_SHIRO_APP_REFRESH_TOKEN_TRANSITION + userName)) {
            redisService.remove(Constant.PREFIX_SHIRO_APP_REFRESH_TOKEN_TRANSITION + userName);
        }
        // 清除当前用户信息缓存
        if (redisService.exists(Constant.PREFIX_APP_CURRENT_USER + userName)) {
            redisService.remove(Constant.PREFIX_APP_CURRENT_USER + userName);
        }
        return ResultUtils.genSuccesResult();
    }

    /**
     * @return com.mantoo.mtic.common.entity.RestResult<java.lang.String>
     * @Description 获取验证码
     * @Param [sysUser]
     * @Author renjt
     * @Date 2020-5-13 13:57
     */
    @PostMapping("/getMessageAuthCode")
    public RestResult<String> getMessageAuthCode(@RequestBody LoginSysUser user) {
        String mobile = user.getMobile();
        if (StringUtil.isBlank(mobile)) {
            return ResultUtils.genErrorResult("mobile不能为空！");
        }
        // 校验手机号码是否存在
        SysUser existUser = userService.getUserByMobile(mobile);
        if (existUser == null) {
            return ResultUtils.genErrorResult("该手机号码尚未注册");
        }
        // 校验用户是否停用
        if (Global.DISABLE.equals(existUser.getDoUse())) {
            return ResultUtils.genErrorResult(ErrorInfo.USER_NOT_AVAILABLE);
        }
        shortMessageService.sendMessageAuthCode(mobile, Constant.MESSAGEAUTHCODE_LOGIN_APP);
        return ResultUtils.genSuccesResult();
    }

    /**
     * @return com.mantoo.mtic.common.entity.RestResult<com.mantoo.mtic.module.system.data.LoginSysUser>
     * @Description 通过手机号码+短信验证码登录
     * @Param [loginSysUser, httpServletResponse]
     * @Author renjt
     * @Date 2020-5-13 14:30
     */
    @PostMapping("/loginByMobile")
    public RestResult<LoginSysUser> loginByMobile(@RequestBody LoginSysUser loginSysUser, HttpServletResponse httpServletResponse) {
        // 非空校验
        String mobile = loginSysUser.getMobile();
        String messageAuthCode = loginSysUser.getMessageAuthCode();
        if (StringUtil.isBlank(mobile)) {
            return ResultUtils.genErrorResult("mobile不能为空！");
        }
        if (StringUtil.isBlank(messageAuthCode)) {
            return ResultUtils.genErrorResult("messageAuthCode不能为空！");
        }
        // 校验短信验证码是否正确
        if (!redisService.exists(Constant.MESSAGEAUTHCODE_LOGIN_APP + mobile)) {
            return ResultUtils.genErrorResult("短信验证码已过期，请重新获取");
        } else {
            if (!messageAuthCode.equals(redisService.get(Constant.MESSAGEAUTHCODE_LOGIN_APP + mobile))) {
                return ResultUtils.genErrorResult("请输入正确的短信验证码");
            }
        }
        // 校验手机号码是否存在
        SysUser existUser = userService.getUserByMobile(mobile);
        if (existUser == null) {
            return ResultUtils.genErrorResult("该手机号码尚未注册");
        }
        // 校验用户是否停用
        if (Global.DISABLE.equals(existUser.getDoUse())) {
            return ResultUtils.genErrorResult(ErrorInfo.USER_NOT_AVAILABLE);
        }
        // 执行登录操作
        String currentTimeMillis = String.valueOf(System.currentTimeMillis());
        LoginSysUser resultUser = login(existUser, currentTimeMillis, httpServletResponse);

        return ResultUtils.genSuccesResult(resultUser);
    }

    /**
     * @return com.mantoo.mtic.module.system.data.LoginSysUser
     * @Description 执行登录操作
     * @Param [existUser, currentTimeMillis]
     * @Author renjt
     * @Date 2020-5-13 14:29
     */
    private LoginSysUser login(SysUser existUser, String currentTimeMillis, HttpServletResponse httpServletResponse) {
        // 设置RefreshToken，时间戳为当前时间戳，直接设置即可(不用先删后设，会覆盖已有的RefreshToken)
        redisService.set(Constant.PREFIX_SHIRO_APP_REFRESH_TOKEN + existUser.getUserName(), currentTimeMillis, Long.parseLong(appRefreshTokenExpireTime));
        // 更新登录时间
        userService.updateLoginTime(existUser.getUserId());
        // 将当前用户信息存入redis，方便其他接口获取
        redisService.set(Constant.PREFIX_APP_CURRENT_USER + existUser.getUserName(), existUser, Long.parseLong(appRefreshTokenExpireTime));

        // 获取当前用户菜单权限列表
        LoginSysUser resultUser = new LoginSysUser();
//        Long roleId = existUser.getRoleId();
//        List<ExOpenMenu> menuList = roleService.getMenuListByLogin(roleId);
//        List<Permission> permissionList = roleService.getPermissionsByRoleId(roleId);
        resultUser.setUserName(existUser.getUserName());
        resultUser.setUserImgUrl(StringUtil.convertFeUrl(existUser.getUserImgUrl()));
        resultUser.setName(existUser.getName());
        resultUser.setSex(existUser.getSex());
        resultUser.setMobile(existUser.getMobile());
        resultUser.setUserType(existUser.getUserType());
        resultUser.setReceiveWarning(existUser.getReceiveWarning());
//        resultUser.setPermissionList(permissionList);
//        resultUser.setMenuList(menuList);

        // 从Header中Authorization返回AccessToken，时间戳为当前时间戳
        String token = JwtUtil.sign(existUser.getUserName(), currentTimeMillis, Constant.LOGIN_TYPE_APP);
        httpServletResponse.setHeader("Authorization", token);
        httpServletResponse.setHeader("Access-Control-Expose-Headers", "Authorization");
        return resultUser;
    }
}