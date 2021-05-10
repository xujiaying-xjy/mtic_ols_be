package com.dahantc.iot.dto;

import java.io.Serializable;

/**
 * 批量卡请求封装类
 * @author zdq
 * @create 2018/3/22
 */
public class BatchCardStatusDto implements Serializable{
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
