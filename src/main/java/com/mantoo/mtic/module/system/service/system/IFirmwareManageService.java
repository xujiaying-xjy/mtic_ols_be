package com.mantoo.mtic.module.system.service.system;

import com.mantoo.mtic.module.system.entity.system.FirmwareManage;
import com.mantoo.mtic.module.system.entity.system.MaterialManage;
import com.mantoo.mtic.module.system.entity.system.ProductManage;
import com.mantoo.mtic.module.system.entity.vo.FirmwareManageVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @ClassName: IFirmwareManageService
 * @Author: ghy
 * @Date: 2021-04-20 10:05
 */
public interface IFirmwareManageService {


    /*** 
    * @Description: 上传文件并将路径返回 
    * @Param: [file, firmwareManage] 
    * @return: java.lang.String 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    String firmwareManageFileUrl(MultipartFile file,FirmwareManage firmwareManage);
    
    /*** 
    * @Description: 条件查询所有固件 
    * @Param: [firmwareManage] 
    * @return: java.util.List<com.mantoo.mtic.module.system.entity.system.FirmwareManage> 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    List<FirmwareManageVO> findByCondition(FirmwareManage firmwareManage);

    /*** 
    * @Description: 新增固件 
    * @Param: [firmwareManage] 
    * @return: java.lang.Integer 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    Integer addFirmwareManage(FirmwareManage firmwareManage);

    /*** 
    * @Description: 修改固件 
    * @Param: [firmwareManage] 
    * @return: java.lang.Integer 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    Integer editFirmwareManage(FirmwareManage firmwareManage);

    /*** 
    * @Description: 删除固件
    * @Param: [firmwareManage] 
    * @return: java.lang.Integer 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    Integer deleteFirmwareManage(FirmwareManage firmwareManage);

    /*** 
    * @Description: 判断固件是否存在
    * @Param: [firmwareManage] 
    * @return: java.lang.Boolean 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    Boolean firmwareManageRepeat(FirmwareManage firmwareManage);

    /***
    * @Description: 根据产品id查询固件
    * @Param: [id]
    * @return: java.util.List<com.mantoo.mtic.module.system.entity.system.FirmwareManage>
    * @Author: ghy
    * @Date: 2021/4/25
    */
    List<FirmwareManage> getFirmwareManageByProductMange(Long id);
}
