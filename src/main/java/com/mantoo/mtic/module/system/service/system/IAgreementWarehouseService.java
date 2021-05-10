package com.mantoo.mtic.module.system.service.system;

import com.mantoo.mtic.module.system.entity.agreement.AgreementWarehouse;

import java.util.List;

public interface IAgreementWarehouseService {

    /**
     * 新增协议
     * @param agreementWarehouse
     * @param agreementId
     * @return
     */
    void addAgreementWarehouse(AgreementWarehouse agreementWarehouse, Long agreementId);

    /**
     * 编辑协议
     * @param agreementWarehouse
     * @return
     */
    void updateAgreementWarehouse(AgreementWarehouse agreementWarehouse);

    /**
     * 编辑脚本
     * @param agreementWarehouse
     */
    void updateScriptContent(AgreementWarehouse agreementWarehouse);

    /**
     * 判断设备组是否重复
     * @param agreementWarehouse
     * @return
     */
    Boolean agreementWarehouseNameIsRepeatable(AgreementWarehouse agreementWarehouse);

    /**
     * 删除协议
     * @param agreementWarehouse
     * @return
     */
    int delAgreementWarehouse(AgreementWarehouse agreementWarehouse);

    /**
     * 条件模糊查询设备组
     * @param agreementWarehouse
     * @return
     */
    List<AgreementWarehouse> findByAgreementCondition(AgreementWarehouse agreementWarehouse);

    /**
     * 根据协议名称查询协议
     * @param agreementName
     * @return
     */
    AgreementWarehouse getAgreementByAgreementName(String agreementName);

    /**
     * 根据协议编号查询协议
     * @param agreementCode
     * @return
     */
    AgreementWarehouse getAgreementByAgreementCode(String agreementCode);
}
