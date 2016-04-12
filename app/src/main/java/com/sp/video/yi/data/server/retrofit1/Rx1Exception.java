package com.sp.video.yi.data.server.retrofit1;

import retrofit.RetrofitError;

/**
 * Created by Weichao Wang on 2016/4/12.
 */
public class Rx1Exception extends Throwable {
    private static final String DEFAULT_MESSAGE = "操作失败，请重试";

    private  RetrofitError.Kind errorKind;

    private int code;

    private String message;


    public Rx1Exception() {
    }


    public Rx1Exception(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
        this.message = detailMessage;
    }

    public Rx1Exception(String detailMessage) {
        super(detailMessage);
        this.message = detailMessage;
    }

    public Rx1Exception(Throwable throwable) {
        super(throwable);
    }

    public Rx1Exception(int code, String message) {
        this.code = code;
        this.message = message == null ? DEFAULT_MESSAGE : message;
    }

    public Rx1Exception(RetrofitError.Kind kind, String message) {
        this.errorKind = kind;
        this.message = message == null ? DEFAULT_MESSAGE : message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public RetrofitError.Kind getErrorKind() {
        return errorKind;
    }

}
