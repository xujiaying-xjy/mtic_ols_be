package com.mantoo.mtic.module.system.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.mantoo.mtic.common.entity.Global;
import com.mantoo.mtic.common.utils.AesCipherUtil;
import com.mantoo.mtic.common.utils.DateUtils;
import com.mantoo.mtic.conf.redis.RedisService;
import com.mantoo.mtic.conf.shiro.Constant;
import com.mantoo.mtic.exception.ErrorInfo;
import com.mantoo.mtic.exception.MticException;
import com.mantoo.mtic.module.system.data.ExSysUser;
import com.mantoo.mtic.module.system.entity.Permission;
import com.mantoo.mtic.module.system.entity.SysUser;
import com.mantoo.mtic.module.system.exMapper.ExPermissionMapper;
import com.mantoo.mtic.module.system.exMapper.ExSysUserMapper;
import com.mantoo.mtic.module.system.mapper.SysUserMapper;
import com.mantoo.mtic.module.system.service.system.IUserService;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @ClassName: UserServiceImpl
 * @Description: 用户service实现类
 * @Author: renjt
 * @Date: 2019-11-25 10:44
 */
@Service
@Transactional
public class UserServiceImpl implements IUserService {

    private static final String INIT_PWD = "123456";
    /**
     * RefreshToken过期时间
     */
    @Value("${refreshTokenExpireTime}")
    private String refreshTokenExpireTime;
    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    ExPermissionMapper exPermissionMapper;
    @Autowired
    ExSysUserMapper exSysUserMapper;
    @Autowired
    RedisService redisService;


    /**
     * @return java.util.List<com.mantoo.mtic.module.system.data.ExSysUser>
     * @description 分页查询用户列表
     * @Param [sysUser]
     * @Author renjt
     * @Date 2019-11-25 11:26
     */
    @Override
    public List<ExSysUser> getPageList(ExSysUser exSysUser) {
        if (exSysUser.getPageNum() != null && exSysUser.getPageSize() != null) {
            PageHelper.startPage(exSysUser.getPageNum(), exSysUser.getPageSize());
        }
        return exSysUserMapper.getPageList(exSysUser);
    }

    /**
     * @return int
     * @description 新增用户
     * @Param [sysUser]
     * @Author renjt
     * @Date 2019-11-25 11:26
     */
    @Override
    public int addUser(ExSysUser sysUser, Long userId) {
        // 判断用户名是否重复
        SysUser existUser = getUserByUserName(sysUser.getUserName());
        if (existUser != null) {
            throw new MticException("用户名已存在", ErrorInfo.USER_EXIST.getCode());
        }
        // 判断电话号码是否重复
        SysUser userByMobile = getUserByMobile(sysUser.getMobile());
        if (userByMobile != null) {
            throw new MticException("电话号码已存在", ErrorInfo.USER_EXIST.getCode());
        }
        // 密码以帐号+密码的形式进行AES加密
        String key = AesCipherUtil.enCrypto(sysUser.getUserName() + INIT_PWD);
        sysUser.setPassword(key);
        sysUser.setCreateBy(userId);
        sysUser.setCreateDate(DateUtils.getNowDate());
        return sysUserMapper.insertSelective(sysUser);
    }


    /**
     * @return int
     * @description 编辑用户
     * @Param [sysUser]
     * @Author renjt
     * @Date 2019-11-25 11:26
     */
    @Override
    public int updateUser(ExSysUser sysUser, Long userId) {
        // 判断用户名是否重复
        Example example = new Example(SysUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userName", sysUser.getUserName());
        criteria.andNotEqualTo("userId", sysUser.getUserId());
        criteria.andEqualTo("deleteFlag", SysUser.DELETE_FLAG_NORMAL);
        List<SysUser> userList = sysUserMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(userList)) {
            throw new MticException("用户名已存在", ErrorInfo.USER_EXIST.getCode());
        }
        // 判断电话号码是否重复
        Example exampleMobile = new Example(SysUser.class);
        Example.Criteria criteriaMobile = exampleMobile.createCriteria();
        criteriaMobile.andEqualTo("mobile", sysUser.getMobile());
        criteriaMobile.andNotEqualTo("userId", sysUser.getUserId());
        criteriaMobile.andEqualTo("deleteFlag", SysUser.DELETE_FLAG_NORMAL);
        List<SysUser> userMobileList = sysUserMapper.selectByExample(exampleMobile);
        if (!CollectionUtils.isEmpty(userMobileList)) {
            throw new MticException("电话号码已存在", ErrorInfo.USER_EXIST.getCode());
        }
        sysUser.setUpdateBy(userId);
        sysUser.setUpdateDate(DateUtils.getNowDate());
        return sysUserMapper.updateByPrimaryKeySelective(sysUser);
    }

    /**
     * @return void
     * @Description 修改手机号码
     * @Param [mobile, userId]
     * @Author renjt
     * @Date 2020-5-13 15:47
     */
    @Override
    public void updateMobile(String mobile, Long userId) {
        SysUser user = new SysUser();
        user.setMobile(mobile);
        user.setUserId(userId);
        user.setUpdateBy(userId);
        user.setUpdateDate(DateUtils.getNowDate());
        sysUserMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * @return int
     * @description 删除用户
     * @Param [sysUser]
     * @Author renjt
     * @Date 2019-11-25 11:26
     */
    @Override
    public int delUser(ExSysUser sysUser, Long userId) {
        // 删除用户
        sysUser.setUpdateBy(userId);
        sysUser.setUpdateDate(DateUtils.getNowDate());
        sysUser.setDeleteFlag(SysUser.DELETE_FLAG_DELETED);
        // 清除该用户在redis中的登录信息，相当于退出（踢下线）
        removeUserInfoInRedis(sysUser.getUserName());
        return sysUserMapper.updateByPrimaryKeySelective(sysUser);
    }

    /**
     * @return int
     * @description 重置密码
     * @Param [sysUser]
     * @Author renjt
     * @Date 2019-11-25 11:27
     */
    @Override
    public int restPwd(ExSysUser sysUser, Long userId) {
        // 判断用户是否存在
        SysUser existUserById = sysUserMapper.selectByPrimaryKey(sysUser);
        if (existUserById == null) {
            throw new MticException(ErrorInfo.USER_NOT_EXIST.getMsg(), ErrorInfo.USER_NOT_EXIST.getCode());
        } else if (existUserById.getDeleteFlag() == SysUser.DELETE_FLAG_DELETED) {
            throw new MticException(ErrorInfo.USER_NOT_EXIST.getMsg(), ErrorInfo.USER_NOT_EXIST.getCode());
        }
        sysUser.setUpdateBy(userId);
        sysUser.setUpdateDate(DateUtils.getNowDate());
        // 密码以帐号+密码的形式进行AES加密
        String key = AesCipherUtil.enCrypto(existUserById.getUserName() + INIT_PWD);
        sysUser.setPassword(key);
        return sysUserMapper.updateByPrimaryKeySelective(sysUser);
    }

    /**
     * @return int
     * @description 启用/停用用户
     * @Param [sysUser]
     * @Author renjt
     * @Date 2019-11-25 11:27
     */
    @Override
    public int updateDoUse(ExSysUser sysUser, Long userId) {
        // 清除redis中的登录信息，将redis中的当前用户信息改为停用状态
        if (Global.DISABLE.equals(sysUser.getDoUse())) {
            disableUserInfoInRedis(sysUser);
        }
        sysUser.setUpdateBy(userId);
        sysUser.setUpdateDate(DateUtils.getNowDate());
        return sysUserMapper.updateByPrimaryKeySelective(sysUser);
    }

    /**
     * @return com.mantoo.mtic.module.system.entity.SysUser
     * @description 根据用户名查询（未删除）用户
     * @Param [userName]
     * @Author renjt
     * @Date 2019-11-25 13:34
     */
    @Override
    public SysUser getUserByUserName(String userName) {
        SysUser sysUser = new SysUser();
        sysUser.setUserName(userName);
        sysUser.setDeleteFlag(SysUser.DELETE_FLAG_NORMAL);
        SysUser existUser;
        try {
            existUser = sysUserMapper.selectOne(sysUser);
        } catch (Exception e) {
            throw new MticException(ErrorInfo.EXIST_MORE.getMsg(), ErrorInfo.EXIST_MORE.getCode());
        }
        return existUser;
    }

    /**
     * @return com.mantoo.mtic.module.system.entity.SysUser
     * @Description 根据openId查询用户
     * @Param [userName]
     * @Author renjt
     * @Date 2020-5-7 16:28
     */
    @Override
    public SysUser getUserByOpenId(String openId) {
        SysUser sysUser = new SysUser();
        sysUser.setOpenId(openId);
        sysUser.setDeleteFlag(SysUser.DELETE_FLAG_NORMAL);
        SysUser existUser;
        try {
            existUser = sysUserMapper.selectOne(sysUser);
        } catch (Exception e) {
            throw new MticException(ErrorInfo.EXIST_MORE.getMsg(), ErrorInfo.EXIST_MORE.getCode());
        }
        return existUser;
    }

    /**
     * @return com.mantoo.mtic.module.system.entity.SysUser
     * @Description 根据小程序openId查询用户
     * @Param [miniappOpenId]
     * @Author renjt
     * @Date 2020-6-22 14:04
     */
    @Override
    public SysUser getUserByMiniAppOpenId(String miniappOpenId) {
        SysUser sysUser = new SysUser();
        sysUser.setMiniappOpenId(miniappOpenId);
        sysUser.setDeleteFlag(SysUser.DELETE_FLAG_NORMAL);
        SysUser existUser;
        try {
            existUser = sysUserMapper.selectOne(sysUser);
        } catch (Exception e) {
            throw new MticException(ErrorInfo.EXIST_MORE.getMsg(), ErrorInfo.EXIST_MORE.getCode());
        }
        return existUser;
    }

    /**
     * @return com.mantoo.mtic.module.system.entity.SysUser
     * @Description 根据手机号码查询用户
     * @Param [openId]
     * @Author renjt
     * @Date 2020-5-13 14:06
     */
    @Override
    public SysUser getUserByMobile(String mobile) {
        SysUser sysUser = new SysUser();
        sysUser.setMobile(mobile);
        sysUser.setDeleteFlag(SysUser.DELETE_FLAG_NORMAL);
        SysUser existUser;
        try {
            existUser = sysUserMapper.selectOne(sysUser);
        } catch (Exception e) {
            throw new MticException(ErrorInfo.EXIST_MORE.getMsg(), ErrorInfo.EXIST_MORE.getCode());
        }
        return existUser;
    }

    /**
     * @return com.mantoo.mtic.module.system.data.LoginSysUser
     * @description 根据用户名获取权限
     * @Param [userName]
     * @Author renjt
     * @Date 2019-11-25 17:18
     */
    @Override
    public List<Permission> getPermissionsByUserName(String userName) {
        return exPermissionMapper.getPermissionsByUserName(userName);
    }

    /**
     * @Description 更新登录时间
     * @Param [loginSysUser]
     * @Author renjt
     * @Date 2019-11-29 18:46
     */
    @Override
    public void updateLoginTime(Long userId) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginTime(DateUtils.getNowDate());
        sysUser.setPasswordErrorCount(0);
        sysUserMapper.updateByPrimaryKeySelective(sysUser);
    }

    /**
     * @return void
     * @Description web端根据用户名清除该用户在redis中的登录信息，相当于退出（踢下线）
     * @Param [userName]
     * @Author renjt
     * @Date 2020/3/23 19:27
     */
    public void removeUserInfoInRedis(String userName) {
        // 清除Shiro权限信息缓存
        if (redisService.exists(Constant.PREFIX_SHIRO_CACHE + userName)) {
            redisService.remove(Constant.PREFIX_SHIRO_CACHE + userName);
        }
        // 清除Shiror登录信息缓存
        if (redisService.exists(Constant.PREFIX_SHIRO_REFRESH_TOKEN + userName)) {
            redisService.remove(Constant.PREFIX_SHIRO_REFRESH_TOKEN + userName);
        }
        // 清除refreshToken过渡时间信息缓存
        if (redisService.exists(Constant.PREFIX_SHIRO_REFRESH_TOKEN_TRANSITION + userName)) {
            redisService.remove(Constant.PREFIX_SHIRO_REFRESH_TOKEN_TRANSITION + userName);
        }
        // 清除当前用户信息缓存
        if (redisService.exists(Constant.PREFIX_CURRENT_USER + userName)) {
            redisService.remove(Constant.PREFIX_CURRENT_USER + userName);
        }
    }

    /**
     * @return void
     * @Description 停用用户，清除redis中的登录信息，将redis中的当前用户信息改为停用状态
     * @Param [userId]
     * @Author renjt
     * @Date 2020/3/23 19:38
     */
    private void disableUserInfoInRedis(SysUser user) {
        String userName = user.getUserName();
        // 清除Shiro权限信息缓存
        if (redisService.exists(Constant.PREFIX_SHIRO_CACHE + userName)) {
            redisService.remove(Constant.PREFIX_SHIRO_CACHE + userName);
        }

        // 停用web端
        // 清除Shiror登录信息缓存
        if (redisService.exists(Constant.PREFIX_SHIRO_REFRESH_TOKEN + userName)) {
            redisService.remove(Constant.PREFIX_SHIRO_REFRESH_TOKEN + userName);
        }
        // 清除refreshToken过渡时间信息缓存
        if (redisService.exists(Constant.PREFIX_SHIRO_REFRESH_TOKEN_TRANSITION + userName)) {
            redisService.remove(Constant.PREFIX_SHIRO_REFRESH_TOKEN_TRANSITION + userName);
        }
        // 将该用户<停用>信息存入redis，该用户下次请求将提示用户已停用
        redisService.set(Constant.PREFIX_CURRENT_USER + userName, user, Long.parseLong(refreshTokenExpireTime));

        // 停用APP端
        // 清除Shiror登录信息缓存
        if (redisService.exists(Constant.PREFIX_SHIRO_APP_REFRESH_TOKEN + userName)) {
            redisService.remove(Constant.PREFIX_SHIRO_APP_REFRESH_TOKEN + userName);
        }
        // 清除refreshToken过渡时间信息缓存
        if (redisService.exists(Constant.PREFIX_SHIRO_APP_REFRESH_TOKEN_TRANSITION + userName)) {
            redisService.remove(Constant.PREFIX_SHIRO_APP_REFRESH_TOKEN_TRANSITION + userName);
        }
        // 将该用户<停用>信息存入redis，该用户下次请求将提示用户已停用
        redisService.set(Constant.PREFIX_APP_CURRENT_USER + userName, user, Long.parseLong(refreshTokenExpireTime));

        // 停用微信端
        // 清除Shiror登录信息缓存
        if (redisService.exists(Constant.PREFIX_SHIRO_WECHAT_REFRESH_TOKEN + userName)) {
            redisService.remove(Constant.PREFIX_SHIRO_WECHAT_REFRESH_TOKEN + userName);
        }
        // 清除refreshToken过渡时间信息缓存
        if (redisService.exists(Constant.PREFIX_SHIRO_WECHAT_REFRESH_TOKEN_TRANSITION + userName)) {
            redisService.remove(Constant.PREFIX_SHIRO_WECHAT_REFRESH_TOKEN_TRANSITION + userName);
        }
        // 将该用户<停用>信息存入redis，该用户下次请求将提示用户已停用
        redisService.set(Constant.PREFIX_WECHAT_CURRENT_USER + userName, user, Long.parseLong(refreshTokenExpireTime));
    }
}
