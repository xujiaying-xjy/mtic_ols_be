package com.dahantc.iot.dto;

import java.io.Serializable;

/**
 * 描述
 *
 * @author zdq
 * @create 2018/3/22
 */
public class SmsDto implements Serializable {
    private String numbers;
    private String content;

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
