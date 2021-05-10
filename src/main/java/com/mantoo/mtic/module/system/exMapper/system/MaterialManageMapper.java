package com.mantoo.mtic.module.system.exMapper.system;

import com.mantoo.mtic.common.mapper.MyMapper;
import com.mantoo.mtic.module.system.entity.system.FirmwareManage;
import com.mantoo.mtic.module.system.entity.system.MaterialManage;
import com.mantoo.mtic.module.system.entity.vo.MaterialManageVO;

import java.util.List;

public interface MaterialManageMapper extends MyMapper<MaterialManage> {
    /*** 
    * @Description: 条件模糊查询资料 
    * @Param: [materialManage] 
    * @return: java.util.List<com.mantoo.mtic.module.system.entity.system.MaterialManage> 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    List<MaterialManageVO> selectMaterialManageListWithCondition(MaterialManage materialManage);

    /*** 
    * @Description: 判断资料是否已经存在 
    * @Param: [materialManage] 
    * @return: java.util.List<com.mantoo.mtic.module.system.entity.system.MaterialManage> 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    List<MaterialManage> materialManageRepeat(MaterialManage materialManage);

    /***
    * @Description: 删除资料 
    * @Param: [materialManage] 
    * @return: java.lang.Integer 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    Integer deleteMaterialManage(MaterialManage materialManage);

//    List<MaterialManage> getFirmwareManageByProductMange(Long productId);
}