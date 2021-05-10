package com.mantoo.mtic.module.system.controller.system;

import com.github.pagehelper.PageInfo;
import com.mantoo.mtic.common.entity.RestResult;
import com.mantoo.mtic.common.entity.ResultUtils;
import com.mantoo.mtic.common.utils.StringUtil;
import com.mantoo.mtic.common.utils.UserUtil;
import com.mantoo.mtic.exception.MticException;
import com.mantoo.mtic.module.system.entity.agreement.AgreementMeasuredValues;
import com.mantoo.mtic.module.system.entity.agreement.AgreementWarehouse;
import com.mantoo.mtic.module.system.service.system.IAgreementWarehouseService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/web/agreementWarehouse")
public class AgreementWarehouseController {

    @Autowired
    IAgreementWarehouseService agreementWarehouseService;

    /**
     * 条件模糊查询
     * @param agreementWarehouse
     * @param result
     * @return
     */
    @PostMapping("/findByAgreementCondition")
    @RequiresAuthentication
    public RestResult<PageInfo<AgreementWarehouse>> findByAgreementCondition(@Valid @RequestBody AgreementWarehouse agreementWarehouse,
                                                                      BindingResult result) {
        //简单参数校验
        if (result.hasErrors()) {
            return ResultUtils.genErrorResult(result.getAllErrors().get(0).getDefaultMessage());
        }

        List<AgreementWarehouse> agreementWarehouseList = agreementWarehouseService.findByAgreementCondition(agreementWarehouse);
        return ResultUtils.genSuccesResult(ResultUtils.toPageInfo(agreementWarehouseList));
    }

    /**
     * 新增协议
     * @param agreementWarehouse
     * @param result
     * @return
     */
    @PostMapping("/addAgreementWarehouse")
    @RequiresAuthentication
    public RestResult<String> addAgreementWarehouse(@Valid @RequestBody AgreementWarehouse agreementWarehouse,
                                                    BindingResult result) {
        //简单参数校验
        if (result.hasErrors()) {
            return ResultUtils.genErrorResult(result.getAllErrors().get(0).getDefaultMessage());
        }
        // 非空校验
        RestResult<String> restResult = notNullUtil(agreementWarehouse);
        if (restResult != null) {
            return restResult;
        }

        try {
           agreementWarehouseService.addAgreementWarehouse(agreementWarehouse, UserUtil.getUserId());
        } catch (MticException e) {
            return ResultUtils.genErrorResult(e.getMessage(), e.getCode());
        }

        return ResultUtils.genSuccesResult();
    }

    /**
     *编辑协议信息
     * @param agreementWarehouse
     * @param result
     * @return
     */
    @PostMapping("/updateAgreementWarehouse")
    @RequiresAuthentication
    public RestResult<String> updateAgreementWarehouse(@Valid @RequestBody AgreementWarehouse agreementWarehouse, BindingResult result) {
        //简单参数校验
        if (result.hasErrors()) {
            return ResultUtils.genErrorResult(result.getAllErrors().get(0).getDefaultMessage());
        }
        // 非空校验
        RestResult<String> restResult = notNullUtil(agreementWarehouse);
        if (restResult != null) {
            return restResult;
        }
        if (StringUtil.isBlank(String.valueOf(agreementWarehouse.getAgreementId()))) {
            return ResultUtils.genErrorResult("agreementId不能为空！");
        }
        //判断设备组是否存在
        if (!agreementWarehouseService.agreementWarehouseNameIsRepeatable(agreementWarehouse)) {
            return ResultUtils.genErrorResult("该协议不存在，修改失败！");
        }

        try {
            agreementWarehouseService.updateAgreementWarehouse(agreementWarehouse);
        } catch (MticException e) {
            return ResultUtils.genErrorResult(e.getMessage(), e.getCode());
        }
        return ResultUtils.genSuccesResult();
    }

    /**
     * 删除协议
     * @param agreementWarehouse
     * @param result
     * @return
     */
    @PostMapping("/delAgreementWarehouse")
    @RequiresAuthentication
    public RestResult<String> delAgreementWarehouse(@Valid @RequestBody AgreementWarehouse agreementWarehouse, BindingResult result){
        //简单参数校验
        if (result.hasErrors()) {
            return ResultUtils.genErrorResult(result.getAllErrors().get(0).getDefaultMessage());
        }
        int i;
        try {
            i = agreementWarehouseService.delAgreementWarehouse(agreementWarehouse);
        } catch (MticException e) {
            return ResultUtils.genErrorResult(e.getMessage(), e.getCode());
        }
        if (0 != i) {
            // 添加操作日志
            // ...
            return ResultUtils.genSuccesResult();
        } else {
            return ResultUtils.genErrorResult("删除协议失败");
        }

    }

    /**
     * 编辑脚本
     * @param agreementWarehouse
     * @param result
     * @return
     */
    @PostMapping("/updateScriptContent")
    @RequiresAuthentication
    public RestResult<String> updateScriptContent(@Valid @RequestBody AgreementWarehouse agreementWarehouse, BindingResult result) {

        if (result.hasErrors()) {
            return ResultUtils.genErrorResult(result.getAllErrors().get(0).getDefaultMessage());
        }

        try {
            agreementWarehouseService.updateScriptContent(agreementWarehouse);
        } catch (MticException e) {
            return ResultUtils.genErrorResult(e.getMessage(), e.getCode());
        }
        return ResultUtils.genSuccesResult();
    }


    /**
     * 简单参数校验
     * @param agreementWarehouse
     * @return
     */
    private RestResult<String> notNullUtil(AgreementWarehouse agreementWarehouse) {

        if (StringUtil.isBlank(agreementWarehouse.getAgreementName())) {
            return ResultUtils.genErrorResult("agreementName不能为空！");
        }
        if (StringUtil.isBlank(agreementWarehouse.getAgreementCode())) {
            return ResultUtils.genErrorResult("agreementCode不能为空！");
        }
        return null;
    }
}

