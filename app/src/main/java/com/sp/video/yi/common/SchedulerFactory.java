package com.sp.video.yi.common;

import com.nd.hy.android.hermes.frame.thread.BackgroundThreadExecutor;

import java.util.concurrent.ExecutorService;

import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * SchedulerFactory
 * <p>
 *
 * @author yangz
 * @version 2015/11/6
 */
public class SchedulerFactory {

    private static final int MAX_THREAD_NUM = 3;

    private static ExecutorService sExecutors =
            new BackgroundThreadExecutor(MAX_THREAD_NUM, "SchedulerFactory IO - ");

    private static Scheduler sIoScheduler = Schedulers.from(sExecutors);

    public static Scheduler getIoScheduler() {
        return sIoScheduler;
    }
}
