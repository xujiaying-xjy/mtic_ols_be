package com.mantoo.mtic.common.entity;

import com.github.pagehelper.PageInfo;
import com.mantoo.mtic.exception.ErrorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 生成响应结果工具类
 *
 * @Author: mjh
 * @Date: 2018-03-16 16:01:30
 */
public class ResultUtils {

    private static final Logger logger = LoggerFactory.getLogger(ResultUtils.class);

    public static final boolean SUCCESS = true;
    public static final boolean ERROR = false;

    public static final String SUCCESS_DEFUALT_MSG = "操作成功";

    public static final String SUCCESS_NODATA_MSG = "未查询到匹配数据";

    public static <T> RestResult<T> genResult(boolean success, String msg, String errorCode, T data) {

        RestResult<T> result = RestResult.newInstance();

        result.setSuccess(success);
        result.setMsg(msg);
        result.setErrorCode(errorCode);
        result.setData(data);

        if (logger.isDebugEnabled()) {
            logger.debug("generate rest result:{}", result);
        }

        return result;
    }

    public static <T> RestResult<T> genSuccesResult() {
        return genResult(SUCCESS, SUCCESS_DEFUALT_MSG, null, null);
    }

    public static <T> RestResult<T> genSuccesResult(T data) {
        return genResult(SUCCESS, SUCCESS_DEFUALT_MSG, null, data);
    }

    public static <T> RestResult<T> genSuccesNoDataResult(T data) {
        return genResult(SUCCESS, SUCCESS_NODATA_MSG, null, data);
    }

    public static <T> RestResult<T> genErrorResult(String msg) {
        return genResult(ERROR, msg, null, null);
    }

    public static <T> RestResult<T> genErrorResult(String msg, String code) {
        return genResult(ERROR, msg, code, null);
    }

    public static <T> RestResult<T> genErrorResult(ErrorInfo errorInfo) {
        return genResult(ERROR, errorInfo.getMsg(), errorInfo.getCode(), null);
    }

    public static <T> PageInfo<T> toPageInfo(List<T> list) {
        return new PageInfo<T>(list);
    }


}
