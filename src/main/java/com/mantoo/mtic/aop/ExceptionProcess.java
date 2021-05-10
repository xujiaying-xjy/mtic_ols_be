package com.mantoo.mtic.aop;

import com.mantoo.mtic.common.entity.Global;
import com.mantoo.mtic.common.entity.RestResult;
import com.mantoo.mtic.common.entity.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartException;

@ControllerAdvice
@Component
public class ExceptionProcess {
    @Autowired
    private Environment env;

    // 对这个异常的统一处理，返回值和Controller的返回规则一样
    @ExceptionHandler(MultipartException.class)
    @ResponseBody
    public RestResult<Object> handleAll(Throwable t) {
        if (t.getMessage().contains("FileSizeLimitExceededException")) {
            return ResultUtils.genErrorResult(Global.OUTOFMAXSIZE + env.getProperty("spring.servlet.multipart.max-file-size"), "5001");
        } else {
            return ResultUtils.genErrorResult(t.getMessage(), "5001");
        }
    }
}