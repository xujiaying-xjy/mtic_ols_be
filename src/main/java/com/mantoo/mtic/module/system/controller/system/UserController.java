package com.mantoo.mtic.module.system.controller.system;

import com.github.pagehelper.PageInfo;
import com.mantoo.mtic.common.entity.RestResult;
import com.mantoo.mtic.common.entity.ResultUtils;
import com.mantoo.mtic.common.utils.StringUtil;
import com.mantoo.mtic.common.utils.UserUtil;
import com.mantoo.mtic.exception.MticException;
import com.mantoo.mtic.module.system.data.ExSysUser;
import com.mantoo.mtic.module.system.service.system.IUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @ClassName: UserController
 * @Description: 用户管理 Controller
 * @Author: renjt
 * @Date: 2019-11-25 11:54
 */
@RestController
@RequestMapping("/web/user")
public class UserController {

    @Autowired
    IUserService userService;

    /**
     * @return com.mantoo.mtic.common.entity.RestResult<com.github.pagehelper.PageInfo<com.mantoo.mtic.module.system.data.ExSysUser>>
     * @Description 分页查询用户列表
     * @Param  [exSysUser, result]
     * @Author renjt
     * @Date 2019-11-29 19:04
     */
    @PostMapping("/getPageList")
    @RequiresPermissions("user:list")
    public RestResult<PageInfo<ExSysUser>> getPageList(@Valid @RequestBody ExSysUser exSysUser, BindingResult result) {
        //简单参数校验
        if (result.hasErrors()) {
            return ResultUtils.genErrorResult(result.getAllErrors().get(0).getDefaultMessage());
        }
        List<ExSysUser> userList = userService.getPageList(exSysUser);
        if (CollectionUtils.isEmpty(userList)) {
            return ResultUtils.genSuccesNoDataResult(null);
        }
        return ResultUtils.genSuccesResult(ResultUtils.toPageInfo(userList));
    }

    /**
     * @return com.mantoo.mtic.common.entity.RestResult
     * @Description 新增用户
     * @Param [exSysUser, result]
     * @Author renjt
     * @Date 2019-11-29 19:05
     */
    @PostMapping("/addUser")
    @RequiresPermissions("user:add")
    public RestResult<String> addUser(@Valid @RequestBody ExSysUser exSysUser, BindingResult result) {
        //简单参数校验
        if (result.hasErrors()) {
            return ResultUtils.genErrorResult(result.getAllErrors().get(0).getDefaultMessage());
        }
        // 非空校验
        RestResult<String> restResult = notNullUtil(exSysUser);
        if (restResult != null) {
            return restResult;
        }
        int i;
        try {
            i = userService.addUser(exSysUser, UserUtil.getUserId());
        } catch (MticException e) {
            return ResultUtils.genErrorResult(e.getMessage(), e.getCode());
        }
        if (i != 0) {
            // 添加操作日志
            // ...
            return ResultUtils.genSuccesResult();
        } else {
            return ResultUtils.genErrorResult("新增用户失败");
        }
    }

    /**
     * @return com.mantoo.mtic.common.entity.RestResult
     * @Description 编辑用户
     * @Param [exSysUser, result]
     * @Author renjt
     * @Date 2019-11-29 19:05
     */
    @PostMapping("/updateUser")
    @RequiresPermissions("user:update")
    public RestResult<String> updateUser(@Valid @RequestBody ExSysUser exSysUser, BindingResult result) {
        //简单参数校验
        if (result.hasErrors()) {
            return ResultUtils.genErrorResult(result.getAllErrors().get(0).getDefaultMessage());
        }
        // 非空校验
        RestResult<String> restResult = notNullUtil(exSysUser);
        if (restResult != null) {
            return restResult;
        }
        if (exSysUser.getUserId() == null) {
            return ResultUtils.genErrorResult("userId不能为空！");
        }
        int i;
        try {
            i = userService.updateUser(exSysUser, UserUtil.getUserId());
        } catch (MticException e) {
            return ResultUtils.genErrorResult(e.getMessage(), e.getCode());
        }
        if (i != 0) {
            // 添加操作日志
            // ...
            return ResultUtils.genSuccesResult();
        } else {
            return ResultUtils.genErrorResult("编辑用户失败");
        }
    }

    /**
     * @return com.mantoo.mtic.common.entity.RestResult
     * @Description 删除用户
     * @Param [exSysUser, result]
     * @Author renjt
     * @Date 2019-11-29 19:05
     */
    @PostMapping("/delUser")
    @RequiresPermissions("user:del")
    public RestResult<String> delUser(@Valid @RequestBody ExSysUser exSysUser, BindingResult result) {
        //简单参数校验
        if (result.hasErrors()) {
            return ResultUtils.genErrorResult(result.getAllErrors().get(0).getDefaultMessage());
        }
        // 非空校验
        if (exSysUser.getUserId() == null) {
            return ResultUtils.genErrorResult("userId不能为空！");
        }
        if (StringUtil.isBlank(exSysUser.getUserName())) {
            return ResultUtils.genErrorResult("userName不能为空！");
        }
        int i;
        try {
            i = userService.delUser(exSysUser, UserUtil.getUserId());
        } catch (MticException e) {
            return ResultUtils.genErrorResult(e.getMessage(), e.getCode());
        }
        if (0 != i) {
            // 添加操作日志
            // ...
            return ResultUtils.genSuccesResult();
        } else {
            return ResultUtils.genErrorResult("删除用户失败");
        }
    }

    /**
     * @return com.mantoo.mtic.common.entity.RestResult
     * @Description 重置密码
     * @Param [exSysUser, result]
     * @Author renjt
     * @Date 2019-11-29 19:05
     */
    @PostMapping("/restPwd")
    @RequiresPermissions("user:restPwd")
    public RestResult<String> restPwd(@Valid @RequestBody ExSysUser exSysUser, BindingResult result) {
        //简单参数校验
        if (result.hasErrors()) {
            return ResultUtils.genErrorResult(result.getAllErrors().get(0).getDefaultMessage());
        }
        // 非空校验
        if (exSysUser.getUserId() == null) {
            return ResultUtils.genErrorResult("userId不能为空！");
        }
        int i;
        try {
            i = userService.restPwd(exSysUser, UserUtil.getUserId());
        } catch (MticException e) {
            return ResultUtils.genErrorResult(e.getMessage(), e.getCode());
        }
        if (i != 0) {
            // 添加操作日志
            // ...
            return ResultUtils.genSuccesResult();
        } else {
            return ResultUtils.genErrorResult("重置密码失败");
        }
    }

    /**
     * @return com.mantoo.mtic.common.entity.RestResult
     * @Description 启用/停用用户
     * @Param [exSysUser, result]
     * @Author renjt
     * @Date 2019-11-29 19:05
     */
    @PostMapping("/updateDoUse")
    @RequiresPermissions("user:updateDoUse")
    public RestResult<String> updateDoUse(@Valid @RequestBody ExSysUser exSysUser, BindingResult result) {
        //简单参数校验
        if (result.hasErrors()) {
            return ResultUtils.genErrorResult(result.getAllErrors().get(0).getDefaultMessage());
        }
        // 非空校验
        if (exSysUser.getUserId() == null) {
            return ResultUtils.genErrorResult("userId不能为空！");
        }
        if (exSysUser.getDoUse() == null) {
            return ResultUtils.genErrorResult("doUse不能为空！");
        }
        if (StringUtil.isBlank(exSysUser.getUserName())) {
            return ResultUtils.genErrorResult("userName不能为空！");
        }
        int i;
        try {
            i = userService.updateDoUse(exSysUser, UserUtil.getUserId());
        } catch (MticException e) {
            return ResultUtils.genErrorResult(e.getMessage(), e.getCode());
        }
        if (i != 0) {
            // 添加操作日志
            // ...
            return ResultUtils.genSuccesResult();
        } else {
            return ResultUtils.genErrorResult("启用/停用失败");
        }
    }

    /**
     * @return void
     * @description 非空校验工具
     * @Param []
     * @Author renjt
     * @Date 2019-11-26 16:02
     */
    private RestResult<String> notNullUtil(ExSysUser exSysUser) {
        if (StringUtil.isBlank(exSysUser.getUserName())) {
            return ResultUtils.genErrorResult("userName不能为空！");
        }
        if (exSysUser.getRoleId() == null) {
            return ResultUtils.genErrorResult("roleId不能为空！");
        }
        if (StringUtil.isBlank(exSysUser.getName())) {
            return ResultUtils.genErrorResult("name不能为空！");
        }
        if (exSysUser.getReceiveWarning() == null) {
            return ResultUtils.genErrorResult("receiveWarning不能为空！");
        }
        if (StringUtil.isBlank(exSysUser.getMobile())) {
            return ResultUtils.genErrorResult("mobile不能为空！");
        }
        return null;
    }
}
