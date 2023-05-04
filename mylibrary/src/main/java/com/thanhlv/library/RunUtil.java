package com.thanhlv.library;

import android.os.Handler;
import android.os.Looper;

public final class RunUtil {
    static private Handler handler;
    public static void runOnUI(Runnable runnable) {
        if (handler == null) handler = new Handler(Looper.getMainLooper());
        handler.post(runnable);
    }
}