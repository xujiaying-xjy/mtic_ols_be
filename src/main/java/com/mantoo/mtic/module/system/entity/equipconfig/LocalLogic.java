package com.mantoo.mtic.module.system.entity.equipconfig;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: LocalLogic
 * @Description: 本地逻辑指令
 * @Author: xjy
 * @Date: 2021-04-27 13:08
 */
@Getter
@Setter
public class LocalLogic {

    /**
     * 协议编号
     */
    private Integer number;

    /**
     * 参数类型
     */
    private String parameterType;

    /**
     * 参数地址
     */
    private Integer parameterAddr;

    /**
     * 判断逻辑
     */
    private String judgeLogic;

    /**
     * 判断条件
     */
    private String judgeConditions;

    /**
     * 数值
     */
    private String value;

    /**
     * 逻辑输出
     */
    private String logicOutput;

}
