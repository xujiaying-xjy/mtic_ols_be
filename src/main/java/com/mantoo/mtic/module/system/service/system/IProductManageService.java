package com.mantoo.mtic.module.system.service.system;


import com.mantoo.mtic.module.system.entity.vo.ProductManageVO;
import com.mantoo.mtic.module.system.entity.system.ProductManage;

import java.util.List;

/**
 * @ClassName: IProductService
 * @Author: ghy
 * @Date: 2021-04-08 13:13
 */
public interface IProductManageService{

    /*** 
    * @Description: 获取产品类型列表 
    * @Param: [] 
    * @return: java.util.List<com.mantoo.mtic.module.system.entity.system.ProductManage> 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    List<ProductManageVO> getProductCodeList();

    /*** 
    * @Description: 条件模糊查询产品 
    * @Param: [productManage] 
    * @return: java.util.List<com.mantoo.mtic.module.system.entity.system.ProductManage> 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    List<ProductManage> findByCondition(ProductManage productManage);

    /*** 
    * @Description: 删除产品 
    * @Param: [productManage] 
    * @return: java.lang.Integer 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    Integer deleteProductManage(ProductManage productManage);

    /*** 
    * @Description: 判断产品是否重复 
    * @Param: [productManage] 
    * @return: java.lang.Boolean 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    Boolean productManageIsRepeatable(ProductManage productManage);

    /*** 
    * @Description: 添加产品 
    * @Param: [productManage] 
    * @return: java.lang.Integer 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    Integer addProductManage(ProductManage productManage);

    /*** 
    * @Description: 更新产品 
    * @Param: [productManage] 
    * @return: java.lang.Integer 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    Integer updateProductManage(ProductManage productManage);

    /*** 
    * @Description: 通过产品编号查询产品是否重复 
    * @Param: [productId] 
    * @return: java.lang.Boolean 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    Boolean productManageIsRepeatableByProductId(Long productId);
}
