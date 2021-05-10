package com.mantoo.mtic.module.system.service.system.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.mantoo.mtic.common.utils.UserUtil;
import com.mantoo.mtic.module.system.entity.vo.ProductManageVO;
import com.mantoo.mtic.module.system.entity.system.ProductManage;
import com.mantoo.mtic.module.system.exMapper.system.ProductManageMapper;
import com.mantoo.mtic.module.system.mapper.SysUserMapper;
import com.mantoo.mtic.module.system.service.system.IProductManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName: ProductManageServiceImpl
 * @Author: ghy
 * @Date: 2021-04-08 13:13
 */
@Service
@Transactional
public class ProductManageServiceImpl implements IProductManageService {

    @Autowired
    private ProductManageMapper productManageMapper;

    @Autowired
    private SysUserMapper sysUserMapper;


    /*** 
    * @Description: 查询所有产品型号
     * @Param: [] 
    * @return: java.util.List<com.mantoo.mtic.module.system.entity.vo.ProductManageVO>
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    @Override
    public List<ProductManageVO> getProductCodeList() {
        List<ProductManage> list = productManageMapper.getProductCodeList();
        List<ProductManageVO> listVO = new ArrayList<ProductManageVO>();
        //通过循环拷贝属性到VO中
        list.stream().forEach(
                (x)->{
                    ProductManageVO productManageVO  = new ProductManageVO();
                    BeanUtil.copyProperties(x,productManageVO);
                    listVO.add(productManageVO);
                }
        );
        return listVO;
    }

    

    /*** 
    * @Description: 条件模糊查询产品 
    * @Param: [productManage] 
    * @return: java.util.List<com.mantoo.mtic.module.system.entity.system.ProductManage> 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    @Override
    public List<ProductManage> findByCondition(ProductManage productManage) {
        //判断页码和记录数是否为空
        if (!Objects.isNull( productManage.getPageNum())&&!Objects.isNull(productManage.getPageSize())) {
            PageHelper.startPage(productManage.getPageNum(), productManage.getPageSize());
        }
        return productManageMapper.selectProductManageListWithCondition(productManage);
    }

    /*** 
    * @Description: 删除产品，将deleteFlag改为1 
    * @Param: [productManage] 
    * @return: java.lang.Integer 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    @Override
    public Integer deleteProductManage(ProductManage productManage) {
        //添加更新者信息与时间
        productManage.setUpdateTime(new Date());
        productManage.setUpdateBy(UserUtil.getUserId());
        return productManageMapper.deleteProductManage(productManage);
    }

    /*** 
    * @Description: 判断产品是否重复
    * @Param: [productManage] 
    * @return: java.lang.Boolean true表示重复，false表示不重复
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    @Override
    public Boolean productManageIsRepeatable(ProductManage productManage) {
        //如果不重复返回false
        if(!Objects.isNull(productManageMapper.selectProductManageByProductCode(productManage))) {
            return true;
        }else{
            return false;
        }
    }

    /***
    * @Description: 添加产品
    * @Param: [productManage]
    * @return: java.lang.Integer
    * @Author: ghy
    * @Date: 2021/4/23
    */
    @Override
    public Integer addProductManage(ProductManage productManage) {
        //默认生产状态为0
        productManage.setDeleteFlag(0);
        //添加产品的创建时间
        productManage.setCreateTime(new Date());
        //添加产品的更新、创建时间和更新、创建者信息
        productManage.setCreateBy(UserUtil.getUserId());
        productManage.setCreateTime(new Date());
        productManage.setUpdateTime(new Date());
        productManage.setUpdateBy(UserUtil.getUserId());
        return productManageMapper.insert(productManage);
    }

    /*** 
    * @Description: 更新产品 
    * @Param: [productManage] 
    * @return: java.lang.Integer 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    @Override
    public Integer updateProductManage(ProductManage productManage) {
        //添加产品的更新时间和更新者信息
        productManage.setUpdateBy(UserUtil.getUserId());
        productManage.setUpdateTime(new Date());
        return productManageMapper.updateProductManageInfo(productManage);
    }

    /*** 
    * @Description: 按照产品id查询产品，为true则表示重复，如果为false表示不重复 
    * @Param: [productId] 
    * @return: java.lang.Boolean 
    * @Author: ghy
    * @Date: 2021/4/23 
    */
    @Override
    public Boolean productManageIsRepeatableByProductId(Long productId) {
        //如果查询出来的为null，则标识数据库中没有这个产品
        if(Objects.isNull(productManageMapper.selectByPrimaryKey(productId))){
            return false;
        }else{
            return true;
        }
    }
}
