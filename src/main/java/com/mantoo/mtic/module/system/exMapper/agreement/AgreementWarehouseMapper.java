package com.mantoo.mtic.module.system.exMapper.agreement;

import com.mantoo.mtic.common.mapper.MyMapper;
import com.mantoo.mtic.module.system.entity.agreement.AgreementMeasuredValues;
import com.mantoo.mtic.module.system.entity.agreement.AgreementWarehouse;
import com.mantoo.mtic.module.system.entity.system.ProductManage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgreementWarehouseMapper extends MyMapper<AgreementWarehouse> {
    /**
     * 条件模糊查询协议测量信息
     * @param agreementWarehouseId
     * @return
     */
    List<AgreementMeasuredValues> selectAgreementListWithCondition(Long agreementWarehouseId);

    /**
     * 条件模糊查询协议信息
     * @param agreementWarehouse
     * @return
     */
    List<AgreementWarehouse> selectByAgreementListWithCondition(AgreementWarehouse agreementWarehouse);

    /**
     * 判断是否重复
     * @param agreementWarehouse
     * @return
     */
    Integer selectAgreementByAgreement(AgreementWarehouse agreementWarehouse);

    /**
     * 删除协议
     * @param agreementWarehouse
     * @return
     */
    int delAgreementWarehouse(AgreementWarehouse agreementWarehouse);

}