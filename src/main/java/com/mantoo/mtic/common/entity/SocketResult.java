package com.mantoo.mtic.common.entity;

import com.mantoo.mtic.module.system.entity.equip.EquipInfo;

/**
 * @ClassName: SocketResult
 * @Description: TODO
 * @Author: xjy
 * @Date: 2021-04-22 17:20
 */
public class SocketResult {

    //类型 （0-发送，1-接收）
    int type;
    //完整指令
    String order;
    //提取部分
    Object validOrder;
    //提取部分类型(0-空，1-基本参数读取，2-升级进度, 3-modbus基础，4-modbusRtu，5-自定义, 6-决策使能，7-本地逻辑)
    int validType;
    //是否成功（0-失败，1-单个成功，2-成功）
    int isSuccess;

    public static SocketResult getSocketResult(int type, String order, Object validOrder, int validType, int isSuccess){
        SocketResult socketResult = new SocketResult();
        socketResult.setType(type);
        socketResult.setOrder(order);
        socketResult.setValidOrder(validOrder);
        socketResult.setValidType(validType);
        socketResult.setIsSuccess(isSuccess);
        return socketResult;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Object getValidOrder() {
        return validOrder;
    }

    public void setValidOrder(Object validOrder) {
        this.validOrder = validOrder;
    }

    public int getValidType() {
        return validType;
    }

    public void setValidType(int validType) {
        this.validType = validType;
    }

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }
}
