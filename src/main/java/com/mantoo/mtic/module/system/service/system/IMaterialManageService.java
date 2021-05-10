package com.mantoo.mtic.module.system.service.system;

import com.mantoo.mtic.module.system.entity.system.FirmwareManage;
import com.mantoo.mtic.module.system.entity.system.MaterialManage;
import com.mantoo.mtic.module.system.entity.vo.MaterialManageVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @ClassName: IMaterialManageService
 * @Author: ghy
 * @Date: 2021-04-22 13:35
 */
public interface IMaterialManageService {
    /*** 
    * @Description: 条件模糊查询资料 
    * @Param: [materialManage] 
    * @return: java.util.List<com.mantoo.mtic.module.system.entity.system.MaterialManage> 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    List<MaterialManageVO>  findByCondition(MaterialManage materialManage);

    /*** 
    * @Description: 判断资料是否重复 
    * @Param: [materialManage] 
    * @return: java.lang.Boolean 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    Boolean materialManageRepeat(MaterialManage materialManage);

    /***
    * @Description: 上传文件并返回路径
    * @Param: [file, materialManage]
    * @return: java.lang.String
    * @Author: ghy
    * @Date: 2021/4/23
    */
    String materialManageFileUrl(MultipartFile file,MaterialManage materialManage);

    /*** 
    * @Description: 添加资料 
    * @Param: [materialManage] 
    * @return: java.lang.Integer 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    Integer addMaterialManage(MaterialManage materialManage);

    /*** 
    * @Description: 修改资料 
    * @Param: [materialManage] 
    * @return: java.lang.Integer 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    Integer editMaterialManage(MaterialManage materialManage);

    /*** 
    * @Description: 删除资料
    * @Param: [materialManage] 
    * @return: java.lang.Integer 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    Integer deleteMaterialManage(MaterialManage materialManage);



    String  updateFileName(MaterialManage materialManage);
}
