package com.mantoo.mtic.module.system.data;

import com.mantoo.mtic.module.system.entity.OpenMenu;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;
import javax.validation.Valid;
import java.util.List;

/**
 * @ClassName: ExOpenMenu
 * @Description: 一级菜单
 * @Author: renjt
 * @Date: 2019-11-28 10:49
 */
@Setter
@Getter
public class ExOpenMenu extends OpenMenu {

    /**
     * 页签列表
     */
    @Transient
    @Valid
    private List<TabMenu> tabMenuList;

}
