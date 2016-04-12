package com.sp.video.yi.data.server.retrofit1.component;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;

/**
 * Created by Weichao Wang on 2016/4/12.
 */
public class BasicErrorHandler implements ErrorHandler {
    @Override
    public Throwable handleError(RetrofitError err) {
        switch (err.getKind()) {
            case NETWORK:
                return new Rx1Exception(RetrofitError.Kind.NETWORK,"请检查您的网络设置");
            case CONVERSION:
                return new Rx1Exception(RetrofitError.Kind.CONVERSION,"数据解析错误，请重试");
            default:
                ErrorEntry entry = null;
                try {
                    entry = (ErrorEntry) err.getBodyAs(ErrorEntry.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (entry != null) {
                    return entry;
                }
                return new Rx1Exception(err.getMessage());
        }
    }
}
