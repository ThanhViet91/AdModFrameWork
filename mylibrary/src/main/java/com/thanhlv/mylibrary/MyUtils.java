package com.thanhlv.mylibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;

public class MyUtils {

    public static String getAppStorageDirectory(Context context, String dirName) {
        File directory = new File(context.getFilesDir(), dirName);
        if (!directory.exists()) {
            boolean a = directory.mkdirs();
        }
        return directory.getAbsolutePath();
    }

    public static long getDurationMs(Context context, String path) {
        if (path.equals("")) return 0;
        if (!new File(path).exists()) {
            return 0;
        }
        long timeInMs;
        String time;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(context, Uri.parse(path));
            time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            retriever.release();
        } catch (Exception ignored) {
            return 0;
        }
        if (time == null) return 0;
        timeInMs = Long.parseLong(time);
        return timeInMs;
    }

    public static void hideStatusBar(Activity activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            setFullscreen(activity);
        }
    }

    public static void changeStatusBarColor(Activity activity, int color) {
        if (activity.isFinishing()) return;
        Window window = activity.getWindow();
        if (window == null) return;
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }

    public static void setFullscreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            activity.getWindow().getAttributes().layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.getWindow().setDecorFitsSystemWindows(false);
            activity.getWindow().getInsetsController().hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
            activity.getWindow().getInsetsController().setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        } else {
            activity.getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
    }

    public static void hideSoftInput(@NonNull final Activity activity) {
        hideSoftInput(activity.getWindow());
    }

    public static void hideSoftInput(@NonNull final Window window) {
        View view = window.getCurrentFocus();
        if (view == null) {
            View decorView = window.getDecorView();
            View focusView = decorView.findViewWithTag("keyboardTagView");
            if (focusView == null) {
                view = new EditText(window.getContext());
                view.setTag("keyboardTagView");
                ((ViewGroup) decorView).addView(view, 0, 0);
            } else {
                view = focusView;
            }
            view.requestFocus();
        }
        hideSoftInput(window.getContext(), view);
    }

    public static void showSoftInput(Context context) {
        if (context == null) return;
        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static void hideSoftInput(Context context, @NonNull final View view) {
        if (context == null) return;
        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @SuppressLint("DefaultLocale")
    public static String parseLongToTime(long durationInMillis) {
        long second = (durationInMillis / 1000) % 60;
        long minute = (durationInMillis / (1000 * 60)) % 60;
        long hour = (durationInMillis / (1000 * 60 * 60)) % 24;
        if (hour == 0) return String.format("%02d:%02d", minute, second);
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    public static void showSnackBarNotification(View view, String msg, int length) {
        Snackbar.make(view, msg, length).show();
    }


    @NonNull
    public static String getExtensionFile(String path) {
        if (TextUtils.isEmpty(path)) return "";
        return path.substring(path.lastIndexOf("."));
    }

    @NonNull
    public static String getFileNameWithoutExtension(String path) {
        if (TextUtils.isEmpty(path)) return "";
        String ext = path.substring(path.lastIndexOf("."));

        File file = new File(path);
        return file.getName().replace(ext, "");
    }

    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
