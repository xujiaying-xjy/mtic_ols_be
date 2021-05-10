package com.mantoo.mtic.exception;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.mantoo.mtic.common.entity.RestResult;
import com.mantoo.mtic.common.entity.ResultUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 系统异常统一处理
 *
 * @Author: mjh
 * @Date: 2018-03-19 15:58:52
 */
@RestControllerAdvice
public class RestExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    /**
     * 运行时异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public <T> RestResult<T> runtimeExceptionHandler(RuntimeException ex) {
        if (logger.isDebugEnabled()) {
            logger.error("[RestExceptionHandler] -----> ", ex);
        }
        return ResultUtils.genErrorResult(ex.getMessage(), ErrorInfo.SERVICE_ERROR.getCode());
    }

    /**
     * 空指针异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(NullPointerException.class)
    public <T> RestResult<T> nullPointerExceptionHandler(NullPointerException ex) {
        if (logger.isDebugEnabled()) {
            logger.error("[RestExceptionHandler] -----> ", ex);
        }
        return ResultUtils.genErrorResult(ErrorInfo.SERVICE_ERROR);
    }

    /**
     * 非法参数异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public <T> RestResult<T> illegalParamsExceptionHandler(MethodArgumentNotValidException ex) {
        if (logger.isDebugEnabled()) {
            logger.error("[RestExceptionHandler] -----> ", ex);
        }
        return ResultUtils.genErrorResult(ErrorInfo.ILLEGAL_PARAM);
    }

    /**
     * 类型转换异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(ClassCastException.class)
    public <T> RestResult<T> classCastExceptionHandler(ClassCastException ex) {
        if (logger.isDebugEnabled()) {
            logger.error("[RestExceptionHandler] -----> ", ex);
        }
        return ResultUtils.genErrorResult(ErrorInfo.SERVICE_ERROR);
    }

    /**
     * IO异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(IOException.class)
    public <T> RestResult<T> iOExceptionHandler(IOException ex) {
        if (logger.isDebugEnabled()) {
            logger.error("[RestExceptionHandler] -----> ", ex);
        }
        return ResultUtils.genErrorResult(ErrorInfo.SERVICE_ERROR);
    }

    /**
     * 未知方法异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(NoSuchMethodException.class)
    public <T> RestResult<T> noSuchMethodExceptionHandler(NoSuchMethodException ex) {
        if (logger.isDebugEnabled()) {
            logger.error("[RestExceptionHandler] -----> ", ex);
        }
        return ResultUtils.genErrorResult(ErrorInfo.SERVICE_ERROR);
    }

    /**
     * 数组越界异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public <T> RestResult<T> indexOutOfBoundsExceptionHandler(IndexOutOfBoundsException ex) {
        if (logger.isDebugEnabled()) {
            logger.error("[RestExceptionHandler] -----> ", ex);
        }
        return ResultUtils.genErrorResult(ErrorInfo.SERVICE_ERROR);
    }

    /**
     * 请求消息不可读-->缺少参数
     *
     * @param ex
     * @return
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public <T> RestResult<T> requestNotReadable(HttpMessageNotReadableException ex) {
        if (logger.isDebugEnabled()) {
            logger.error("[RestExceptionHandler] -----> ", ex);
        }
        Throwable a = ex.getCause();
        if (a == null) {
            return ResultUtils.genErrorResult(ErrorInfo.MISSING_PARAM_OR_MAPPING);
        }
        String path = a.getMessage();
        int strStartIndex = path.lastIndexOf("[\"");
        int strEndIndex = path.lastIndexOf("\"]");
        /* index 为负数 即表示该字符串中 没有该字符 */
        String result = "";
        if (strStartIndex > 0 && strEndIndex > 0) {
            result = path.substring(strStartIndex, strEndIndex).substring(2);
        }
        String msg = result + ErrorInfo.MISSING_PARAM_OR_MAPPING.getMsg();
        return ResultUtils.genErrorResult(msg, ErrorInfo.MISSING_PARAM_OR_MAPPING.getCode());
    }

    /**
     * 类型不匹配
     *
     * @param ex
     * @return
     */
    @ExceptionHandler({TypeMismatchException.class})
    public <T> RestResult<T> requestTypeMismatch(TypeMismatchException ex) {
        if (logger.isDebugEnabled()) {
            logger.error("[RestExceptionHandler] -----> ", ex);
        }
        return ResultUtils.genErrorResult(ErrorInfo.TYPE_MISMATCH);
    }

    /**
     * 缺少参数
     *
     * @param ex
     * @return
     */
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public <T> RestResult<T> requestMissingServletRequest(MissingServletRequestParameterException ex) {
        if (logger.isDebugEnabled()) {
            logger.error("[RestExceptionHandler] -----> ", ex);
        }
        return ResultUtils.genErrorResult(ErrorInfo.MISSING_PARAM);
    }

    /**
     * 405错误
     *
     * @return
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public <T> RestResult<T> request405() {
        if (logger.isDebugEnabled()) {
            logger.error("[RestExceptionHandler] -----> {}", 405);
        }
        return ResultUtils.genErrorResult(ErrorInfo.METHOD_NOT_SUPPORT);
    }

    /**
     * 406错误
     *
     * @return
     */
    @ExceptionHandler({HttpMediaTypeNotAcceptableException.class})
    public <T> RestResult<T> request406() {
        if (logger.isDebugEnabled()) {
            logger.error("[RestExceptionHandler] -----> {}", 406);
        }
        return ResultUtils.genErrorResult(ErrorInfo.SERVICE_ERROR);
    }

    /**
     * 500错误
     *
     * @param ex
     * @return
     */
    @ExceptionHandler({ConversionNotSupportedException.class, HttpMessageNotWritableException.class})
    public <T> RestResult<T> server500(RuntimeException ex) {
        if (logger.isDebugEnabled()) {
            logger.error("[RestExceptionHandler] -----> ", ex);
        }
        return ResultUtils.genErrorResult(ErrorInfo.SERVICE_ERROR);
    }

    /**
     * 捕捉所有Shiro异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ShiroException.class)
    public <T> RestResult<T> handle401(ShiroException e) {
        if (logger.isDebugEnabled()) {
            logger.error("[RestExceptionHandler] -----> ", e);
        }
        return ResultUtils.genErrorResult(ErrorInfo.AUTH_NOT_EXIST);
    }

    /**
     * 单独捕捉Shiro(UnauthorizedException)异常
     * 该异常为访问有权限管控的请求而该用户没有所需权限所抛出的异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(UnauthorizedException.class)
    public <T> RestResult<T> handle401(UnauthorizedException e) {
        if (logger.isDebugEnabled()) {
            logger.error("[RestExceptionHandler] -----> ", e);
        }
        return ResultUtils.genErrorResult(ErrorInfo.AUTH_NOT_EXIST.getMsg() + e.getMessage(), ErrorInfo.AUTH_NOT_EXIST.getCode());
    }

    /**
     * 单独捕捉Shiro(AuthenticationException)异常
     * 该异常为访问有权限管控的请求而该用户没有所需权限所抛出的异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(TokenExpiredException.class)
    public <T> RestResult<T> handle401(TokenExpiredException e) {
        if (logger.isDebugEnabled()) {
            logger.error("[RestExceptionHandler] -----> ", e);
        }
        return ResultUtils.genErrorResult(e.getMessage(), ErrorInfo.AUTH_NOT_EXIST.getCode());
    }

    /**
     * 单独捕捉Shiro(AuthenticationException)异常
     * 该异常为访问有权限管控的请求而该用户没有所需权限所抛出的异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(AuthenticationException.class)
    public <T> RestResult<T> handle401(AuthenticationException e) {
        if (logger.isDebugEnabled()) {
            logger.error("[RestExceptionHandler] -----> ", e);
        }
        return ResultUtils.genErrorResult(e.getMessage(), ErrorInfo.AUTH_NOT_EXIST.getCode());
    }

    /**
     * 单独捕捉Shiro(UnauthenticatedException)异常
     * 该异常为以游客身份访问有权限管控的请求无法对匿名主体进行授权，而授权失败所抛出的异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(UnauthenticatedException.class)
    public <T> RestResult<T> handle401(UnauthenticatedException e) {
        if (logger.isDebugEnabled()) {
            logger.error("[RestExceptionHandler] -----> ", e);
        }
        return ResultUtils.genErrorResult(ErrorInfo.UN_AUTH);
    }


    /**
     * 捕捉校验异常(BindException)
     * @return
     */
    /*@ResponseBody
    @ExceptionHandler(BindException.class)
    public <T> RestResult<T> validException(BindException e) {
        if (logger.isDebugEnabled()) {
            logger.error("[ShiroExceptionHandler] -----> ", e);
        }
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        Map<String, Object> result = this.getValidError(fieldErrors);
        return new ResponseBean(HttpStatus.BAD_REQUEST.value(), result.get("errorMsg").toString(), result.get("errorList"));
    }*/

    /**
     * 捕捉校验异常(MethodArgumentNotValidException)
     * @return
     */
  /*  @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public <T> RestResult<T> validException(MethodArgumentNotValidException e) {
        if (logger.isDebugEnabled()) {
            logger.error("[ShiroExceptionHandler] -----> ", e);
        }
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        Map<String, Object> result = this.getValidError(fieldErrors);
        return new ResponseBean(HttpStatus.BAD_REQUEST.value(), result.get("errorMsg").toString(), result.get("errorList"));
    }*/


    /**
     * 捕捉404异常
     *
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public <T> RestResult<T> handle(NoHandlerFoundException e) {
        if (logger.isDebugEnabled()) {
            logger.error("[RestExceptionHandler] -----> ", e);
        }
        return ResultUtils.genErrorResult("您请求的地址不存在","404");
    }


    /**
     * 获取状态码
     *
     * @param request
     * @return
     */
    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }

    /**
     * 获取校验错误信息
     *
     * @param fieldErrors
     * @return
     */
    private Map<String, Object> getValidError(List<FieldError> fieldErrors) {
        Map<String, Object> result = new HashMap<String, Object>(16);
        List<String> errorList = new ArrayList<String>();
        StringBuffer errorMsg = new StringBuffer("校验异常(ValidException):");
        for (FieldError error : fieldErrors) {
            errorList.add(error.getField() + "-" + error.getDefaultMessage());
            errorMsg.append(error.getField()).append("-").append(error.getDefaultMessage()).append(".");
        }
        result.put("errorList", errorList);
        result.put("errorMsg", errorMsg);
        return result;
    }

}  