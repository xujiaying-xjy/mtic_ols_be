package com.mantoo.mtic.common.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;

/**
 * @ClassName: BaseEntity
 * @Description: 基础分页实体类
 * @Author: renjt
 * @Date: 2019-11-19 15:34
 */
@Getter
@Setter
public class BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 删除标记（0：正常；1：删除；）
     */
    public static final int DELETE_FLAG_NORMAL = 0;
    public static final int DELETE_FLAG_DELETED = 1;

    /**
     * 排序
     */
    @Transient
    private Integer sort;

    /**
     * 当前页码，默认第1页
     */
    @Transient
    private Integer pageNum;

    /**
     * 每页的记录数,默认10条
     */
    @Transient
    private Integer pageSize;
}
