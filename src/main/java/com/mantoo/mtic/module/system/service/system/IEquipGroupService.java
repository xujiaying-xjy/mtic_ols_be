package com.mantoo.mtic.module.system.service.system;



import com.mantoo.mtic.module.system.entity.SysUser;
import com.mantoo.mtic.module.system.entity.equip.EquipGroup;

import java.util.List;

public interface IEquipGroupService {


    /**
     * 条件模糊查询设备组
     * @param equipGroup
     * @return
     */
    List<EquipGroup> findByEquipGroupCondition(EquipGroup equipGroup);

    /**
     * 新增设备组
     * @param EquipGroup
     * @param equipGroupId
     * @return
     */
    Integer addEquipGroup(EquipGroup EquipGroup, Long equipGroupId);

    /**
     * 修改设备组信息
     * @param EquipGroup
     * @param equipGroupId
     * @return
     */
    Integer updateEquipGroup(EquipGroup EquipGroup, Long equipGroupId);

    /**
     * 判断设备组是否重复
     * @param equipGroup
     * @return
     */
    Boolean equipGroupNameIsRepeatable(EquipGroup equipGroup);

    /**
     * 删除设备组
     * @param EquipGroup
     * @param userId
     * @return
     */
    int delEquipGroup(EquipGroup EquipGroup, Long userId);

    /**
     * 根据设备组名称查询设备组
     * @param equipName
     * @return
     */
    EquipGroup getEquipByEquipName(String equipName);

    /**
     * 根据设备组编号查询设备组
     * @param equipCode
     * @return
     */
    EquipGroup getEquipByEquipCode(String equipCode);


    /**
     *关联客户
     * @param customerIdList
     * @param equipGroupId
     */
    void setEquipGroupCustomer(List<Long> customerIdList, Long equipGroupId);


    /**
     *  查询客户
     * @param type
     * @return
     */
    List<SysUser> findByEquipGroupCustomerCondition(int type);


}
