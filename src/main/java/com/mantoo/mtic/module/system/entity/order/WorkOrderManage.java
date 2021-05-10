package com.mantoo.mtic.module.system.entity.order;

import com.mantoo.mtic.common.entity.BaseEntity;
import java.util.Date;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "work_order_manage")
public class WorkOrderManage extends BaseEntity {
    /**
     * 主键id
     */
    @Id
    @Column(name = "work_order_id")
    @GeneratedValue(generator = "JDBC")
    private Long workOrderId;

    /**
     * 工单编号
     */
    @Column(name = "work_order_code")
    private String workOrderCode;

    /**
     * 工单类型（1.内部工单；2.客户反馈）
     */
    @Column(name = "work_order_type")
    private Integer workOrderType;

    /**
     * 工单状态（1.待处理；2.处理中；3.已完成）
     */
    @Column(name = "work_order_status")
    private Integer workOrderStatus;

    /**
     * 问题分类
     */
    @Column(name = "question_classify")
    private String questionClassify;

    /**
     * 处理人
     */
    @Column(name = "handler_by")
    private Integer handlerBy;

    /**
     * 详细描述
     */
    @Column(name = "detail_describe")
    private String detailDescribe;

    /**
     * 处理意见
     */
    @Column(name = "handler_opinion")
    private String handlerOpinion;

    /**
     * 创建人
     */
    @Column(name = "created_by")
    private Long createdBy;

    /**
     * 创建时间
     */
    @Column(name = "created_time")
    private Date createdTime;

    /**
     * 更新人
     */
    @Column(name = "updated_by")
    private Long updatedBy;

    /**
     * 更新时间
     */
    @Column(name = "updated_time")
    private Date updatedTime;
}