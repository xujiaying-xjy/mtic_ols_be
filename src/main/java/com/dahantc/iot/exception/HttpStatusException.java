package com.dahantc.iot.exception;

/**
 * http返回状态异常
 * @author zdq
 * @create 2018/4/3
 */
public class HttpStatusException extends RuntimeException{

    public HttpStatusException() {
    }

    public HttpStatusException(String message) {
        super(message);
    }

    public HttpStatusException(Throwable cause) {
        super(cause);
    }
}
