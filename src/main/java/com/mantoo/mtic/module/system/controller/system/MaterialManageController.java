package com.mantoo.mtic.module.system.controller.system;

import com.github.pagehelper.PageInfo;
import com.mantoo.mtic.common.entity.RestResult;
import com.mantoo.mtic.common.entity.ResultUtils;
import com.mantoo.mtic.exception.MticException;
import com.mantoo.mtic.module.system.entity.system.MaterialManage;
import com.mantoo.mtic.module.system.entity.system.ProductManage;
import com.mantoo.mtic.module.system.entity.vo.MaterialManageVO;
import com.mantoo.mtic.module.system.exMapper.system.FirmwareManageMapper;
import com.mantoo.mtic.module.system.exMapper.system.MaterialManageMapper;
import com.mantoo.mtic.module.system.exMapper.system.ProductManageMapper;
import com.mantoo.mtic.module.system.service.system.IMaterialManageService;
import com.mantoo.mtic.module.system.service.system.IProductManageService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

/**
 * @Description 资料管理controller
 * @ClassName: MaterialManage
 * @Author: ghy
 * @Date: 2021-04-22 13:34
 */
@RestController
@RequestMapping("/web/materialManage")
public class MaterialManageController {

    @Autowired
    private IMaterialManageService materialManageService;

    @Autowired
    private IProductManageService productManageService;

    @Autowired
    private MaterialManageMapper materialManageMapper;

    @Autowired
    private ProductManageMapper productManageMapper;
    /*** 
    * @Description: 条件获取所有固件 
    * @Param: [materialManage, result] 
    * @return: com.mantoo.mtic.common.entity.RestResult<com.github.pagehelper.PageInfo<com.mantoo.mtic.module.system.entity.system.MaterialManage>> 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    @PostMapping("/findAllByCondition")
    @RequiresPermissions("material:list")
    public RestResult<PageInfo<MaterialManageVO>> getAll(@Valid @RequestBody MaterialManage materialManage, BindingResult result) {
        //简单参数校验
        if (result.hasErrors()) {
            return ResultUtils.genErrorResult(result.getAllErrors().get(0).getDefaultMessage());
        }
        List<MaterialManageVO> list = materialManageService.findByCondition(materialManage);
        //如果为空
        if (CollectionUtils.isEmpty(list)) {
            return ResultUtils.genSuccesNoDataResult(null);
        }
        return ResultUtils.genSuccesResult(ResultUtils.toPageInfo(list));
    }

    /*** 
    * @Description: 添加固件
    * @Param: [materialName, productId, file] 
    * @return: com.mantoo.mtic.common.entity.RestResult<java.lang.String> 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    @PostMapping("/addMaterialManage")
    @RequiresPermissions("material:add")
    public RestResult<String> addProductManage(@NotNull(message="资料名称不能为空") @RequestParam("materialName") String materialName,
                                               @NotNull(message="所属产品不能为空") @RequestParam("productId") Long productId,
                                               @NotNull(message="文件上传不能为空") @RequestParam(value="file") MultipartFile file
    ) {
        MaterialManage materialManage = new MaterialManage();
        materialManage.setMaterialName(materialName);
        materialManage.setProductId(productId);
        if(materialManageService.materialManageRepeat(materialManage)){
            return ResultUtils.genErrorResult("资料已经存在，请检查");
        }
        if(!productManageService.productManageIsRepeatableByProductId(productId)){
            return ResultUtils.genErrorResult("该资料所属的产品不存在");
        }
        //获得文件上传的url
        String path = materialManageService.materialManageFileUrl(file,materialManage);
        materialManage.setMaterialFile(path);
        //往数据库中添加信息
        try {
            //添加固件
            int i = materialManageService.addMaterialManage(materialManage);
        } catch (MticException e) {
            return ResultUtils.genErrorResult(e.getMessage(), e.getCode());
        }
        return ResultUtils.genSuccesResult(path);
    }


    /*** 
    * @Description: 修改固件 上传固件为空则修改文件名
    * @Param: [materialName, productId, file] 
    * @return: com.mantoo.mtic.common.entity.RestResult<java.lang.String> 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    @PostMapping("/editMaterialManage")
    @RequiresPermissions("material:update")
    public RestResult<String> editMaterialManage(
                                                 @NotNull(message="id不能为空") @RequestParam("materialId") Long materialId,
                                                 @NotNull(message="资料名称不能为空") @RequestParam("materialName") String materialName,
                                                 @NotNull(message="所属产品不能为空") @RequestParam("productId") Long productId,
                                                @RequestParam(value="file",required = false) MultipartFile file) {
        String path = null;
        //判断该固件是否已经存在
        MaterialManage materialManage = new MaterialManage();
        materialManage.setMaterialId(materialId);
        materialManage.setMaterialName(materialName);
        materialManage.setProductId(productId);

        if(Objects.isNull(materialManageMapper.selectByPrimaryKey(materialManage))){
            return ResultUtils.genErrorResult("该资料不存在");
        }

        if(!productManageService.productManageIsRepeatableByProductId(productId)){
            return ResultUtils.genErrorResult("该资料所属的产品不存在");
        }

        //如果文件上传不为空，则表示需要修改文件,如果文件为空，则修改文件名称
        if(!Objects.isNull(file)){
            path = materialManageService.materialManageFileUrl(file,materialManage);
            materialManage.setMaterialFile(path);
        }else{
            //更新文件url
            String url = materialManageService.updateFileName(materialManage);
            materialManage.setMaterialFile(url);
        }

        //往数据库中添加信息
        try {
            //修改固件信息
            int i = materialManageService.editMaterialManage(materialManage);
        } catch (MticException e) {
            return ResultUtils.genErrorResult(e.getMessage(), e.getCode());
        }
        //返回文件上传的路径
        return ResultUtils.genSuccesResult(path);
    }

    /*** 
    * @Description: 删除固件
    * @Param: [materialManage, result] 
    * @return: com.mantoo.mtic.common.entity.RestResult<java.lang.String> 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    @PostMapping("/deleteMaterialManage")
    @RequiresPermissions("material:del")
    public RestResult<String> deleteProductManage(@Valid @RequestBody MaterialManage materialManage, BindingResult result) {
        //简单参数校验
        if (result.hasErrors()) {
            return ResultUtils.genErrorResult(result.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            //删除固件
            int i = materialManageService.deleteMaterialManage(materialManage);
        } catch (MticException e) {
            return ResultUtils.genErrorResult(e.getMessage(), e.getCode());
        }
        return ResultUtils.genSuccesResult();
    }


}
