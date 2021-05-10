package com.mantoo.mtic.module.system.controller.system;

import com.github.pagehelper.PageInfo;
import com.mantoo.mtic.common.entity.RestResult;
import com.mantoo.mtic.common.entity.ResultUtils;
import com.mantoo.mtic.common.utils.StringUtil;
import com.mantoo.mtic.common.utils.UserUtil;
import com.mantoo.mtic.exception.MticException;
import com.mantoo.mtic.module.system.data.ExOpenMenu;
import com.mantoo.mtic.module.system.data.ExSysRole;
import com.mantoo.mtic.module.system.data.TabMenu;
import com.mantoo.mtic.module.system.data.TreeModel;
import com.mantoo.mtic.module.system.entity.Permission;
import com.mantoo.mtic.module.system.entity.SysRole;
import com.mantoo.mtic.module.system.entity.SysRolePermission;
import com.mantoo.mtic.module.system.service.system.IRoleService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: RoleController
 * @Description: 系统角色controller
 * @Author: renjt
 * @Date: 2019-11-27 16:55
 */
@RestController
@RequestMapping("/web/role")
public class RoleController {

    @Autowired
    IRoleService roleService;

    /**
     * @return com.mantoo.mtic.common.entity.RestResult<com.github.pagehelper.PageInfo<com.mantoo.mtic.module.system.entity.SysRole>>
     * @Description 分页查询角色列表
     * @Param [sysRole, result]
     * @Author renjt
     * @Date 2019-11-29 19:02
     */
    @PostMapping("/getPageList")
    @RequiresPermissions(value={"role:list","user:list"},logical= Logical.OR)
    public RestResult<PageInfo<SysRole>> getPageList(@Valid @RequestBody SysRole sysRole, BindingResult result){
        //简单参数校验
        if (result.hasErrors()) {
            return ResultUtils.genErrorResult(result.getAllErrors().get(0).getDefaultMessage());
        }
        List<SysRole> roleList = roleService.getPageList(sysRole);
        if (CollectionUtils.isEmpty(roleList)) {
            return ResultUtils.genSuccesNoDataResult(null);
        }
        return ResultUtils.genSuccesResult(ResultUtils.toPageInfo(roleList));
    }

    /**
     * @return com.mantoo.mtic.common.entity.RestResult
     * @Description 新增角色
     * @Param [sysRole, result]
     * @Author renjt
     * @Date 2019-11-29 19:03
     */
    @PostMapping("/addRole")
    @RequiresPermissions("role:add")
    public RestResult<String> addRole(@Valid @RequestBody SysRole sysRole, BindingResult result) {
        //简单参数校验
        if (result.hasErrors()) {
            return ResultUtils.genErrorResult(result.getAllErrors().get(0).getDefaultMessage());
        }
        // 非空校验
        RestResult<String> restResult = notNullUtil(sysRole);
        if (restResult != null) {
            return restResult;
        }
        int i;
        try {
            i = roleService.addRole(sysRole, UserUtil.getUserId());
        } catch (MticException e) {
            return ResultUtils.genErrorResult(e.getMessage(), e.getCode());
        }
        if (i != 0) {
            // 添加操作日志
            // ...
            return ResultUtils.genSuccesResult();
        } else {
            return ResultUtils.genErrorResult("新增角色失败");
        }
    }

    /**
     * @return com.mantoo.mtic.common.entity.RestResult
     * @Description 编辑角色
     * @Param [sysRole, result]
     * @Author renjt
     * @Date 2019-11-29 19:03
     */
    @PostMapping("/updateRole")
    @RequiresPermissions("role:update")
    public RestResult<String> updateRole(@Valid @RequestBody SysRole sysRole, BindingResult result) {
        //简单参数校验
        if (result.hasErrors()) {
            return ResultUtils.genErrorResult(result.getAllErrors().get(0).getDefaultMessage());
        }
        // 非空校验
        RestResult<String> restResult = notNullUtil(sysRole);
        if (restResult != null) {
            return restResult;
        }
        if (sysRole.getRoleId() == null) {
            return ResultUtils.genErrorResult("roleId不能为空");
        }
        int i ;
        try {
            i = roleService.updateRole(sysRole, UserUtil.getUserId());
        } catch (MticException e) {
            return ResultUtils.genErrorResult(e.getMessage(), e.getCode());
        }
        if (i != 0) {
            // 添加操作日志
            // ...
            return ResultUtils.genSuccesResult();
        } else {
            return ResultUtils.genErrorResult("编辑角色失败");
        }
    }

    /**
     * @return com.mantoo.mtic.common.entity.RestResult
     * @Description 删除角色
     * @Param [sysRole, result]
     * @Author renjt
     * @Date 2019-11-29 19:03
     */
    @PostMapping("/delRole")
    @RequiresPermissions("role:del")
    public RestResult<String> delRole(@Valid @RequestBody SysRole sysRole, BindingResult result) {
        //简单参数校验
        if (result.hasErrors()) {
            return ResultUtils.genErrorResult(result.getAllErrors().get(0).getDefaultMessage());
        }
        // 非空校验
        if (sysRole.getRoleId() == null) {
            return ResultUtils.genErrorResult("roleId不能为空");
        }
        int i;
        try {
            i = roleService.delRole(sysRole, UserUtil.getUserId());
        } catch (MticException e) {
            return ResultUtils.genErrorResult(e.getMessage(), e.getCode());
        }
        if (i != 0) {
            // 添加操作日志
            // ...
            return ResultUtils.genSuccesResult();
        } else {
            return ResultUtils.genErrorResult("删除角色失败");
        }
    }

    /**
     * @return com.mantoo.mtic.common.entity.RestResult<java.util.List<com.mantoo.mtic.module.system.data.ExOpenMenu>>
     * @Description 根据角色获取菜单权限列表
     * @Param [sysRole, result]
     * @Author renjt
     * @Date 2019-11-29 19:03
     */
    @PostMapping("/getPermissionsByRoleId")
    @RequiresPermissions("role:permissionConf")
    public RestResult<List<Permission>> getPermissionsByRoleId(@Valid @RequestBody SysRole sysRole, BindingResult result) {
        //简单参数校验
        if (result.hasErrors()) {
            return ResultUtils.genErrorResult(result.getAllErrors().get(0).getDefaultMessage());
        }
        // 非空校验
        if (sysRole.getRoleId() == null) {
            return ResultUtils.genErrorResult("roleId不能为空");
        }
        List<Permission> permissionList = roleService.getPermissionsByRoleId(sysRole.getRoleId());
        if (CollectionUtils.isEmpty(permissionList)) {
            return ResultUtils.genSuccesNoDataResult(null);
        }
        return ResultUtils.genSuccesResult(permissionList);
    }

    /**
     * @return com.mantoo.mtic.common.entity.RestResult<java.util.List<com.mantoo.mtic.module.system.data.ExOpenMenu>>
     * @Description 获取系统全部菜单权限列表
     * @Param []
     * @Author renjt
     * @Date 2019-11-29 19:03
     */
    @PostMapping("/getAllMenuList")
    @RequiresPermissions("role:permissionConf")
    public RestResult<List<TreeModel>> getAllMenuList() {
        List<ExOpenMenu> menuList = roleService.getMenuListByRole(null);
        List<TreeModel> treeModelList = treeConver(menuList);
        if (CollectionUtils.isEmpty(treeModelList)) {
            return ResultUtils.genSuccesNoDataResult(null);
        }
        return ResultUtils.genSuccesResult(treeModelList);
    }

    /**
     * @return com.mantoo.mtic.common.entity.RestResult
     * @Description 为角色配置权限
     * @Param [exSysRole, result]
     * @Author renjt
     * @Date 2019-11-29 19:04
     */
    @PostMapping("/permissionConf")
    @RequiresPermissions("role:permissionConf")
    public RestResult<String> permissionConf(@Valid @RequestBody ExSysRole exSysRole, BindingResult result) {
        //简单参数校验
        if (result.hasErrors()) {
            return ResultUtils.genErrorResult(result.getAllErrors().get(0).getDefaultMessage());
        }
        // 非空校验
        if (exSysRole.getRoleId() == null) {
            return ResultUtils.genErrorResult("roleId不能为空");
        }
        List<SysRolePermission> rolePermissionList = exSysRole.getRolePermissionList();
        if (!CollectionUtils.isEmpty(rolePermissionList)) {
            for (SysRolePermission rolePermission : rolePermissionList) {
                if (rolePermission.getPermissionId() == null) {
                    return ResultUtils.genErrorResult("permissionId不能为空");
                }
            }
        }
        try {
            roleService.permissionConf(exSysRole);
        } catch (MticException e) {
            return ResultUtils.genErrorResult(e.getMessage(), e.getCode());
        }
        return ResultUtils.genSuccesResult();
    }

    /**
     * @return com.mantoo.mtic.common.entity.RestResult
     * @Description 非空校验工具
     * @Param [sysRole]
     * @Author renjt
     * @Date 2019-11-27 18:12
     */
    private RestResult<String> notNullUtil(SysRole sysRole) {
        if (StringUtil.isBlank(sysRole.getRoleName())) {
            return ResultUtils.genErrorResult("roleName不能为空！");
        }
        if (sysRole.getRoleType() == null) {
            return ResultUtils.genErrorResult("rotype不能为空！");
        }
        return null;
    }

    /**
     * @return java.util.List<com.mantoo.mtic.module.system.data.TreeModel>
     * @Description 转换树形结构
     * @Param [menuList]
     * @Author renjt
     * @Date 2020-4-3 17:14
     */
    private List<TreeModel> treeConver(List<ExOpenMenu> menuList){
        List<TreeModel> oneList = new ArrayList<>();
        // menuList：一级
        for (ExOpenMenu exOpenMenu : menuList) {
            TreeModel oneTreeModel = new TreeModel();
            oneTreeModel.setId(exOpenMenu.getMenuId());
            oneTreeModel.setLabel(exOpenMenu.getName());
            // tabMenuList：二级
            List<TreeModel> twoList = new ArrayList<>();
            List<TabMenu> tabMenuList = exOpenMenu.getTabMenuList();
            for (TabMenu tabMenu : tabMenuList) {
                TreeModel twoTreeModel = new TreeModel();
                twoTreeModel.setId(tabMenu.getMenuId());
                twoTreeModel.setLabel(tabMenu.getName());
                // permissionList：三级
                List<TreeModel> threeList = new ArrayList<>();
                List<Permission> permissionList = tabMenu.getPermissionList();
                for (Permission permission : permissionList) {
                    TreeModel threeTreeModel = new TreeModel();
                    threeTreeModel.setId(permission.getPermissionId().toString());
                    threeTreeModel.setLabel(permission.getPermissionName());
                    threeList.add(threeTreeModel);
                }
                twoTreeModel.setChildren(threeList);
                twoList.add(twoTreeModel);
            }
            oneTreeModel.setChildren(twoList);
            oneList.add(oneTreeModel);
        }
        return oneList;
    }
}
