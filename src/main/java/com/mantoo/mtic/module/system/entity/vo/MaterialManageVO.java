package com.mantoo.mtic.module.system.entity.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @Description:
 * @ClassName: MaterialManageVO
 * @Author: ghy
 * @Date: 2021-04-26 09:19
 */
@Getter
@Setter
@ToString
public class MaterialManageVO {
    /**
     * 资料id
     */
    private Long materialId;

    /**
     * 资料名称
     */
    private String materialName;

    /**
     * 产品型号
     */
    private String productCode;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 资料文件
     */
    private String materialFile;

    /**
     * 产品Id
     */
    private  Long productId;
}
