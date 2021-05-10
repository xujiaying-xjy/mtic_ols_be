package com.mantoo.mtic.module.system.controller.system;

import com.github.pagehelper.PageInfo;
import com.mantoo.mtic.common.entity.RestResult;
import com.mantoo.mtic.common.entity.ResultUtils;
import com.mantoo.mtic.common.utils.StringUtil;
import com.mantoo.mtic.exception.MticException;
import com.mantoo.mtic.module.system.entity.vo.ProductManageVO;
import com.mantoo.mtic.module.system.entity.system.ProductManage;
import com.mantoo.mtic.module.system.service.system.IProductManageService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Description 产品管理controller
 * @ClassName: ProductManageController
 * @Author: ghy
 * @Date: 2021-04-16 11:54
 */
@RestController
@RequestMapping("/web/productManage")
public class ProductManageController {
    @Autowired
    IProductManageService productManageService;

     /*** 
     * @Description: 条件模糊查询产品。名称、型号、产品类型、生产状态（1表示正常，2表示停产）
     * @Param: [productManage, result] 
     * @return: com.mantoo.mtic.common.entity.RestResult<com.github.pagehelper.PageInfo<com.mantoo.mtic.module.system.entity.system.ProductManage>> 
     * @Author: ghy
     * @Date: 2021/4/23 
     */
    @PostMapping("/findAllByCondition")
    @RequiresPermissions("product:list")
    public RestResult<PageInfo<ProductManage>> getAll(@Valid @RequestBody ProductManage productManage, BindingResult result) {
        //简单参数校验
        if (result.hasErrors()) {
            return ResultUtils.genErrorResult(result.getAllErrors().get(0).getDefaultMessage());
        }
        List<ProductManage> productManagelist = productManageService.findByCondition(productManage);
        //如果为空
        if (CollectionUtils.isEmpty(productManagelist)) {
            return ResultUtils.genSuccesNoDataResult(null);
        }
        return ResultUtils.genSuccesResult(ResultUtils.toPageInfo(productManagelist));
    }

    /*** 
    * @Description: 删除产品 
    * @Param: [productManage, result] 
    * @return: com.mantoo.mtic.common.entity.RestResult<java.lang.String> 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    @PostMapping("/deleteProductManage")
    @RequiresPermissions("product:del")
    public RestResult<String> delete(@Valid @RequestBody ProductManage productManage, BindingResult result) {
        //简单参数校验
        if (result.hasErrors()) {
            return ResultUtils.genErrorResult(result.getAllErrors().get(0).getDefaultMessage());
        }
        int i;
        try {
            i = productManageService.deleteProductManage(productManage);
        } catch (MticException e) {
            return ResultUtils.genErrorResult(e.getMessage(), e.getCode());
        }
        if (0 != i) {
            return ResultUtils.genSuccesResult();
        } else {
            return ResultUtils.genErrorResult("删除产品失败");
        }
    }

    /*** 
    * @Description: 添加产品 
    * @Param: [productManage, result] 
    * @return: com.mantoo.mtic.common.entity.RestResult<java.lang.String> 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    @PostMapping("/addProductManage")
    @RequiresPermissions("product:add")
    public RestResult<String> save(@Valid @RequestBody ProductManage productManage, BindingResult result) {
        //简单参数校验
        if (result.hasErrors()) {
            return ResultUtils.genErrorResult(result.getAllErrors().get(0).getDefaultMessage());
        }
        // 非空校验
        RestResult<String> restResult = notNull(productManage);
        if (null != restResult) {
            return restResult;
        }
        //判断是否重复，productCode不能重复，productName是可以重复的
        if (productManageService.productManageIsRepeatable(productManage)) {
            return ResultUtils.genErrorResult("产品添加重复！");
        }
        int i;
        try {
            //添加产品
            i = productManageService.addProductManage(productManage);
        } catch (MticException e) {
            return ResultUtils.genErrorResult(e.getMessage(), e.getCode());
        }
        if (i != 0) {
            return ResultUtils.genSuccesResult();
        } else {
            return ResultUtils.genErrorResult("新增产品失败");
        }
    }

    /***
    * @Description:  查询所有产品型号数据，包含productId、productName和productCode
    * @Param: []
    * @return: com.mantoo.mtic.common.entity.RestResult<java.util.List<com.mantoo.mtic.module.system.entity.vo.ProductManageVO>>
    * @Author: ghy
    * @Date: 2021/4/23
    */
    @PostMapping("/getProductCodeList")
    @RequiresPermissions("product:list")
    public RestResult<List<ProductManageVO>> getProductCodeList() {
        List<ProductManageVO> list = productManageService.getProductCodeList();
        //如果为空
        if (CollectionUtils.isEmpty(list)) {
            return ResultUtils.genSuccesNoDataResult(null);
        }
        return ResultUtils.genSuccesResult(list);
    }


    /*** 
    * @Description: 更新产品
     * @Param: [productManage, result] 
    * @return: com.mantoo.mtic.common.entity.RestResult<java.lang.String> 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    @PostMapping("/editProductManage")
    @RequiresPermissions("product:update")
    public RestResult<String> editProductManage(@Valid @RequestBody ProductManage productManage, BindingResult result) {
        //简单参数校验
        if (result.hasErrors()) {
            return ResultUtils.genErrorResult(result.getAllErrors().get(0).getDefaultMessage());
        }

        // 非空校验
        RestResult<String> restResult = notNull(productManage);
        if (null != restResult) {
            return restResult;
        }
        //判断产品是否存在
        if (!productManageService.productManageIsRepeatable(productManage)) {
            return ResultUtils.genErrorResult("该产品不存在，修改失败！");
        }
        int i;
        try {
            i = productManageService.updateProductManage(productManage);
        } catch (MticException e) {
            return ResultUtils.genErrorResult(e.getMessage(), e.getCode());
        }
        //i为0 则表示修改失败
        if (i != 0) {
            return ResultUtils.genSuccesResult();
        } else {
            return ResultUtils.genErrorResult("编辑产品失败");
        }
    }

    /*** 
    * @Description: 判断属性是否为空
    * @Param: [productManage] 
    * @return: com.mantoo.mtic.common.entity.RestResult<java.lang.String> 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    private RestResult<String> notNull(ProductManage productManage) {
        if (StringUtil.isBlank(productManage.getProductName())) {
            return ResultUtils.genErrorResult("productname不能为空！");
        }
        if (null == productManage.getProductType()) {
            return ResultUtils.genErrorResult("producttype不能为空！");
        }
        if (null == productManage.getProduceStatus()) {
            return ResultUtils.genErrorResult("produceStatus不能为空！");
        }
        if (StringUtil.isBlank(productManage.getProductCode())) {
            return ResultUtils.genErrorResult("productCode不能为空！");
        }
        return null;
    }

}