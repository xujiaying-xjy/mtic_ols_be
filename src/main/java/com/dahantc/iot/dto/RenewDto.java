package com.dahantc.iot.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 续费接口描述
 *
 * @author zdq
 * @create 2018/5/16
 */
public class RenewDto implements Serializable {

    private Integer payType;

    private List<String> iccidList;

    private Integer renewPeriod;

    private String loginName;

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public List<String> getIccidList() {
        return iccidList;
    }

    public void setIccidList(List<String> iccidList) {
        this.iccidList = iccidList;
    }

    public Integer getRenewPeriod() {
        return renewPeriod;
    }

    public void setRenewPeriod(Integer renewPeriod) {
        this.renewPeriod = renewPeriod;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
