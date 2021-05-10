package com.mantoo.mtic.module.system.exMapper.system;

import com.mantoo.mtic.common.mapper.MyMapper;
import com.mantoo.mtic.module.system.entity.system.FirmwareManage;
import com.mantoo.mtic.module.system.entity.vo.FirmwareManageVO;

import java.util.List;


public interface FirmwareManageMapper extends MyMapper<FirmwareManage> {

    /***
    * @Description: 条件模糊查询固件
    * @Param: [firmwareManage]
    * @return: java.util.List<com.mantoo.mtic.module.system.entity.system.FirmwareManage>
    * @Author: ghy
    * @Date: 2021/4/23
    */
    List<FirmwareManageVO>  selectFirmwareManageListWithCondition(FirmwareManage firmwareManage);

    /*** 
    * @Description: 删除固件 
    * @Param: [firmwareManage] 
    * @return: java.lang.Integer 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    Integer deleteFirmManage(FirmwareManage firmwareManage);

    /*** 
    * @Description: 判断固件是否已经存在
    * @Param: [firmwareManage] 
    * @return: java.util.List<com.mantoo.mtic.module.system.entity.system.FirmwareManage> 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    List<FirmwareManage> firmManageRepeat(FirmwareManage firmwareManage);


    List<FirmwareManage> getFirmwareManageByProductMange(Long id);
}