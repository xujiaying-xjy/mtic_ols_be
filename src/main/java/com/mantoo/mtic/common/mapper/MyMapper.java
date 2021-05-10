package com.mantoo.mtic.common.mapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @ClassName: MyMapper
 * @Description: 通用mapper基础接口
 * @Author: renjt
 * @Date: 2019-11-19 08:46
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
    //TODO
    //FIXME 特别注意，该接口不能被扫描到，否则会出错
}