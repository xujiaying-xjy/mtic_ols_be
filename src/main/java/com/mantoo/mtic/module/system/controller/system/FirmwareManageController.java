package com.mantoo.mtic.module.system.controller.system;

import cn.hutool.core.io.FileTypeUtil;
import com.github.pagehelper.PageInfo;
import com.mantoo.mtic.common.entity.RestResult;
import com.mantoo.mtic.common.entity.ResultUtils;
import com.mantoo.mtic.exception.MticException;
import com.mantoo.mtic.module.system.entity.system.FirmwareManage;
import com.mantoo.mtic.module.system.entity.system.MaterialManage;
import com.mantoo.mtic.module.system.entity.system.ProductManage;
import com.mantoo.mtic.module.system.entity.vo.FirmwareManageVO;
import com.mantoo.mtic.module.system.exMapper.system.FirmwareManageMapper;
import com.mantoo.mtic.module.system.service.system.IFirmwareManageService;
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
 * @Description 固件管理controller
 * @ClassName: FirmwareManageController
 * @Author: ghy
 * @Date: 2021-04-20 10:02
 */
@RestController
@RequestMapping("/web/firmwareManage")
public class FirmwareManageController {

    @Autowired
    private IFirmwareManageService firmwareManageService;

    @Autowired
    private IProductManageService productManageService;

    @Autowired
    private FirmwareManageMapper firmwareManageMapper;
    /*** 
    * @Description: 条件模糊查询
    * @Param: [firmwareManage, result] 
    * @return: com.mantoo.mtic.common.entity.RestResult<com.github.pagehelper.PageInfo<com.mantoo.mtic.module.system.entity.vo.FirmwareManageVO>> 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    @PostMapping("/findAllByCondition")
    @RequiresPermissions("firmware:list")
    public RestResult<PageInfo<FirmwareManageVO>> getAll(@Valid @RequestBody FirmwareManage firmwareManage, BindingResult result) {
        //简单参数校验
        if (result.hasErrors()) {
            return ResultUtils.genErrorResult(result.getAllErrors().get(0).getDefaultMessage());
        }
        //模糊查询
        List<FirmwareManageVO> list = firmwareManageService.findByCondition(firmwareManage);
        //如果为空
        if (CollectionUtils.isEmpty(list)) {
            return ResultUtils.genSuccesNoDataResult(null);
        }
        return ResultUtils.genSuccesResult(ResultUtils.toPageInfo(list));
    }

    /**
     * 修改固件
     * @param firmwareName
     * @param firmwareVersion
     * @param productId
     * @param file
     * @param firmwareDescribe
     * @return
     */
    @PostMapping("/editFirmwareManage")
    @RequiresPermissions("firmware:update")
    public RestResult<String> editFirmwareManage(
                                                 @NotNull(message="固件id不能为空")  @RequestParam("firmwareId") Long firmwareId,
                                                 @NotNull(message="固件名称不能为空") @RequestParam("firmwareName") String firmwareName,
                                                 @NotNull(message="固件版本不能为空") @RequestParam("firmwareVersion") String firmwareVersion,
                                                 @NotNull(message="所属产品不能为空") @RequestParam("productId") Long productId,
                                                 @RequestParam(value="file",required = false) MultipartFile file,
                                                 @RequestParam(value="firmwareDescribe",required = false) String firmwareDescribe) {
        String path=null;
        FirmwareManage firmwareManage = new FirmwareManage();
        firmwareManage.setFirmwareId(firmwareId);
        firmwareManage.setFirmwareName(firmwareName);
        firmwareManage.setFirmwareVersion(firmwareVersion);
        firmwareManage.setProductId(productId);
        //判断该固件是否已经存在
        if(Objects.isNull(firmwareManageMapper.selectByPrimaryKey(firmwareManage))){
            return ResultUtils.genErrorResult("该固件不存在");
        }
        if(!productManageService.productManageIsRepeatableByProductId(productId)){
            return ResultUtils.genErrorResult("该固件所属的产品不存在");
        }
        //如果描述不为空则添加描述
        if(!Objects.isNull(firmwareDescribe)){
            firmwareManage.setFirmwareDescribe(firmwareDescribe);
        }
        if(!Objects.isNull(file)){
            firmwareManage.setFirmwareSize((int) file.getSize());
            //保存文件并返回url
             path = firmwareManageService.firmwareManageFileUrl(file,firmwareManage);
            firmwareManage.setFirmwareFile(path);
        }
        //往数据库中添加信息
        try {
            //修改固件信息
            int i = firmwareManageService.editFirmwareManage(firmwareManage);
        } catch (MticException e) {
            return ResultUtils.genErrorResult(e.getMessage(), e.getCode());
        }
        //返回文件上传的路径
        return ResultUtils.genSuccesResult(path);
    }


    /** 
    * @Description:
    * @Param: [firmwareName, firmwareVersion, productId, file, firmwareDescribe] 
    * @return: com.mantoo.mtic.common.entity.RestResult<java.lang.String> 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    @PostMapping("/addFirmwareManage")
    @RequiresPermissions("firmware:add")
    public RestResult<String> addProductManage(
                                                @NotNull(message="固件名称不能为空") @RequestParam("firmwareName") String firmwareName,
                                                @NotNull(message="固件版本不能为空") @RequestParam("firmwareVersion") String firmwareVersion,
                                                @NotNull(message="所属产品不能为空") @RequestParam("productId") Long productId,
                                                @NotNull(message="上传文件不能为空") @RequestParam("file") MultipartFile file,
                                                @RequestParam(value = "firmwareDescribe",required = false) String firmwareDescribe
                                               ) {
        //判断该固件是否已经存在
        FirmwareManage firmwareManage = new FirmwareManage();
        firmwareManage.setFirmwareName(firmwareName);
        firmwareManage.setFirmwareVersion(firmwareVersion);
        firmwareManage.setProductId(productId);
        firmwareManage.setFirmwareSize((int) file.getSize());
        if(!Objects.isNull(firmwareDescribe)){
            firmwareManage.setFirmwareDescribe(firmwareDescribe);
        }
        if(firmwareManageService.firmwareManageRepeat(firmwareManage)){
        }
        if(!productManageService.productManageIsRepeatableByProductId(productId)){
            return ResultUtils.genErrorResult("该固件所属的产品不存在");
        }
        //获得文件上传的url
        String path = firmwareManageService.firmwareManageFileUrl(file,firmwareManage);
        firmwareManage.setFirmwareFile(path);
        //往数据库中添加信息
        try {
            //添加固件
            int i = firmwareManageService.addFirmwareManage(firmwareManage);
        } catch (MticException e) {
            return ResultUtils.genErrorResult(e.getMessage(), e.getCode());
        }
        return ResultUtils.genSuccesResult(path);
    }


    /**
     * 删除固件
     * @param firmwareManage
     * @param result
     * @return
     */
    @PostMapping("/deleteFirmwareManage")
    @RequiresPermissions("firmware:del")
    public RestResult<String> deleteProductManage(@Valid @RequestBody FirmwareManage firmwareManage, BindingResult result) {
        //简单参数校验
        if (result.hasErrors()) {
            return ResultUtils.genErrorResult(result.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            //删除固件
            int i = firmwareManageService.deleteFirmwareManage(firmwareManage);
        } catch (MticException e) {
            return ResultUtils.genErrorResult(e.getMessage(), e.getCode());
        }
        return ResultUtils.genSuccesResult();
    }

    //产品查固件
    @PostMapping("/getFirmwareManageByProductMange")
    @RequiresPermissions("firmware:list")
    public RestResult<List<FirmwareManage>> getFirmwareManageByProductMange(@Valid @RequestBody ProductManage productManage, BindingResult result){
        //简单参数校验
        if (result.hasErrors()) {
            return ResultUtils.genErrorResult(result.getAllErrors().get(0).getDefaultMessage());
        }
        //根据产品id查询固件
        List<FirmwareManage> list = firmwareManageService.getFirmwareManageByProductMange(productManage.getProductId());
        //如果为空
        if (CollectionUtils.isEmpty(list)) {
            return ResultUtils.genSuccesNoDataResult(null);
        }
        return ResultUtils.genSuccesResult(list);
    }
}
