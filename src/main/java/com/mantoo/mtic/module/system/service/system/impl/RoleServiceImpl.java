package com.mantoo.mtic.module.system.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.mantoo.mtic.common.utils.DateUtils;
import com.mantoo.mtic.common.utils.StringUtil;
import com.mantoo.mtic.exception.ErrorInfo;
import com.mantoo.mtic.exception.MticException;
import com.mantoo.mtic.module.system.data.ExOpenMenu;
import com.mantoo.mtic.module.system.data.ExSysRole;
import com.mantoo.mtic.module.system.entity.Permission;
import com.mantoo.mtic.module.system.entity.SysRole;
import com.mantoo.mtic.module.system.entity.SysRolePermission;
import com.mantoo.mtic.module.system.entity.SysUser;
import com.mantoo.mtic.module.system.exMapper.ExOpenMenuMapper;
import com.mantoo.mtic.module.system.exMapper.ExPermissionMapper;
import com.mantoo.mtic.module.system.mapper.SysRoleMapper;
import com.mantoo.mtic.module.system.mapper.SysRolePermissionMapper;
import com.mantoo.mtic.module.system.mapper.SysUserMapper;
import com.mantoo.mtic.module.system.service.system.IRoleService;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @ClassName: RoleServiceImpl
 * @Description: 角色service
 * @Author: renjt
 * @Date: 2019-11-27 16:52
 */
@Service
@Transactional
public class RoleServiceImpl implements IRoleService {

    @Autowired
    SysRoleMapper sysRoleMapper;
    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    ExOpenMenuMapper exOpenMenuMapper;
    @Autowired
    SysRolePermissionMapper rolePermissionMapper;
    @Autowired
    ExPermissionMapper exPermissionMapper;

    /**
     * @return java.util.List<com.mantoo.mtic.module.system.entity.SysRole>
     * @Description 查询角色列表
     * @Param [sysRole]
     * @Author renjt
     * @Date 2019-11-27 16:52
     */
    @Override
    public List<SysRole> getPageList(SysRole sysRole) {
        if (sysRole.getPageNum() != null && sysRole.getPageSize() != null) {
            PageHelper.startPage(sysRole.getPageNum(), sysRole.getPageSize());
        }
        Example example = new Example(SysRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("deleteFlag", SysRole.DELETE_FLAG_NORMAL);
        if (StringUtil.isNotBlank(sysRole.getRoleName())) {
            criteria.andLike("roleName", "%" + sysRole.getRoleName() + "%");
        }
        if (null != sysRole.getRoleType()) {
            criteria.andEqualTo("roleType", sysRole.getRoleType());
        }
        example.setOrderByClause("create_date DESC");
        return sysRoleMapper.selectByExample(example);
    }

    /**
     * @return int
     * @Description 新增角色
     * @Author renjt
     * @Date 2019-11-27 16:49
     */
    @Override
    public int addRole(SysRole sysRole, Long userId) {
        SysRole existRole = getRoleByRoleName(sysRole.getRoleName());
        if (existRole != null) {
            throw new MticException("该角色已存在！", ErrorInfo.EXIST.getCode());
        }
        sysRole.setCreateBy(userId);
        sysRole.setCreateDate(DateUtils.getNowDate());
        return sysRoleMapper.insertSelective(sysRole);
    }

    /**
     * @return int
     * @Description 编辑角色
     * @Author renjt
     * @Date 2019-11-27 16:52
     */
    @Override
    public int updateRole(SysRole sysRole, Long userId) {
        // 判断角色名称是否重复
        Example example = new Example(SysRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleName", sysRole.getRoleName());
        criteria.andNotEqualTo("roleId", sysRole.getRoleId());
        criteria.andEqualTo("deleteFlag", SysRole.DELETE_FLAG_NORMAL);
        List<SysRole> roleList = sysRoleMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(roleList)) {
            throw new MticException("该角色已存在！", ErrorInfo.EXIST.getCode());
        }
        sysRole.setUpdateBy(userId);
        sysRole.setUpdateDate(DateUtils.getNowDate());
        return sysRoleMapper.updateByPrimaryKeySelective(sysRole);
    }

    /**
     * @return int
     * @Description 删除角色
     * @Author renjt
     * @Date 2019-11-27 16:52
     */
    @Override
    public int delRole(SysRole sysRole, Long userId) {
        // 判断该角色是否有用户关联
        SysUser sysUser = new SysUser();
        sysUser.setRoleId(sysRole.getRoleId());
        sysUser.setDeleteFlag(SysUser.DELETE_FLAG_NORMAL);
        List<SysUser> userList = sysUserMapper.select(sysUser);
        if (!CollectionUtils.isEmpty(userList)) {
            throw new MticException(ErrorInfo.EXIST_RELATED_USER.getMsg(), ErrorInfo.EXIST_RELATED_USER.getCode());
        }
        sysRole.setUpdateBy(userId);
        sysRole.setUpdateDate(DateUtils.getNowDate());
        sysRole.setDeleteFlag(SysRole.DELETE_FLAG_DELETED);
        return sysRoleMapper.updateByPrimaryKeySelective(sysRole);
    }

    /**
     * @return java.util.List<com.mantoo.mtic.module.system.data.ExOpenMenu>
     * @Description 根据角色获取菜单权限列表
     * @Param [roleId]
     * @Author renjt
     * @Date 2019-11-28 13:09
     */
    @Override
    public List<ExOpenMenu> getMenuListByRole(Long roleId) {
        return exOpenMenuMapper.getMenuListByRole(roleId);
    }

    /**
     * @return java.util.List<com.mantoo.mtic.module.system.data.ExOpenMenu>
     * @Description 根据角色获取菜单列表（没有权限列表），登录时调用
     * @Param [roleId]
     * @Author renjt
     * @Date 2020-4-1 15:51
     */
    @Override
    public List<ExOpenMenu> getMenuListByLogin(Long roleId) {
        return exOpenMenuMapper.getMenuListByLogin(roleId);
    }

    /**
     * @return void
     * @Description 权限配置
     * @Param [exSysRole]
     * @Author renjt
     * @Date 2019-11-28 16:17
     */
    @Override
    public void permissionConf(ExSysRole exSysRole) {
        // 先删除再新增
        Long roleId = exSysRole.getRoleId();
        SysRolePermission rolePermission = new SysRolePermission();
        rolePermission.setRoleId(roleId);
        rolePermissionMapper.delete(rolePermission);

        List<SysRolePermission> rolePermissionList = exSysRole.getRolePermissionList();
        if (CollectionUtils.isEmpty(rolePermissionList)) {
            return;
        }
        for (SysRolePermission sysRolePermission : rolePermissionList) {
            sysRolePermission.setRoleId(roleId);
        }
        rolePermissionMapper.insertList(rolePermissionList);
    }

    /**
     * @return java.util.List<com.mantoo.mtic.module.system.entity.Permission>
     * @Description 根据角色id获取权限list
     * @Param [roleId]
     * @Author renjt
     * @Date 2020-4-1 15:25
     */
    @Override
    public List<Permission> getPermissionsByRoleId(Long roleId) {
        return exPermissionMapper.getPermissionsByRoleId(roleId);
    }

    /**
     * @return com.mantoo.mtic.module.system.entity.SysRole
     * @Description 根据角色名查询（未删除）角色
     * @Param [roleName]
     * @Author renjt
     * @Date 2019-11-27 18:07
     */
    private SysRole getRoleByRoleName(String roleName) {
        SysRole sysRole = new SysRole();
        sysRole.setRoleName(roleName);
        sysRole.setDeleteFlag(SysRole.DELETE_FLAG_NORMAL);
        SysRole existRole;
        try {
            existRole = sysRoleMapper.selectOne(sysRole);
        } catch (Exception e) {
            throw new MticException(ErrorInfo.EXIST_MORE.getMsg(), ErrorInfo.EXIST_MORE.getCode());
        }
        return existRole;
    }
}
