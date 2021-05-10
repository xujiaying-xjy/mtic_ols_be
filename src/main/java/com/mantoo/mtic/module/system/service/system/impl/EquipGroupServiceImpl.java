package com.mantoo.mtic.module.system.service.system.impl;


import com.github.pagehelper.PageHelper;
import com.mantoo.mtic.common.utils.DateUtils;
import com.mantoo.mtic.common.utils.UserUtil;
import com.mantoo.mtic.exception.ErrorInfo;
import com.mantoo.mtic.exception.MticException;
import com.mantoo.mtic.module.system.entity.SysUser;
import com.mantoo.mtic.module.system.entity.equip.EquipGroup;
import com.mantoo.mtic.module.system.entity.equip.EquipGroupCustomer;
import com.mantoo.mtic.module.system.exMapper.ExSysUserMapper;
import com.mantoo.mtic.module.system.exMapper.equip.EquipGroupCustomerMapper;
import com.mantoo.mtic.module.system.exMapper.equip.EquipGroupMapper;
import com.mantoo.mtic.module.system.service.system.IEquipGroupService;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

@Service
@Transactional
public class EquipGroupServiceImpl implements IEquipGroupService {

    @Resource
    EquipGroupMapper equipGroupMapper;

    @Resource
    EquipGroupCustomerMapper equipGroupCustomerMapper;

    @Resource
    ExSysUserMapper exSysUserMapper;


    /**
     * @param equipGroup
     * @return List<EquipGroup>
     * @Description 按照条件模糊查询产品
     */
    @Override
    public List<EquipGroup> findByEquipGroupCondition(EquipGroup equipGroup) {
        //判断页码和记录数是否为空
        if (!Objects.isNull(equipGroup.getPageNum()) && !Objects.isNull(equipGroup.getPageSize())) {
            PageHelper.startPage(equipGroup.getPageNum(), equipGroup.getPageSize());
        }
        List<EquipGroup> list = equipGroupMapper.selectEquipGroupListWithCondition(equipGroup);
        for (EquipGroup e : list) {
            List<SysUser> sysUserList = equipGroupMapper.selectByEquipGroupCustomerId(e.getEquipGroupId());
            List<String> userNameList= new ArrayList<>();
            List<String> userIdList= new ArrayList<>();
            for (SysUser sysUser:sysUserList) {
                userNameList.add(sysUser.getName());
                userIdList.add(String.valueOf(sysUser.getUserId()));
            }
            e.setCustomerName(String.join(",", userNameList));
            e.setCustomerId(String.join(",", userIdList));
        }
        return list;

    }

    /**
     * 新增设备组
     *
     * @param equipGroup
     * @param equipGroupId
     * @return
     */
    @Override
    public Integer addEquipGroup(EquipGroup equipGroup, Long equipGroupId) {
        SysUser user = UserUtil.getUser();

        EquipGroup equipGroupName = getEquipByEquipName(equipGroup.getEquipGroupName());
        if (equipGroupName != null) {
            throw new MticException("设备组名称已存在", ErrorInfo.USER_EXIST.getCode());
        }

        EquipGroup equipGroupCode = getEquipByEquipCode(equipGroup.getEquipGroupCode());
        if (equipGroupCode != null) {
            throw new MticException("设备组编号已存在", ErrorInfo.USER_EXIST.getCode());
        }
        //默认生产状态为0
        equipGroup.setDeleteFlag(0);
        //添加产品的创建时间
        equipGroup.setCreateTime(new Date());
        //添加产品的更新时间和更新者信息
        equipGroup.setCreateBy(user.getUserId());
        equipGroup.setUpdateTime(new Date());
        equipGroup.setUpdateBy(user.getUserId());
        return equipGroupMapper.insert(equipGroup);
    }

    /**
     * 修改设备组
     *
     * @param equipGroup
     * @param equipGroupId
     * @return
     */
    @Override
    public Integer updateEquipGroup(EquipGroup equipGroup, Long equipGroupId) {
        SysUser user = UserUtil.getUser();
        //判断设备组名称是否重复
        Example exampleName = new Example(EquipGroup.class);
        Example.Criteria criteriaName = exampleName.createCriteria();
        criteriaName.andEqualTo("equipGroupName", equipGroup.getEquipGroupName());
        criteriaName.andNotEqualTo("equipGroupId", equipGroup.getEquipGroupId());
        criteriaName.andEqualTo("deleteFlag", SysUser.DELETE_FLAG_NORMAL);
        List<EquipGroup> equipGroupNameList = equipGroupMapper.selectByExample(exampleName);
        if (!CollectionUtils.isEmpty(equipGroupNameList)) {
            throw new MticException("设备组名称已存在", ErrorInfo.USER_EXIST.getCode());
        }
        //判断设备组编号是否重复
        Example exampleCode = new Example(EquipGroup.class);
        Example.Criteria criteriaCode = exampleCode.createCriteria();
        criteriaCode.andEqualTo("equipGroupCode", equipGroup.getEquipGroupCode());
        criteriaCode.andNotEqualTo("equipGroupId", equipGroup.getEquipGroupId());
        criteriaCode.andEqualTo("deleteFlag", SysUser.DELETE_FLAG_NORMAL);
        List<EquipGroup> equipGroupCodeList = equipGroupMapper.selectByExample(exampleCode);
        if (!CollectionUtils.isEmpty(equipGroupCodeList)) {
            throw new MticException("设备组编号已存在", ErrorInfo.USER_EXIST.getCode());
        }
        //添加产品的更新时间和更新者信息
        equipGroup.setUpdateBy(user.getUserId());
        equipGroup.setUpdateTime(new Date());
        return equipGroupMapper.updateByPrimaryKeySelective(equipGroup);
    }

    /**
     * 删除设备组, 不是真正删除，而是将标志改为1
     *
     * @param equipGroup
     * @return
     * @Description
     */
    @Override
    public int delEquipGroup(EquipGroup equipGroup, Long userId) {
        equipGroup.setUpdateBy(userId);
        equipGroup.setUpdateTime(DateUtils.getNowDate());
        equipGroup.setDeleteFlag(EquipGroup.DELETE_FLAG_DELETED);
        return equipGroupMapper.updateByPrimaryKeySelective(equipGroup);
    }

    /**
     * 根据设备组名称查询设备组
     *
     * @param equipName
     * @return
     */
    @Override
    public EquipGroup getEquipByEquipName(String equipName) {
        EquipGroup equipGroup = new EquipGroup();
        equipGroup.setEquipGroupName(equipName);
        equipGroup.setDeleteFlag(EquipGroup.DELETE_FLAG_NORMAL);
        EquipGroup equipGroupName;
        try {
            equipGroupName = equipGroupMapper.selectOne(equipGroup);
        } catch (Exception e) {
            throw new MticException(ErrorInfo.EXIST_MORE.getMsg(), ErrorInfo.EXIST_MORE.getCode());
        }

        return equipGroupName;
    }

    /**
     * 根据设备组编号查询设备组
     *
     * @param equipCode
     * @return
     */
    @Override
    public EquipGroup getEquipByEquipCode(String equipCode) {
        EquipGroup equipGroup = new EquipGroup();
        equipGroup.setEquipGroupCode(equipCode);
        equipGroup.setDeleteFlag(EquipGroup.DELETE_FLAG_NORMAL);
        EquipGroup equipGroupCode;
        try {
            equipGroupCode = equipGroupMapper.selectOne(equipGroup);
        } catch (Exception e) {
            throw new MticException(ErrorInfo.EXIST_MORE.getMsg(), ErrorInfo.EXIST_MORE.getCode());
        }
        return equipGroupCode;
    }


    /**
     * 关联客户
     *
     * @param customerIdList
     * @param equipGroupId
     */
    @Override
    public void setEquipGroupCustomer(List<Long> customerIdList, Long equipGroupId) {
        EquipGroupCustomer delEquipGroupId = new EquipGroupCustomer();
        delEquipGroupId.setEquipGroupId(equipGroupId);
        equipGroupCustomerMapper.delete(delEquipGroupId);
        if (!CollectionUtils.isEmpty(customerIdList)) {
            List<EquipGroupCustomer> equipGroupCustomerList = new ArrayList<>();
            for (long customerId : customerIdList) {
                EquipGroupCustomer equipGroup = new EquipGroupCustomer();
                equipGroup.setEquipGroupId(equipGroupId);
                equipGroup.setCustomerId(customerId);
                equipGroupCustomerList.add(equipGroup);
            }
            equipGroupCustomerMapper.insertList(equipGroupCustomerList);
        }
    }

    /**
     * 查询是漫途负责人还是漫途客户
     *
     * @param type
     * @return
     */
    @Override
    public List<SysUser> findByEquipGroupCustomerCondition(int type) {
        SysUser sysUserCustomer = new SysUser();
        sysUserCustomer.setUserType(type);
        List<SysUser> sysUserList = exSysUserMapper.select(sysUserCustomer);
        return sysUserList;
    }

    /**
     * @param equipGroup
     * @return 重复返回true，不重复返回false
     * @Description 判断产品是否重复
     */
    @Override
    public Boolean equipGroupNameIsRepeatable(EquipGroup equipGroup) {
        //如果不重复返回false
        if (!Objects.isNull(equipGroupMapper.selectEquipGroupByEquipGroupCode(equipGroup))) {
            return true;
        } else {
            return false;
        }
    }

}
