package com.mantoo.mtic.common.entity;

/**
* 响应结果实体
* @Author: mjh
* @Date: 2018-03-01 11:25:59
*/
public class RestResult<T> {
	
	public static final String SUCCESS = "1";
	public static final String ERROR = "0";
	
	boolean success;
	
	String msg;
	
	String errorCode;
	
	T data;
	
    private RestResult() {}

    public static <T> RestResult<T> newInstance() {
        return new RestResult<>();
    }

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "RestResult [success=" + success + ", msg=" + msg + ", errorCode=" + errorCode + ", data=" + data + "]";
	}


}
