package com.mantoo.mtic.module.system.entity.equipconfig;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: ModbusDefault
 * @Description: modbus自定义
 * @Author: xjy
 * @Date: 2021-04-27 11:17
 */
@Getter
@Setter
public class ModbusDefault {

    /**
     * 协议编号
     */
    private Integer number;

    /**
     * 自定义指令
     */
    private String order;

    /**
     * 是否选中hex(0-否，1-是)
     */
    private Integer hex;


}
