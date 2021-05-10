package com.mantoo.mtic.exception;

public class MticException extends RuntimeException {

    private static final long serialVersionUID = 5122825901877875859L;

    private String code;

    public MticException(String cause, String code) {
        super(cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

