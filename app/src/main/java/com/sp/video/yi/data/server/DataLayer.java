package com.sp.video.yi.data.server;

/**
 * Created by Weichao Wang on 2016/4/12.
 */
public interface DataLayer<T> {
    public T genApi();
    public T getApi();
}
