package com.mantoo.mtic.module.system.controller.order;

import com.github.pagehelper.PageInfo;
import com.mantoo.mtic.common.entity.RestResult;
import com.mantoo.mtic.common.entity.ResultUtils;
import com.mantoo.mtic.exception.MticException;
import com.mantoo.mtic.module.system.entity.SysUser;
import com.mantoo.mtic.module.system.entity.order.WorkOrderManage;
import com.mantoo.mtic.module.system.entity.vo.HandleOrderDetailVO;
import com.mantoo.mtic.module.system.entity.vo.WorkOrderManageVO;
import com.mantoo.mtic.module.system.exMapper.ExSysUserMapper;
import com.mantoo.mtic.module.system.exMapper.order.WorkOrderManageMapper;
import com.mantoo.mtic.module.system.service.order.IWorkOrderManageService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * @Description:
 * @ClassName: WorkOrderManageController
 * @Author: ghy
 * @Date: 2021-04-26 13:31
 */
@RestController
@RequestMapping("/web/workOrderManage")
public class WorkOrderManageController {

    @Autowired
    private IWorkOrderManageService workOrderManageService;

    @Autowired
    private WorkOrderManageMapper workOrderManageMapper;

    @Autowired
    private ExSysUserMapper exSysUserMapper;

    /*** 
    * @Description: 条件模糊查询 
    * @Param: [workOrderManageVO, result] 
    * @return: com.mantoo.mtic.common.entity.RestResult<com.github.pagehelper.PageInfo<com.mantoo.mtic.module.system.entity.vo.WorkOrderManageVO>> 
    * @Author: ghy
    * @Date: 2021/4/28 
    */
    @PostMapping("/findAllByCondition")
    @RequiresPermissions("order:list")
    public RestResult<PageInfo<WorkOrderManageVO>> getAllByCondtion(@Valid @RequestBody WorkOrderManageVO workOrderManageVO, BindingResult result) {
        //简单参数校验
        if (result.hasErrors()) {
            return ResultUtils.genErrorResult(result.getAllErrors().get(0).getDefaultMessage());
        }
        List<WorkOrderManageVO> list = workOrderManageService.findAllByCondition(workOrderManageVO);
        if (CollectionUtils.isEmpty(list)) {
            return ResultUtils.genSuccesNoDataResult(null);
        }
        return ResultUtils.genSuccesResult(ResultUtils.toPageInfo(list));
    }

    /***
     * @Description: 发起工单
     * @Param: [workOrderManageVO, result]
     * @return: com.mantoo.mtic.common.entity.RestResult<com.github.pagehelper.PageInfo<com.mantoo.mtic.module.system.entity.order.WorkOrderManage>>
     * @Author: ghy
     * @Date: 2021/4/26
     */
    @PostMapping("/addWorkOrderManage")
    @RequiresPermissions("order:add")
    public RestResult<String> addWorkOrderManage(@Valid @RequestBody WorkOrderManage workOrderManage, BindingResult result) {
        //简单参数校验
        if (result.hasErrors()) {
            return ResultUtils.genErrorResult(result.getAllErrors().get(0).getDefaultMessage());
        }
        // 非空校验
        RestResult<String> restResult = notNull(workOrderManage);
        if (null != restResult) {
            return restResult;
        }
        int i;
        try {
            //添加产品
            i = workOrderManageService.addWorkOrderManage(workOrderManage);
        } catch (MticException e) {
            return ResultUtils.genErrorResult(e.getMessage(), e.getCode());
        }
        if (i != 0) {
            return ResultUtils.genSuccesResult();
        } else {
            return ResultUtils.genErrorResult("发起工单失败");
        }
    }

    /*** 
    * @Description: 查看工单详情
    * @Param: [workOrderId] 
    * @return: com.mantoo.mtic.common.entity.RestResult<java.util.List<com.mantoo.mtic.module.system.entity.vo.HandleOrderDetailVO>> 
    * @Author: ghy
    * @Date: 2021/4/28 
    */
    @PostMapping("/getHandleDetail")
    @RequiresPermissions("order:list")
    public RestResult<List<HandleOrderDetailVO>> getHandleOrderDetail(@Valid @RequestBody WorkOrderManageVO WorkOrderManageVO) {
        List<HandleOrderDetailVO>  list = workOrderManageService.getHandleOrderDetail(WorkOrderManageVO.getWorkOrderId());
        if (CollectionUtils.isEmpty(list)) {
            return ResultUtils.genSuccesNoDataResult(null);
        }
        return ResultUtils.genSuccesResult(list);
    }

    /*** 
    * @Description: 处理工单，传入的参数为vo对象 
    * @Param: [WorkOrderManageVO] 
    * @return: com.mantoo.mtic.common.entity.RestResult<java.lang.String> 
    * @Author: ghy
    * @Date: 2021/4/28 
    */
    @PostMapping("/handleWorkOrderManage")
    @RequiresPermissions("order:update")
    public RestResult<String> handleWorkOrderManage(@Valid @RequestBody WorkOrderManageVO WorkOrderManageVO) {
        if(workOrderManageService.handleWorkOrderManage(WorkOrderManageVO.getWorkOrderId(),WorkOrderManageVO.getHandlerOpinion())){
            return ResultUtils.genSuccesResult("处理意见成功！");
        }else{
            return ResultUtils.genErrorResult("处理意见失败！");
        }
    }

    private RestResult<String> notNull(WorkOrderManage workOrderManage) {
        if (Objects.isNull(workOrderManage.getWorkOrderType())) {
            return ResultUtils.genErrorResult("工单类型不能为空！");
        }
        if (Objects.isNull(workOrderManage.getQuestionClassify())) {
            return ResultUtils.genErrorResult("问题分类不能为空！");
        }
        if (Objects.isNull(workOrderManage.getHandlerBy())) {
            return ResultUtils.genErrorResult("处理人不能为空");
        }
        if (Objects.isNull(workOrderManage.getDetailDescribe())) {
            return ResultUtils.genErrorResult("详细描述不能为空");
        }
        return null;
    }

    /*** 
    * @Description: 获取用户列表
    * @Param: [] 
    * @return: com.mantoo.mtic.common.entity.RestResult<java.util.List<com.mantoo.mtic.module.system.entity.SysUser>> 
    * @Author: ghy
    * @Date: 2021/4/28 
    */
    @PostMapping("/getCreatedBy")
    @RequiresAuthentication
    public RestResult<List<SysUser>> getCreatedBy(){
        List<SysUser> list = exSysUserMapper.selectAll();
        //如果为空
        if (CollectionUtils.isEmpty(list)) {
            return ResultUtils.genSuccesNoDataResult(null);
        }
        return ResultUtils.genSuccesResult(list);
    }
}
