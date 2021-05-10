package com.mantoo.mtic.module.system.data;

import com.mantoo.mtic.module.system.entity.OpenMenu;
import com.mantoo.mtic.module.system.entity.Permission;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;
import javax.validation.Valid;
import java.util.List;

/**
 * @ClassName: TabMenu
 * @Description: 二级菜单
 * @Author: renjt
 * @Date: 2019-11-28 11:29
 */
@Setter
@Getter
public class TabMenu extends OpenMenu {
    /**
     * 权限列表
     */
    @Transient
    @Valid
    private List<Permission> permissionList;
}
