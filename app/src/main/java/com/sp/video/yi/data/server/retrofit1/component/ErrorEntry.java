package com.sp.video.yi.data.server.retrofit1.component;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorEntry extends Throwable{
    @JsonProperty("code")
    private Integer code;
    @JsonProperty("message")
    private String  message;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}