package com.dahantc.iot.api.base;

import com.dahantc.iot.dto.SignDto;

/**
 * 描述
 *
 * @author zdq
 * @create 2018/5/21
 */
public abstract class BaseApi {
    protected String baseUrl;
    public String appId;
    public String appKey;

    /**
     * 签名
     * @param pathUrl
     * @return
     */
    protected SignDto createSignDto(String pathUrl){
        SignDto signDto = new SignDto();
        signDto.setAppid(appId);
        signDto.setAppkey(appKey);
        signDto.setPath(pathUrl);
        return signDto;
    }

    /**
     * 单卡卡状态改变 0激活 1停用
     */
    public abstract String changeCardStatus(String iccid,int status);

    /**
     * 批量卡状态改变 激活 停用
     */
    public abstract String batchChangeCardStatus(String iccids,int status);

    /**
     * 单卡GPRS功能的开停
     */
    public abstract String changeGPRSStatus(String iccid,int status);

    /**
     * 批量卡GPRS功能的开停
     */
    public abstract String batchChangeGPRSStatus(String iccids,int status);
    /**
     * 发送短信，支持批量
     */
    public abstract String sendSms(String iccids,String content);

    /**
     * 卡续费
     */
    public abstract String renew(String iccids,int payType,int renewPeriod);


    //-------------

    /**
     * 单卡状态查询测试用例
     */
    public abstract String cardStatus(String iccid);

    /**
     * 批量卡状态查询测试用例
     */
    public abstract String batchCardStatus(String iccids);

    /**
     * 单卡信息查询测试用例
     */
    public abstract String cardInfo(String iccid);

    /**
     * 批量卡状态查询测试用例
     */
    public abstract String batchCardInfo(String iccids);

    /**
     * 单卡套餐查询测试用例
     */
    public abstract String cardPackage(String iccid);

    /**
     * 批量卡套餐查询测试用例
     */
    public abstract String batchCardPackage(String iccids);

    /**
     * 单卡查询剩余流量测试用例
     */
    public abstract String flowLeft(String iccid);

    /**
     * 批量查询剩余流量测试用例
     */
    public abstract String batchFlowLef(String iccids);

    /**
     * 短信记录查询
     */
    public abstract String smsRecord(String iccid,String date);

    /**
     * 查询客户流量池列表
     */
    public abstract String queryCustomPool();

    /**
     * 查询客户流量池详情
     */
    public abstract String queryCustomPoolInfo(String poolNo);

    /**
     * 查询客户流量池卡列表
     */
    public abstract String queryCustomPoolCard(String poolNo,String pageNum);

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
