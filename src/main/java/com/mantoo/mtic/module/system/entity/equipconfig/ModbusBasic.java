package com.mantoo.mtic.module.system.entity.equipconfig;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @ClassName: modbus
 * @Description: modbus基础信息
 * @Author: xjy
 * @Date: 2021-04-27 10:45
 */
@Getter
@Setter
public class ModbusBasic {

    /**
     * 设备id
     */
    private Long equipId;

    /**
     * 轮询使能(01打开，00-关闭)
     */
    private String modpolln;

    /**
     * 轮询协议类型(00-自定义，01-Modbus RTU)
     */
    private String agreeType;

    /**
     * 轮询周期
     */
    private Integer modton;

    /**
     * 使能主动上传网络服务器(01-打开，00-关闭)
     */
    private String modpollupn;

    /**
     * 上报周期
     */
    private Integer uploadton;

    /**
     * modbusRtu列表
     */
    private List<ModbusRtu> modbusRtuList;

    /**
     * modbus自定义列表
     */
    private List<ModbusDefault> defaultList;
}
