package com.mantoo.mtic.module.system.entity.equipconfig;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @ClassName: LogicBasic
 * @Description: 本地逻辑参数
 * @Author: xjy
 * @Date: 2021-04-27 11:57
 */
@Getter
@Setter
public class LogicBasic {

    /**
     * 设备id
     */
    private Long equipId;

    /**
     * 本地决策使能(01-打开，00-关闭)
     */
    private String logicEnable;

    /**
     * 本地逻辑指令列表
     */
    private List<LocalLogic> localLogicList;

}
