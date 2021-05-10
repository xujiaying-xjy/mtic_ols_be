package com.mantoo.mtic.module.system.entity.equipconfig;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: ModbusRtu
 * @Description: ModbusRtu参数
 * @Author: xjy
 * @Date: 2021-04-27 11:06
 */
@Getter
@Setter
public class ModbusRtu {

    /**
     * 协议编号
     */
    private Integer number;

    /**
     * 从机地址
     */
    private Integer subAddr;

    /**
     * 数据地址
     */
    private Integer dataAddr;

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 计算因素
     */
    private String factor;

    /**
     * 标识符
     */
    private String remind;
}
