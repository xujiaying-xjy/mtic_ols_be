package com.mantoo.mtic.module.system.controller.equip;


import com.github.pagehelper.PageInfo;
import com.mantoo.mtic.common.entity.RestResult;
import com.mantoo.mtic.common.entity.ResultUtils;
import com.mantoo.mtic.common.utils.StringUtil;
import com.mantoo.mtic.common.utils.UserUtil;
import com.mantoo.mtic.exception.MticException;
import com.mantoo.mtic.module.system.entity.SysUser;
import com.mantoo.mtic.module.system.entity.equip.EquipGroup;
import com.mantoo.mtic.module.system.service.system.IEquipGroupService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/web/equipGroup")
public class EquipGroupController {


    @Autowired
    IEquipGroupService equipGroupService;



    /**
     * @param equipGroup
     * @param result
     * @return RestResult
     * @Description 条件模糊查询
     */
    @PostMapping("/findByEquipGroupCondition")
    @RequiresAuthentication
    public RestResult<PageInfo<EquipGroup>> findByEquipGroupCondition(@Valid @RequestBody EquipGroup equipGroup,
                                                                      BindingResult result) {
        //简单参数校验
        if (result.hasErrors()) {
            return ResultUtils.genErrorResult(result.getAllErrors().get(0).getDefaultMessage());
        }

        List<EquipGroup> equipGroupList = equipGroupService.findByEquipGroupCondition(equipGroup);
        return ResultUtils.genSuccesResult(ResultUtils.toPageInfo(equipGroupList));
    }


    /**
     * 查询用户是漫途负责人还是漫途客户
     *
     * @param
     * @param result
     * @return
     */
    @PostMapping("/findByEquipGroupCustomerCondition")
    @RequiresAuthentication
    public RestResult<PageInfo<SysUser>> findByEquipGroupCustomerCondition(@Valid @RequestBody SysUser sysUser,BindingResult result) {
        //简单参数校验
        if (result.hasErrors()) {
            return ResultUtils.genErrorResult(result.getAllErrors().get(0).getDefaultMessage());
        }
        //非空校验
        if (sysUser.getUserType() == null) {
            return ResultUtils.genErrorResult("userType不能为空");
        }
        List<SysUser> sysUserList = equipGroupService.findByEquipGroupCustomerCondition(sysUser.getUserType());
        if (CollectionUtils.isEmpty(sysUserList)) {
            return ResultUtils.genSuccesNoDataResult(null);
        }
        return ResultUtils.genSuccesResult(ResultUtils.toPageInfo(sysUserList));
    }


    /**
     * 新增设备组
     * @param equipGroup
     * @param result
     * @return
     */
       @PostMapping("/addEquipGroup")
       @RequiresAuthentication
       public RestResult<String> addEquipGroup(@Valid @RequestBody EquipGroup equipGroup, BindingResult result){
        //简单参数校验
        if (result.hasErrors()) {
            return ResultUtils.genErrorResult(result.getAllErrors().get(0).getDefaultMessage());
        }
        // 非空校验
        RestResult<String> restResult = notNullUtil(equipGroup);
        if (restResult != null) {
            return restResult;
        }
        int i;
        try {
           //添加设备组
           i = equipGroupService.addEquipGroup(equipGroup,UserUtil.getUserId());
        } catch (MticException e) {
           return ResultUtils.genErrorResult(e.getMessage(), e.getCode());
        }
        if (i != 0) {
           return ResultUtils.genSuccesResult();
        } else {
           return ResultUtils.genErrorResult("新增产品失败");
        }

       }

    /**
     * 编辑设备组
     * @param equipGroup
     * @param result
     * @return
     */
    @PostMapping("/updateEquipGroup")
    @RequiresAuthentication
    public RestResult<String> updateEquipGroup(@Valid @RequestBody EquipGroup equipGroup, BindingResult result) {
        //简单参数校验
        if (result.hasErrors()) {
            return ResultUtils.genErrorResult(result.getAllErrors().get(0).getDefaultMessage());
        }
        // 非空校验
        RestResult<String> restResult = notNullUtil(equipGroup);
        if (restResult != null) {
            return restResult;
        }
        //判断设备组是否存在
        if (!equipGroupService.equipGroupNameIsRepeatable(equipGroup)) {
            return ResultUtils.genErrorResult("该设备组不存在，修改失败！");
        }
        int i;
        try {
            i = equipGroupService.updateEquipGroup(equipGroup, UserUtil.getUserId());
        } catch (MticException e) {
            return ResultUtils.genErrorResult(e.getMessage(), e.getCode());
        }
        //i为0 则表示修改失败
        if (i != 0) {
            // 添加操作日志
            // ...
            return ResultUtils.genSuccesResult();
        } else {
            return ResultUtils.genErrorResult("编辑设备组信息失败");
        }
    }

    /**
     * 删除设备组
     * @param equipGroup
     * @param result
     * @return
     */
    @PostMapping("/delEquipGroup")
    @RequiresAuthentication
    public RestResult<String> delEquipGroup(@Valid @RequestBody EquipGroup equipGroup, BindingResult result){
        //简单参数校验
        if (result.hasErrors()) {
            return ResultUtils.genErrorResult(result.getAllErrors().get(0).getDefaultMessage());
        }
        int i;
        try {
            i = equipGroupService.delEquipGroup(equipGroup, UserUtil.getUserId());
        } catch (MticException e) {
            return ResultUtils.genErrorResult(e.getMessage(), e.getCode());
        }
        if (0 != i) {
            // 添加操作日志
            // ...
            return ResultUtils.genSuccesResult();
        } else {
            return ResultUtils.genErrorResult("删除设备组失败");
        }

    }


    /**
     *关联客户
     */
    @PostMapping("/setEquipGroupCustomer")
    @RequiresAuthentication
    public RestResult<String> setEquipGroupCustomer(@RequestParam(value = "customerId") List<Long> customerIdList, Long equipGroupId) {
        //非空判断
        if (equipGroupId == null) {
            return ResultUtils.genErrorResult("equipGroupId不能为空");
        }
//        if (customerIdList == null || customerIdList.size() == 0) {
//            return ResultUtils.genErrorResult("customerId不能为空");
//        }
        equipGroupService.setEquipGroupCustomer(customerIdList, equipGroupId);
        return ResultUtils.genSuccesResult();
    }



    /**
     * 简单参数校验
     * @param equipGroup
     * @return
     */
    private RestResult<String> notNullUtil(EquipGroup equipGroup) {
        if (StringUtil.isBlank(equipGroup.getEquipGroupName())) {
            return ResultUtils.genErrorResult("equipGroupName不能为空！");
        }
        if (StringUtil.isBlank(equipGroup.getEquipGroupCode())) {
            return ResultUtils.genErrorResult("equipGroupCode不能为空！");
        }
        if (equipGroup.getPersonInCharge()==null) {
            return ResultUtils.genErrorResult("personInCharge不能为空");
        }
        return null;
    }
}
