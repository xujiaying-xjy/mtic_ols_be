package com.mantoo.mtic.module.system.exMapper.equip;

import com.mantoo.mtic.common.mapper.MyMapper;
import com.mantoo.mtic.module.system.entity.SysUser;
import com.mantoo.mtic.module.system.entity.equip.EquipGroup;


import java.util.List;

public interface EquipGroupMapper extends MyMapper<EquipGroup> {

    /**
     * 条件模糊查询设备组信息
     * @param equipGroup
     * @return
     */
    List<EquipGroup> selectEquipGroupListWithCondition(EquipGroup equipGroup);

    /**
     * 查询关联客户名称
     * @param equipGroupId
     * @return
     */
    List<SysUser> selectByEquipGroupCustomerId(Long equipGroupId);

    /**
     * 判断是否重复
     * @param equipGroup
     * @return
     */
    Integer selectEquipGroupByEquipGroupCode(EquipGroup equipGroup);



}