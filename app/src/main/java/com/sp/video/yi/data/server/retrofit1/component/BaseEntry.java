package com.sp.video.yi.data.server.retrofit1.component;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;


/**
 * BaseEntry
 *
 * @author yangz
 * @version 2014/11/25
 */
public class BaseEntry<T> implements Serializable {

    @JsonProperty("Code")
    private int code;

    @JsonProperty("Message")
    private String message;

    @JsonProperty("Detail")
    private String detail;

    @JsonProperty("Data")
    private T data;


    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isError() {
//        return code != 0 && !TextUtils.isEmpty(message);
        return code != 0;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void throwExceptionIfError() throws Rx1Exception {
        if (isError()) {
            throw new Rx1Exception(code, message);
        }
    }
}
