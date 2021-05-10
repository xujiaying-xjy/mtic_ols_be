package com.mantoo.mtic.module.system.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.mantoo.mtic.common.utils.FileUtils;
import com.mantoo.mtic.common.utils.StringUtil;
import com.mantoo.mtic.common.utils.UserUtil;
import com.mantoo.mtic.module.system.data.FilePathConfig;
import com.mantoo.mtic.module.system.entity.system.FirmwareManage;
import com.mantoo.mtic.module.system.entity.system.MaterialManage;
import com.mantoo.mtic.module.system.entity.system.ProductManage;
import com.mantoo.mtic.module.system.entity.vo.MaterialManageVO;
import com.mantoo.mtic.module.system.exMapper.system.MaterialManageMapper;
import com.mantoo.mtic.module.system.service.system.IMaterialManageService;
import lombok.extern.slf4j.Slf4j;
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
 * @ClassName: MaterialManageServiceImpl
 * @Author: ghy
 * @Date: 2021-04-22 13:35
 */
@Slf4j
@Service
@Transactional
public class MaterialManageServiceImpl implements IMaterialManageService {

    @Autowired
    FilePathConfig filePathConfig;

    @Autowired
    private MaterialManageMapper materialManageMapper;

    /*** 
    * @Description: 条件模糊查询 
    * @Param: [materialManage] 
    * @return: java.util.List<com.mantoo.mtic.module.system.entity.system.MaterialManage> 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    @Override
    public List<MaterialManageVO> findByCondition(MaterialManage materialManage) {
        //判断页码和记录数是否为空
        if (!Objects.isNull( materialManage.getPageNum())&&!Objects.isNull(materialManage.getPageSize())) {
            PageHelper.startPage(materialManage.getPageNum(), materialManage.getPageSize());
        }
        List<MaterialManageVO> list = materialManageMapper.selectMaterialManageListWithCondition(materialManage);
        //循环遍历替换斜杠
        list.forEach(
                x->{
                    x.setMaterialFile(StringUtil.convertFeUrl(x.getMaterialFile()));
                }
        );
        return list;
    }

    /*** 
    * @Description: 判断是否重复 
    * @Param: [materialManage] 
    * @return: java.lang.Boolean 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    @Override
    public Boolean materialManageRepeat(MaterialManage materialManage) {
        List<MaterialManage> list = materialManageMapper.materialManageRepeat(materialManage);
        //查询出来一个则表示已存在，查询出来大于2则表示查询条件不足
        if(list.size()==0){
            return false;
        }else if(list.size()>1){
            return false;
        }else{
            return true;
        }
    }

    /*** 
    * @Description: 上传文件并返回文件url 
    * @Param: [file, materialManage] 
    * @return: java.lang.String 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    @Override
    public String materialManageFileUrl(MultipartFile file, MaterialManage materialManage) {
        //文件名为所属产品名称+固件版本号
        String fileName = materialManage.getMaterialName()+file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));;
        System.out.println(fileName);
        //获得路径
        String winsPath = filePathConfig.getWinsPath() + filePathConfig.getWinsMaterialPath();
        String linuxPath = filePathConfig.getLinuxPath() + filePathConfig.getLinuxMaterialPath();
        //上传文件到服务器
        FileUtils.mticSaveFile(file, winsPath, linuxPath, fileName);
        String fileUrl = "";
        String line = File.separator;
        if ("\\".equals(line)) {
            fileUrl = filePathConfig.getWinsMaterialPath() + line + fileName;
        } else if ("/".equals(line)) {
            fileUrl = filePathConfig.getLinuxMaterialPath() + line + fileName;
        }
//        String url = StringUtil.convertFeUrl(fileUrl);

        return fileUrl;
    }

    /*** 
    * @Description: 添加资料 
    * @Param: [materialManage] 
    * @return: java.lang.Integer 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    @Override
    public Integer addMaterialManage(MaterialManage materialManage) {
        //默认生产状态为0
        materialManage.setDeleteFlag(0);
        //添加资料的创建时间和创建者信息
        materialManage.setCreatedTime(new Date());
        materialManage.setCreatedBy(UserUtil.getUserId());
        //添加资料的更新时间和更新者信息
        materialManage.setUpdatedTime(new Date());
        materialManage.setUpdatedBy(UserUtil.getUserId());
        //将新的资料信息插入到数据表中
        return materialManageMapper.insert(materialManage);
    }

    /*** 
    * @Description: 修改资料 
    * @Param: [materialManage] 
    * @return: java.lang.Integer 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    @Override
    public Integer editMaterialManage(MaterialManage materialManage) {
        //添加资料的更新时间和更新者信息
        materialManage.setUpdatedTime(new Date());
        materialManage.setUpdatedBy(UserUtil.getUserId());
        materialManage.setDeleteFlag(0);
        materialManage.setMaterialFile(materialManage.getMaterialFile());
        return materialManageMapper.updateByPrimaryKeySelective(materialManage);
    }

    /*** 
    * @Description: 删除资料
    * @Param: [materialManage] 
    * @return: java.lang.Integer 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    @Override
    public Integer deleteMaterialManage(MaterialManage materialManage) {
        //添加产品的更新时间和更新者信息
        materialManage.setUpdatedTime(new Date());
        materialManage.setUpdatedBy(UserUtil.getUserId());
        return materialManageMapper.deleteMaterialManage(materialManage);
    }

    @Override
    public String updateFileName(MaterialManage materialManage) {
        //资料名称，需要将文件名修改为资料名称
        String materialName = materialManage.getMaterialName();

        String dbfilePath = "";
        String dbline = File.separator;
        MaterialManage materialInfo = materialManageMapper.selectByPrimaryKey(materialManage);
        String filePath = materialInfo.getMaterialFile().substring(1);
        if ("\\".equals(dbline)) {
            dbfilePath =StringUtil.convertFeUrl (filePathConfig.getWinsPath() + dbline + filePath);
        } else if ("/".equals(dbline)) {
            dbfilePath = filePathConfig.getLinuxPath() + dbline + filePath;
        }
        //修改文件名
        if(!Objects.isNull(materialName)){
            //如果不相同，进行修改
            if(!materialName.equals(materialInfo.getMaterialName())){
                //原始文件地址，即数据库中的文件地址
                File dbFile=new File(dbfilePath);
                //新文件地址
                String newFilePath = dbfilePath.substring(0, dbfilePath.lastIndexOf("/")) + "/" + materialName+".pdf";
                File newFile=new File(newFilePath);
                //文件重命名
                if(dbFile.renameTo(newFile))
                {
                    log.info("修改成功");
                }
                else
                {
                    log.error("修改失败");
                }
            }
        }
        return filePathConfig.getWinsMaterialPath() + dbline + materialName+".pdf";
    }

}
