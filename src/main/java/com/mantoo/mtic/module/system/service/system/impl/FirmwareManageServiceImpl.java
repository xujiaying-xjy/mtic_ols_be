package com.mantoo.mtic.module.system.service.system.impl;

import com.auth0.jwt.JWT;
import com.github.pagehelper.PageHelper;
import com.mantoo.mtic.common.utils.FileUtils;
import com.mantoo.mtic.common.utils.StringUtil;
import com.mantoo.mtic.common.utils.UserUtil;
import com.mantoo.mtic.conf.shiro.Constant;
import com.mantoo.mtic.module.system.data.FilePathConfig;
import com.mantoo.mtic.module.system.entity.SysUser;
import com.mantoo.mtic.module.system.entity.system.FirmwareManage;
import com.mantoo.mtic.module.system.entity.system.MaterialManage;
import com.mantoo.mtic.module.system.entity.system.ProductManage;
import com.mantoo.mtic.module.system.entity.vo.FirmwareManageVO;
import com.mantoo.mtic.module.system.exMapper.system.FirmwareManageMapper;
import com.mantoo.mtic.module.system.exMapper.system.ProductManageMapper;
import com.mantoo.mtic.module.system.mapper.SysUserMapper;
import com.mantoo.mtic.module.system.service.system.IFirmwareManageService;
import me.chanjar.weixin.mp.bean.card.Sku;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName: FirmwareManageServiceImpl
 * @Author: ghy
 * @Date: 2021-04-20 10:06
 */
@Service
@Transactional
public class FirmwareManageServiceImpl implements IFirmwareManageService {

    @Autowired
    FilePathConfig filePathConfig;

    @Autowired
    FirmwareManageMapper firmwareManageMapper;

    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    ProductManageMapper productManageMapper;

    /***
    * @Description: 上传文件并且返回文件url
    * @Param: [file, firmwareManage]
    * @return: java.lang.String
    * @Author: ghy
    * @Date: 2021/4/23
    */
    @Override
    public String firmwareManageFileUrl(MultipartFile file,FirmwareManage firmwareManage) {
        //根据ID查找产品名称
        ProductManage productManage = productManageMapper.selectByPrimaryKey(firmwareManage.getProductId());
        //文件名为所属产品名称+固件版本号
        String fileName = productManage.getProductCode()+"_"+firmwareManage.getFirmwareVersion().replaceAll("[^0-9]", "")+ file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));;
        //获得文件名和路径，
        String winsPath = filePathConfig.getWinsPath() + filePathConfig.getWinsFirmwarePath();
        String linuxPath = filePathConfig.getLinuxPath() + filePathConfig.getLinuxFirmwarePath();
        System.out.println(winsPath);
        //上传文件
        FileUtils.mticSaveFile(file, winsPath, linuxPath, fileName);
        String fileUrl = "";
        String line = File.separator;
        if ("\\".equals(line)) {
            fileUrl = filePathConfig.getWinsFirmwarePath() + line + fileName;
        } else if ("/".equals(line)) {
            fileUrl = filePathConfig.getLinuxFirmwarePath() + line + fileName;
        }
        //返回文件路径
        return fileUrl;
    }


    /***
    * @Description: 条件模糊查询固件
    * @Param: [firmwareManage]
    * @return: java.util.List<com.mantoo.mtic.module.system.entity.system.FirmwareManage>
    * @Author: ghy
    * @Date: 2021/4/23
    */
    @Override
    public List<FirmwareManageVO> findByCondition(FirmwareManage firmwareManage) {
        //判断页码和记录数是否为空
        if (!Objects.isNull( firmwareManage.getPageNum())&&!Objects.isNull(firmwareManage.getPageSize())) {
            PageHelper.startPage(firmwareManage.getPageNum(), firmwareManage.getPageSize());
        }
        List<FirmwareManageVO> list = firmwareManageMapper.selectFirmwareManageListWithCondition(firmwareManage);
        list.forEach(
                x->{
                    x.setFirmwareFile(StringUtil.convertFeUrl(x.getFirmwareFile()));
                }
        );
        return list;
    }

    /*** 
    * @Description: 新增固件
    * @Param: [firmwareManage] 
    * @return: java.lang.Integer 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    @Override
    public Integer addFirmwareManage(FirmwareManage firmwareManage) {
        //默认生产状态为0
        firmwareManage.setDeleteFlag(0);
        //添加产品的创建时间和创建者信息
        firmwareManage.setCreateTime(new Date());
        firmwareManage.setCreateBy(UserUtil.getUserId());
        //添加产品的更新时间和更新者信息
        firmwareManage.setUpdateTime(new Date());
        firmwareManage.setUpdateBy(UserUtil.getUserId());
        //将新的固件信息插入到数据表中
        return firmwareManageMapper.insert(firmwareManage);
    }

   /***
   * @Description: 修改固件
   * @Param: [firmwareManage]
   * @return: java.lang.Integer
   * @Author: ghy
   * @Date: 2021/4/23
   */
    @Override
    public Integer editFirmwareManage(FirmwareManage firmwareManage) {
        //添加产品的更新时间和更新者信息
        firmwareManage.setUpdateTime(new Date());
        firmwareManage.setUpdateBy(UserUtil.getUserId());
        firmwareManage.setDeleteFlag(0);
        return firmwareManageMapper.updateByPrimaryKeySelective(firmwareManage);
    }

    /***
    * @Description: 删除固件，将deleteFlag改为1
    * @Param: [firmwareManage]
    * @return: java.lang.Integer
    * @Author: ghy
    * @Date: 2021/4/23
    */
    @Override
    public Integer deleteFirmwareManage(FirmwareManage firmwareManage) {
        //添加产品的更新时间和更新者信息
        firmwareManage.setUpdateTime(new Date());
        firmwareManage.setUpdateBy(UserUtil.getUserId());
        return firmwareManageMapper.deleteFirmManage(firmwareManage);
    }

    /*** 
    * @Description: 查询固件是否重复，如果重复返回true,不重复返回false
    * @Param: [firmwareManage] 
    * @return: java.lang.Boolean 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    @Override
    public Boolean firmwareManageRepeat(FirmwareManage firmwareManage) {
        List<FirmwareManage> list = firmwareManageMapper.firmManageRepeat(firmwareManage);
        //list大于0则表示重复
        if(list.size()==0){
            return false;
        //如果大于1则表示查询条件不足，查出了多个结果
        }else if(list.size()>1){
            return false;
        }else{
            return true;
        }
    }

    /*** 
    * @Description: 根据产品id获取固件
    * @Param: [id] 
    * @return: java.util.List<com.mantoo.mtic.module.system.entity.system.FirmwareManage> 
    * @Author: ghy
    * @Date: 2021/4/25 
    */
    @Override
    public List<FirmwareManage> getFirmwareManageByProductMange(Long id) {
        return firmwareManageMapper.getFirmwareManageByProductMange(id);
    }

}
