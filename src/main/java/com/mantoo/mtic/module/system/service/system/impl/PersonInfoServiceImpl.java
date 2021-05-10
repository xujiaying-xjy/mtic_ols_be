package com.mantoo.mtic.module.system.service.system.impl;

import com.mantoo.mtic.common.utils.AesCipherUtil;
import com.mantoo.mtic.common.utils.FileUtils;
import com.mantoo.mtic.common.utils.StringUtil;
import com.mantoo.mtic.conf.redis.RedisService;
import com.mantoo.mtic.conf.shiro.Constant;
import com.mantoo.mtic.exception.ErrorInfo;
import com.mantoo.mtic.exception.MticException;
import com.mantoo.mtic.module.system.data.ExSysUser;
import com.mantoo.mtic.module.system.data.FilePathConfig;
import com.mantoo.mtic.module.system.entity.SysUser;
import com.mantoo.mtic.module.system.mapper.SysUserMapper;
import com.mantoo.mtic.module.system.service.system.IPersonInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @ClassName: PersonInfoServiceImpl
 * @Description: 个人信息service
 * @Author: renjt
 * @Date: 2019-11-29 14:44
 */
@Service
@Transactional
public class PersonInfoServiceImpl implements IPersonInfoService {

    @Autowired
    FilePathConfig filePathConfig;
    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    RedisService redisService;

    /**
     * @return java.lang.String
     * @Description 上传个人头像
     * @Param [userImg]
     * @Author renjt
     * @Date 2019-11-29 14:43
     */
    @Override
    public String updateUserImg(MultipartFile userImg, Long userId) {
        // 上传附件
        String fileName = FileUtils.generateFileName(userImg);
        String winsPath = filePathConfig.getWinsPath() + filePathConfig.getWinsUserImgPath();
        String linuxPath = filePathConfig.getLinuxPath() + filePathConfig.getLinuxUserImgPath();
        FileUtils.mticSaveFile(userImg, winsPath, linuxPath, fileName);
        // 更新用户表
        String userImgUrl = "";
        String line = File.separator;
        if ("\\".equals(line)) {
            userImgUrl = filePathConfig.getWinsUserImgPath() + line + fileName;
        } else if ("/".equals(line)) {
            userImgUrl = filePathConfig.getLinuxUserImgPath() + line + fileName;
        }
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setUserImgUrl(userImgUrl);
        sysUserMapper.updateByPrimaryKeySelective(sysUser);
        return StringUtil.convertFeUrl(userImgUrl);
    }

    /**
     * @return java.lang.String
     * @Description 修改密码
     * @Param [exSysUser, userId]
     * @Author renjt
     * @Date 2020-4-6 16:45
     */
    @Override
    public void updatePassWord(ExSysUser exSysUser, Long userId) {
        // 查询数据库中原密码
        SysUser existUser = sysUserMapper.selectByPrimaryKey(userId);
        // 将原密码进行AES解密
        String key = AesCipherUtil.deCrypto(existUser.getPassword());
        // 因为密码加密是以帐号+密码的形式进行加密的，所以解密后的对比是帐号+密码
        String userName = existUser.getUserName();
        if (key.equals(userName + exSysUser.getOldPassWord())) {
            // 密码以帐号+密码的形式进行AES加密
            String newPassword = AesCipherUtil.enCrypto(existUser.getUserName() + exSysUser.getPassword());
            SysUser sysUser = new SysUser();
            sysUser.setUserId(userId);
            sysUser.setPassword(newPassword);
            sysUserMapper.updateByPrimaryKeySelective(sysUser);
            // 清除当前用户信息，需要重新登录
            // 清除Shiro权限信息缓存
            if (redisService.exists(Constant.PREFIX_SHIRO_CACHE + userName)) {
                redisService.remove(Constant.PREFIX_SHIRO_CACHE + userName);
            }
            // 清除Shiror登录信息缓存
            if (redisService.exists(Constant.PREFIX_SHIRO_REFRESH_TOKEN + userName)) {
                redisService.remove(Constant.PREFIX_SHIRO_REFRESH_TOKEN + userName);
                redisService.remove(Constant.PREFIX_SHIRO_APP_REFRESH_TOKEN + userName);
                redisService.remove(Constant.PREFIX_SHIRO_WECHAT_REFRESH_TOKEN + userName);
            }
            // 清除refreshToken过渡时间信息缓存
            if (redisService.exists(Constant.PREFIX_SHIRO_REFRESH_TOKEN_TRANSITION + userName)) {
                redisService.remove(Constant.PREFIX_SHIRO_REFRESH_TOKEN_TRANSITION + userName);
                redisService.remove(Constant.PREFIX_SHIRO_APP_REFRESH_TOKEN_TRANSITION + userName);
                redisService.remove(Constant.PREFIX_SHIRO_WECHAT_REFRESH_TOKEN_TRANSITION + userName);
            }
            // 清除当前用户信息缓存
            if (redisService.exists(Constant.PREFIX_CURRENT_USER + userName)) {
                redisService.remove(Constant.PREFIX_CURRENT_USER + userName);
                redisService.remove(Constant.PREFIX_APP_CURRENT_USER + userName);
                redisService.remove(Constant.PREFIX_WECHAT_CURRENT_USER + userName);
            }
        } else {
            throw new MticException("请输入正确的原密码！", ErrorInfo.LOGIN_INFO_ERROR.getCode());
        }
    }
}