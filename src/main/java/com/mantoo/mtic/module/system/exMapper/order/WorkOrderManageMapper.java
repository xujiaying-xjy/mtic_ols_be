package com.mantoo.mtic.module.system.exMapper.order;

import com.mantoo.mtic.common.mapper.MyMapper;
import com.mantoo.mtic.module.system.entity.order.WorkOrderManage;
import com.mantoo.mtic.module.system.entity.vo.WorkOrderManageVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WorkOrderManageMapper extends MyMapper<WorkOrderManage> {
    /*** 
    * @Description: 条件模糊查询 
    * @Param: [workOrderManageVO] 
    * @return: java.util.List<com.mantoo.mtic.module.system.entity.vo.WorkOrderManageVO> 
    * @Author: ghy
    * @Date: 2021/4/28 
    */
    List<WorkOrderManageVO> findAllByCondition(WorkOrderManageVO workOrderManageVO);
    
    /*** 
    * @Description: 未处理到处理中
    * @Param: [handlerOpinion, userId, workOrderId] 
    * @return: java.lang.Integer 
    * @Author: ghy
    * @Date: 2021/4/28 
    */
    Integer pendingToProcessing(@Param("handlerOpinion") String handlerOpinion, @Param("userId") Long userId, @Param("workOrderId") Integer workOrderId);

    /*** 
    * @Description: 处理中到处理完
    * @Param: [handlerOpinion, userId, workOrderId] 
    * @return: java.lang.Integer 
    * @Author: ghy
    * @Date: 2021/4/28 
    */
    Integer processingToSolved(@Param("handlerOpinion") String handlerOpinion,@Param("userId") Long userId,@Param("workOrderId") Integer workOrderId);
}