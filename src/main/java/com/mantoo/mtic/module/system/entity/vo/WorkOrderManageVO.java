package com.mantoo.mtic.module.system.entity.vo;

import com.mantoo.mtic.common.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Description:
 * @ClassName: HandleWorkOrderManage
 * @Author: ghy
 * @Date: 2021-04-27 17:04
 */
@Getter
@Setter
@ToString
public class WorkOrderManageVO extends BaseEntity {
    /**
     * 工单ID
     */
    private Integer workOrderId;

    /**
     * 工单编号
     */
    private String workOrderCode;

    /**
     * 工单类型（1.内部工单；2.客户反馈）
     */
    private Integer workOrderType;

    /**
     * 问题分类
     */
    private String questionClassify;

    /**
     * 处理意见
     */
    private  String handlerOpinion;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 发起人姓名
     */
    private String createdName;

    /**
     * 工单状态（1.待处理；2.处理中；3.已完成）
     */
    private Integer workOrderStatus;

    /**
     * 处理人姓名
     */
    private String handlerName;

    /**
     * 发起人id
     */
    private Integer createdBy;

    /**
     * 更新人id
     */
    private Integer updatedBy;

    /**
     * 处理人id
     */
    private Integer handlerBy;

    /**
     * 创建开始时间
     */
    private Date createdStartTime;

    /**
     * 创建结束时间
     */
    private Date createdEndTime;
}
