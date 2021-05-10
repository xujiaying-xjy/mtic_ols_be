package com.mantoo.mtic.module.wechat.controller.person;

import com.mantoo.mtic.common.entity.RestResult;
import com.mantoo.mtic.common.entity.ResultUtils;
import com.mantoo.mtic.common.utils.StringUtil;
import com.mantoo.mtic.common.utils.UserUtil;
import com.mantoo.mtic.conf.redis.RedisService;
import com.mantoo.mtic.conf.shiro.Constant;
import com.mantoo.mtic.module.system.data.ExSysUser;
import com.mantoo.mtic.module.system.data.LoginSysUser;
import com.mantoo.mtic.module.system.entity.SysUser;
import com.mantoo.mtic.module.system.service.system.IPersonInfoService;
import com.mantoo.mtic.module.system.service.system.IShortMessageService;
import com.mantoo.mtic.module.system.service.system.IUserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName: PersonInfoController
 * @Description: 个人信息 Controller
 * @Author: renjt
 * @Date: 2019-11-29 14:08
 */
@RestController
@RequestMapping("/wechat/personInfo")
public class WechatPersonInfoController {

    @Autowired
    IPersonInfoService personInfoService;
    @Autowired
    IShortMessageService shortMessageService;
    @Autowired
    RedisService redisService;
    @Autowired
    IUserService userService;

    /**
     * @return com.mantoo.mtic.common.entity.RestResult
     * @Description 修改用户头像
     * @Param [userImg]
     * @Author renjt
     * @Date 2019-11-29 18:58
     */
    @PostMapping("/updateUserImg")
    @RequiresAuthentication
    public RestResult<String> updateUserImg(@RequestParam(value = "userImg") MultipartFile userImg) {
        String path = personInfoService.updateUserImg(userImg, UserUtil.getWechatUserId());
        return ResultUtils.genSuccesResult(path);
    }

    /**
     * @return com.mantoo.mtic.common.entity.RestResult<java.lang.String>
     * @Description 修改密码
     * @Param [exSysUser]
     * @Author renjt
     * @Date 2020-4-6 17:20
     */
    @PostMapping("/updatePassWord")
    @RequiresAuthentication
    public RestResult<String> updatePassWord(@RequestBody ExSysUser exSysUser) {
        if (StringUtil.isBlank(exSysUser.getOldPassWord())) {
            return ResultUtils.genErrorResult("oldPassWord不能为空！");
        }
        if (StringUtil.isBlank(exSysUser.getPassword())) {
            return ResultUtils.genErrorResult("password不能为空！");
        }
        personInfoService.updatePassWord(exSysUser, UserUtil.getWechatUserId());
        return ResultUtils.genSuccesResult();
    }

    /**
     * @return com.mantoo.mtic.common.entity.RestResult<java.lang.String>
     * @Description 获取验证码
     * @Param [user]
     * @Author renjt
     * @Date 2020-5-13 15:38
     */
    @PostMapping("/getMessageAuthCode")
    @RequiresAuthentication
    public RestResult<String> getMessageAuthCode(@RequestBody LoginSysUser user) {
        String mobile = user.getMobile();
        if (StringUtil.isBlank(mobile)) {
            return ResultUtils.genErrorResult("mobile不能为空！");
        }
        // 校验手机号码
        SysUser userByMobile = userService.getUserByMobile(mobile);
        if (userByMobile != null) {
            return ResultUtils.genErrorResult("该手机号码已被绑定");
        }
        shortMessageService.sendMessageAuthCode(mobile, Constant.MESSAGEAUTHCODE_BIND_WECHAT);
        return ResultUtils.genSuccesResult();
    }

    /**
     * @return com.mantoo.mtic.common.entity.RestResult<java.lang.String>
     * @Description 绑定手机号码
     * @Param [loginSysUser]
     * @Author renjt
     * @Date 2020-5-13 15:51
     */
    @PostMapping("/updateMobile")
    @RequiresAuthentication
    public RestResult<String> updateMobile(@RequestBody LoginSysUser loginSysUser) {
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
        if (!redisService.exists(Constant.MESSAGEAUTHCODE_BIND_WECHAT + mobile)) {
            return ResultUtils.genErrorResult("短信验证码已过期，请重新获取");
        } else {
            if (!messageAuthCode.equals(redisService.get(Constant.MESSAGEAUTHCODE_BIND_WECHAT + mobile))) {
                return ResultUtils.genErrorResult("请输入正确的短信验证码");
            }
        }
        // 绑定手机号码
        userService.updateMobile(mobile, UserUtil.getWechatUserId());
        return ResultUtils.genSuccesResult();
    }
}
