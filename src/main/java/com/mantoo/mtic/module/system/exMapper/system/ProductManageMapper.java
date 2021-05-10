package com.mantoo.mtic.module.system.exMapper.system;

import com.mantoo.mtic.common.mapper.MyMapper;
import com.mantoo.mtic.module.system.entity.system.ProductManage;

import java.util.List;

public interface ProductManageMapper extends MyMapper<ProductManage> {

    /*** 
    * @Description: 条件模糊查询产品 
    * @Param: [productManage] 
    * @return: java.util.List<com.mantoo.mtic.module.system.entity.system.ProductManage> 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    List<ProductManage> selectProductManageListWithCondition(ProductManage productManage);

    /*** 
    * @Description: 按照产品型号查询产品 
    * @Param: [productManage] 
    * @return: com.mantoo.mtic.module.system.entity.system.ProductManage 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    ProductManage  selectProductManageByProductCode(ProductManage productManage);

    /*** 
    * @Description: 更新产品 
    * @Param: [productManage] 
    * @return: java.lang.Integer 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    Integer updateProductManageInfo(ProductManage productManage);

    /*** 
    * @Description: 删除产品 
    * @Param: [productManage] 
    * @return: java.lang.Integer 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    Integer  deleteProductManage(ProductManage productManage);

    /*** 
    * @Description: 查询所有产品型号
    * @Param: [] 
    * @return: java.util.List<com.mantoo.mtic.module.system.entity.system.ProductManage> 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    List<ProductManage>  getProductCodeList();
}