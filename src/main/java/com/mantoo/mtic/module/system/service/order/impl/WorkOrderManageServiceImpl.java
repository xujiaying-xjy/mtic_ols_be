package com.mantoo.mtic.module.system.service.order.impl;

import com.github.pagehelper.PageHelper;
import com.mantoo.mtic.common.entity.ResultUtils;
import com.mantoo.mtic.common.utils.RandomNumber;
import com.mantoo.mtic.common.utils.UserUtil;
import com.mantoo.mtic.module.system.data.ExSysUser;
import com.mantoo.mtic.module.system.entity.SysUser;
import com.mantoo.mtic.module.system.entity.order.WorkOrderManage;
import com.mantoo.mtic.module.system.entity.vo.HandleOrderDetailVO;
import com.mantoo.mtic.module.system.entity.vo.WorkOrderManageVO;
import com.mantoo.mtic.module.system.exMapper.ExSysUserMapper;
import com.mantoo.mtic.module.system.exMapper.order.WorkOrderManageMapper;
import com.mantoo.mtic.module.system.service.order.IWorkOrderManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description:
 * @ClassName: WorkOrderManageServiceImpl
 * @Author: ghy
 * @Date: 2021-04-26 13:34
 */
@Service
@Transactional
public class WorkOrderManageServiceImpl implements IWorkOrderManageService {

    @Autowired
    private WorkOrderManageMapper workOrderManageMapper;

    @Autowired
    private ExSysUserMapper exSysUserMapper;

    /*** 
     * @Description: 条件模糊查询
     * @Param: [workOrderManageVO]
     * @return: java.util.List<com.mantoo.mtic.module.system.entity.vo.WorkOrderManageVO>
     * @Author: ghy
     * @Date: 2021/4/28
     */
    @Override
    public List<WorkOrderManageVO> findAllByCondition(WorkOrderManageVO workOrderManageVO) {
        //判断页码和记录数是否为空
        if (!Objects.isNull(workOrderManageVO.getPageNum()) && !Objects.isNull(workOrderManageVO.getPageSize())) {
            PageHelper.startPage(workOrderManageVO.getPageNum(), workOrderManageVO.getPageSize());
        }
        return workOrderManageMapper.findAllByCondition(workOrderManageVO);
    }

    /*** 
    * @Description: 添加工单 
    * @Param: [workOrderManage] 
    * @return: java.lang.Integer 
    * @Author: ghy
    * @Date: 2021/4/28 
    */
    @Override
    public Integer addWorkOrderManage(WorkOrderManage workOrderManage) {
        workOrderManage.setWorkOrderCode(RandomNumber.GetRandom("MT"));
        workOrderManage.setCreatedBy(UserUtil.getUserId());
        workOrderManage.setCreatedTime(new Date());
        workOrderManage.setUpdatedTime(new Date());
        workOrderManage.setUpdatedBy(UserUtil.getUserId());
        return workOrderManageMapper.insertSelective(workOrderManage);
    }

    /*** 
     * @Description: 根据工单编号查询工单是否重复，如果重复则返回true，不重复则返回false
     * @Param: [workOrderCode]
     * @return: java.lang.Boolean
     * @Author: ghy
     * @Date: 2021/4/28
     */
    @Override
    public Boolean workOrderManageisRepeatable(String workOrderCode) {
        Example ex = new Example(WorkOrderManage.class);
        Example.Criteria criteria = ex.createCriteria();
        criteria.andEqualTo("workOrderCode", workOrderCode);
        List<WorkOrderManage> list = workOrderManageMapper.selectByExample(ex);
        if (list.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /*** 
     * @Description: 处理工单, 处理成功返回true，失败返回false
     * @Param: [workOrderId, handlerOpinion]
     * @return: java.lang.Boolean
     * @Author: ghy
     * @Date: 2021/4/28
     */
    @Override
    public Boolean handleWorkOrderManage(Integer workOrderId, String handlerOpinion) {
        //根据编号查询工单
        WorkOrderManage workOrderManage = workOrderManageMapper.selectByPrimaryKey(workOrderId);
        if (!Objects.isNull(workOrderManage)) {
            //如果工单状态是待处理,添加处理意见之后是到处理中
            if (workOrderManage.getWorkOrderStatus() == 1) {
                if (workOrderManageMapper.pendingToProcessing(handlerOpinion, UserUtil.getUserId(), workOrderId) > 0) {
                    return true;
                }
                //如果工单状态是处理中,添加处理意见之后是到处理结束
            } else if (workOrderManage.getWorkOrderStatus() == 2) {
                if (workOrderManageMapper.processingToSolved(handlerOpinion, UserUtil.getUserId(), workOrderId) > 0) {
                    return true;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public List<HandleOrderDetailVO> getHandleOrderDetail(Integer workOrderId) {
        List<HandleOrderDetailVO> list = null;
        //根据编号查询工单
        WorkOrderManage workOrderManage = workOrderManageMapper.selectByPrimaryKey(workOrderId);
        if (Optional.ofNullable(workOrderManage).isPresent()) {
            list = new ArrayList<>();
            Long createdById = workOrderManage.getCreatedBy();
            SysUser createdUser = exSysUserMapper.selectByPrimaryKey(createdById);

            Integer handlerById = workOrderManage.getHandlerBy();
            SysUser handlerUser = exSysUserMapper.selectByPrimaryKey(handlerById);
            HandleOrderDetailVO handleWithStatus1 = new HandleOrderDetailVO();


            handleWithStatus1.setTitle(String.format(createdUser.getUserName() + " " + "发起工单" + " " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(workOrderManage.getCreatedTime()) ));
            handleWithStatus1.setContent(workOrderManage.getDetailDescribe());

            HandleOrderDetailVO handleWithStatus2 = new HandleOrderDetailVO();
            handleWithStatus2.setTitle(handlerUser.getUserName() + " " + "处理工单" + " " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(workOrderManage.getUpdatedTime()));
            handleWithStatus2.setContent(workOrderManage.getHandlerOpinion());

            HandleOrderDetailVO handleWithStatus3 = new HandleOrderDetailVO();
            handleWithStatus3.setTitle("处理完成");
            handleWithStatus3.setContent("");
            list.add(handleWithStatus1);
            list.add(handleWithStatus2);
            list.add(handleWithStatus3);
            return list;
        } else {
            return null;
        }
    }
}
