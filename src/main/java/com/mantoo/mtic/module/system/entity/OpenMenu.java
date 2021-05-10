package com.mantoo.mtic.module.system.entity;

import com.mantoo.mtic.common.entity.BaseEntity;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "open_menu")
public class OpenMenu extends BaseEntity {
    /**
     * 菜单ID
     */
    @Id
    @Column(name = "menu_id")
    private String menuId;

    /**
     * 父级id
     */
    @Column(name = "parent_id")
    private String parentId;

    /**
     * 所有父级id
     */
    @Column(name = "parent_ids")
    private String parentIds;

    /**
     * 名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 类型（1菜单，2页签）
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 排序（预留）
     */
    @Column(name = "sort")
    private Integer sort;

    /**
     * 连接地址
     */
    @Column(name = "href")
    private String href;

    /**
     * 目标（预留）
     */
    @Column(name = "target")
    private String target;

    /**
     * 图标样式
     */
    @Column(name = "icon")
    private String icon;

    /**
     * 是否显示
     */
    @Column(name = "is_show")
    private Integer isShow;
}