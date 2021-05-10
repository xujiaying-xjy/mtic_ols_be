package com.mantoo.mtic.module.system.service.order;

import com.mantoo.mtic.module.system.entity.order.WorkOrderManage;
import com.mantoo.mtic.module.system.entity.vo.HandleOrderDetailVO;
import com.mantoo.mtic.module.system.entity.vo.WorkOrderManageVO;

import java.util.List;

/**
 * @Description:
 * @ClassName: IWorkOrderManageService
 * @Author: ghy
 * @Date: 2021-04-26 13:34
 */
public interface IWorkOrderManageService {
     /*** 
     * @Description: 条件模糊查询 
     * @Param: [workOrderManageVO] 
     * @return: java.util.List<com.mantoo.mtic.module.system.entity.order.WorkOrderManage> 
     * @Author: ghy
     * @Date: 2021/4/27 
     */
     List<WorkOrderManageVO> findAllByCondition(WorkOrderManageVO workOrderManagevo);

     /*** 
     * @Description: 添加工单
     * @Param: [workOrderManage] 
     * @return: java.lang.Integer 
     * @Author: ghy
     * @Date: 2021/4/27 
     */
     Integer addWorkOrderManage(WorkOrderManage workOrderManage);

     /***
     * @Description: 根据工单编号查询工单是否为空
     * @Param: [workOrderCode]
     * @return: java.lang.Boolean
     * @Author: ghy
     * @Date: 2021/4/26
     */
     Boolean workOrderManageisRepeatable(String workOrderCode);

     /***
     * @Description: 处理工单
     * @Param: [workOrderId, handlerOpinion] 工单id，处理意见
     * @return: java.lang.Boolean 
     * @Author: ghy
     * @Date: 2021/4/27 
     */
     Boolean handleWorkOrderManage(Integer workOrderId, String handlerOpinion);

     /*** 
     * @Description: 工单详情
     * @Param: [workOrderId] 
     * @return: java.util.List<com.mantoo.mtic.module.system.entity.vo.HandleOrderDetailVO> 
     * @Author: ghy
     * @Date: 2021/4/28 
     */
     List<HandleOrderDetailVO> getHandleOrderDetail(Integer workOrderId);
}
