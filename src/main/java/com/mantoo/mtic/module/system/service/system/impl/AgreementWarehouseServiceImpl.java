package com.mantoo.mtic.module.system.service.system.impl;


import com.github.pagehelper.PageHelper;
import com.mantoo.mtic.common.utils.DateUtils;
import com.mantoo.mtic.common.utils.UserUtil;
import com.mantoo.mtic.exception.ErrorInfo;
import com.mantoo.mtic.exception.MticException;
import com.mantoo.mtic.module.system.entity.SysUser;
import com.mantoo.mtic.module.system.entity.agreement.AgreementMeasuredValues;
import com.mantoo.mtic.module.system.entity.agreement.AgreementWarehouse;
import com.mantoo.mtic.module.system.entity.equip.EquipGroup;
import com.mantoo.mtic.module.system.exMapper.agreement.AgreementMeasuredValuesMapper;
import com.mantoo.mtic.module.system.exMapper.agreement.AgreementWarehouseMapper;
import com.mantoo.mtic.module.system.service.system.IAgreementWarehouseService;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class AgreementWarehouseServiceImpl implements IAgreementWarehouseService {


    @Resource
    AgreementWarehouseMapper agreementWarehouseMapper;
    @Resource
    AgreementMeasuredValuesMapper agreementMeasuredValuesMapper;

    /**
     *新增协议
     * @param agreementWarehouse
     * @param agreementId
     * @return
     */
    @Override
    public void addAgreementWarehouse(AgreementWarehouse agreementWarehouse, Long agreementId) {
        SysUser user = UserUtil.getUser();
        AgreementWarehouse agreementName = getAgreementByAgreementName(agreementWarehouse.getAgreementName());
        if (agreementName != null) {
            throw new MticException("协议名称已存在", ErrorInfo.USER_EXIST.getCode());
        }

        AgreementWarehouse agreementCode = getAgreementByAgreementCode(agreementWarehouse.getAgreementCode());
        if (agreementCode != null) {
            throw new MticException("协议编号已存在", ErrorInfo.USER_EXIST.getCode());
        }
        //默认状态为0
        agreementWarehouse.setDeleteFlag(AgreementWarehouse.DELETE_FLAG_NORMAL);
        //添加产品的创建时间
        agreementWarehouse.setCreatedTime(new Date());
        //添加产品的更新时间和更新者信息
        agreementWarehouse.setCreatedBy(user.getUserId());
        agreementWarehouse.setUpdatedTime(new Date());
        agreementWarehouse.setUpdatedBy(user.getUserId());
        agreementWarehouseMapper.insertSelective(agreementWarehouse);
        List<AgreementMeasuredValues> valuesList=agreementWarehouse.getValuesList();
        valuesList.forEach(e->{
            e.setAgreementId(agreementWarehouse.getAgreementId());
            //插入测值
            agreementMeasuredValuesMapper.insertSelective(e);
        });
    }



    /**
     * 编辑协议信息
     * @param agreementWarehouse
     * @return
     */
    @Override
    public void updateAgreementWarehouse(AgreementWarehouse agreementWarehouse) {
        SysUser user = UserUtil.getUser();
        //判断协议名称是否重复
        Example exampleName = new Example(AgreementWarehouse.class);
        Example.Criteria criteriaName = exampleName.createCriteria();
        criteriaName.andEqualTo("agreementName", agreementWarehouse.getAgreementName());
        criteriaName.andNotEqualTo("agreementId", agreementWarehouse.getAgreementId());
        criteriaName.andEqualTo("deleteFlag", SysUser.DELETE_FLAG_NORMAL);
        List<AgreementWarehouse> agreementWarehouseNameList = agreementWarehouseMapper.selectByExample(exampleName);
        if (!CollectionUtils.isEmpty(agreementWarehouseNameList)) {
            throw new MticException("协议名称已存在", ErrorInfo.USER_EXIST.getCode());
        }
        //判断协议编号是否重复
        Example exampleCode = new Example(AgreementWarehouse.class);
        Example.Criteria criteriaCode = exampleCode.createCriteria();
        criteriaCode.andEqualTo("agreementCode", agreementWarehouse.getAgreementCode());
        criteriaCode.andNotEqualTo("agreementId", agreementWarehouse.getAgreementId());
        criteriaCode.andEqualTo("deleteFlag", SysUser.DELETE_FLAG_NORMAL);
        List<AgreementWarehouse> agreementWarehouseCodeList = agreementWarehouseMapper.selectByExample(exampleCode);
        if (!CollectionUtils.isEmpty(agreementWarehouseCodeList)) {
            throw new MticException("协议编号已存在", ErrorInfo.USER_EXIST.getCode());
        }

        //添加更新时间和更新者信息
        agreementWarehouse.setUpdatedBy(user.getUserId());
        agreementWarehouse.setUpdatedTime(DateUtils.getNowDate());
        //先删除再添加
        AgreementMeasuredValues delAgreementId = new AgreementMeasuredValues();
        delAgreementId.setAgreementId(agreementWarehouse.getAgreementId());
        agreementMeasuredValuesMapper.delete(delAgreementId);
        if (!CollectionUtils.isEmpty(agreementWarehouse.getValuesList())) {
            List<AgreementMeasuredValues> valuesList = new ArrayList<>();
            for (AgreementMeasuredValues agreementMeasuredValues : agreementWarehouse.getValuesList()) {
                agreementMeasuredValues.setAgreementId(agreementWarehouse.getAgreementId());
                valuesList.add(agreementMeasuredValues);
            }
            agreementMeasuredValuesMapper.insertList(valuesList);
        }
    }

    /**
     * 编辑脚本
     * @param agreementWarehouse
     */
    @Override
    public void updateScriptContent(AgreementWarehouse agreementWarehouse) {
        SysUser user = UserUtil.getUser();
        //添加更新时间和更新者信息
        agreementWarehouse.setUpdatedBy(user.getUserId());
        agreementWarehouse.setUpdatedTime(DateUtils.getNowDate());
        agreementWarehouse.setScriptContent(agreementWarehouse.getScriptContent());
        agreementWarehouseMapper.updateByPrimaryKeySelective(agreementWarehouse);
    }

    /**
     * 判断是否重复
     * @param agreementWarehouse
     * @return
     */
    @Override
    public Boolean agreementWarehouseNameIsRepeatable(AgreementWarehouse agreementWarehouse) {
        //如果不重复返回false
        if(!Objects.isNull(agreementWarehouseMapper.selectAgreementByAgreement(agreementWarehouse))) {
            return true;
        }else{
            return false;
        }
    }

    /**
     * 删除协议 （1改0）
     * @param agreementWarehouse
     * @return
     */
    @Override
    public int delAgreementWarehouse(AgreementWarehouse agreementWarehouse) {

        AgreementMeasuredValues delAgreementId = new AgreementMeasuredValues();
        delAgreementId.setAgreementId(agreementWarehouse.getAgreementId());
        agreementMeasuredValuesMapper.delete(delAgreementId);
        agreementWarehouse.setUpdatedTime(DateUtils.getNowDate());
        agreementWarehouse.setUpdatedBy(UserUtil.getUserId());
        return agreementWarehouseMapper.delAgreementWarehouse(agreementWarehouse);
    }

    /**
     * 模糊查询
     * @param agreementWarehouse
     * @return
     */
    @Override
    public List<AgreementWarehouse> findByAgreementCondition(AgreementWarehouse agreementWarehouse) {
        if (agreementWarehouse.getPageNum() != null && agreementWarehouse.getPageSize() != null) {
            PageHelper.startPage(agreementWarehouse.getPageNum(), agreementWarehouse.getPageSize());
        }
        List<AgreementWarehouse> list = agreementWarehouseMapper.selectByAgreementListWithCondition(agreementWarehouse);
        for (AgreementWarehouse e : list) {
            List<AgreementMeasuredValues> agreementMeasuredValuesList = agreementWarehouseMapper.selectAgreementListWithCondition(e.getAgreementId());
            e.setValuesList(agreementMeasuredValuesList);
        }
        return list;

    }



    /**
     *根据协议名称查询协议
     * @param agreementName
     * @return
     */
    @Override
    public AgreementWarehouse getAgreementByAgreementName(String agreementName) {
        AgreementWarehouse agreementWarehouse=new AgreementWarehouse();
        agreementWarehouse.setAgreementName(agreementName);
        agreementWarehouse.setDeleteFlag(EquipGroup.DELETE_FLAG_NORMAL);
        AgreementWarehouse agreementWarehouseName;
        try {
            agreementWarehouseName = agreementWarehouseMapper.selectOne(agreementWarehouse);
        }catch (Exception e){
            throw new MticException(ErrorInfo.EXIST_MORE.getMsg(), ErrorInfo.EXIST_MORE.getCode());
        }

        return agreementWarehouseName;
    }

    /**
     *根据协议编号查询协议
     * @param agreementCode
     * @return
     */
    @Override
    public AgreementWarehouse getAgreementByAgreementCode(String agreementCode) {
        AgreementWarehouse agreementWarehouse=new AgreementWarehouse();
        agreementWarehouse.setAgreementCode(agreementCode);
        agreementWarehouse.setDeleteFlag(EquipGroup.DELETE_FLAG_NORMAL);
        AgreementWarehouse agreementWarehouseCode;
        try {
            agreementWarehouseCode = agreementWarehouseMapper.selectOne(agreementWarehouse);
        }catch (Exception e){
            throw new MticException(ErrorInfo.EXIST_MORE.getMsg(), ErrorInfo.EXIST_MORE.getCode());
        }

        return agreementWarehouseCode;
    }


  
}
