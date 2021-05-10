package com.mantoo.mtic.module.system.entity.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 用于返回ProductManage类型VO
 * @ClassName: ProductManageVO
 * @Author: ghy
 * @Date: 2021-04-23 11:15
 */
@Data
@Getter
@Setter
public class ProductManageVO{
    /**
     * 商品Id
     */
    private Long productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品编号
     */
    private String productCode;


}
