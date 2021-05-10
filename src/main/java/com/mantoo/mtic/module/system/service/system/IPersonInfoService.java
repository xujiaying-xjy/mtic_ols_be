package com.mantoo.mtic.module.system.service.system;

import com.mantoo.mtic.module.system.data.ExSysUser;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName: IPersonInfoService
 * @Description: 个人信息
 * @Author: renjt
 * @Date: 2019-11-29 14:42
 */
public interface IPersonInfoService {

    /**
     * @return java.lang.String
     * @Description 上传个人头像
     * @Param [userImg]
     * @Author renjt
     * @Date 2019-11-29 14:43
     */
    String updateUserImg(MultipartFile userImg, Long userId);
    
    /**
     * @return java.lang.String
     * @Description 修改密码
     * @Param [exSysUser, userId]
     * @Author renjt
     * @Date 2020-4-6 16:45
     */
    void updatePassWord(ExSysUser exSysUser, Long userId);
}
