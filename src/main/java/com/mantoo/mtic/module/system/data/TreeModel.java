package com.mantoo.mtic.module.system.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @ClassName: TreeModel
 * @Description: 返回前端的标准树形结构
 * @Author: renjt
 * @Date: 2020-04-03 16:58
 */
@Setter
@Getter
public class TreeModel {

    /**
     * id
     */
    private String id;

    /**
     * value(级联选择框需要)
     */
    private Long value;

    /**
     * 名称
     */
    private String label;

    /**
     * 子菜单
     */
    private List<TreeModel> children;

    /**
     * 等级
     */
    private Integer level;
}
