package com.dahantc.iot.dto;

import java.io.Serializable;

/**
 * 批量卡请求封装类
 *
 * @author zdq
 * @create 2018/3/22
 */
public class BatchCardDto implements Serializable {
    private String iccids;

    public String getIccids() {
        return iccids;
    }

    public void setIccids(String iccids) {
        this.iccids = iccids;
    }
}
