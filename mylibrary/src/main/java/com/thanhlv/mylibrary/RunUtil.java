package com.thanhlv.mylibrary;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public final class RunUtil {

    static private Handler handler;

    static private final int NUM_OF_THREADS = 20;

    static private Executor executor;

    static {
        createExecutor();
    }

    static void createExecutor() {
        RunUtil.executor = Executors.newFixedThreadPool(NUM_OF_THREADS, r -> {
            Thread th = new Thread(r);
            th.setName("Background Thread");
            return th;
        });
    }

    public static void runOnUI(Runnable runnable) {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }

        handler.post(runnable);
    }

    public static void runInBackground(Runnable runnable, boolean forceNewThread) {
        if (forceNewThread || isMain()) {
            executor.execute(runnable);
        } else {
            runnable.run();
        }

    }

    public static void runInBackground(Runnable runnable) {
        runInBackground(runnable, false);
    }

    public static Executor getExecutor() {
        return executor;
    }

    public static boolean isMain() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static <T> void postSuccess(final AsyncListener<T> listener, final T object) {
        if (listener == null)
            return;

        RunUtil.runOnUI(() -> listener.onSuccess(object));
    }

    public static <T> void postError(final AsyncListener<T> listener, final String error) {
        if (listener == null)
            return;

        RunUtil.runOnUI(() -> listener.onError(error));
    }

}