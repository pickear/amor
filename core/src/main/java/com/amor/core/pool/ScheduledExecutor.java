package com.amor.core.pool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created by dylan on 2017/10/1.
 */
public final class ScheduledExecutor {


    private static ScheduledExecutorService service;

    static{
        service = Executors.newScheduledThreadPool(3, new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                return thread;
            }
        });
    }


    /**
     * @param task
     * @param intervalMinutes
     */
    public static void submitTask(Runnable task,long intervalMinutes){
        service.scheduleWithFixedDelay(task, 0, intervalMinutes, TimeUnit.MINUTES);
    }
}
