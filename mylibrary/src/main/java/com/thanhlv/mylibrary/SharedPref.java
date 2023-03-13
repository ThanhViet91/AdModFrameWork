package com.thanhlv.mylibrary;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    private static final String APP_SETTINGS = "APP_SETTINGS";
    private static final String SETTINGS_VERSION_APP = "SETTINGS_VERSION_APP";
    private static final String SETTINGS_IS_PRO = "SETTINGS_IS_PRO";
    private static final String SETTINGS_RATING_APP = "SETTINGS_RATING_APP";
    private static final String SETTINGS_IS_FIRST_OPEN_APP = "SETTINGS_IS_FIRST_OPEN_APP";
    private static final String SETTINGS_IS_OPEN_APP = "SETTINGS_IS_OPEN_APP";
    private static final String SETTINGS_SIGNED_DRIVE = "SETTINGS_SIGNED_DRIVE";
    private static final String SETTINGS_STRING_ = "SETTINGS_STRING_";
    private static final String SETTINGS_DEVICE_HISTORY = "SETTINGS_DEVICE_HISTORY";
    private static final String SETTINGS_DROPBOX_TOKEN = "SETTINGS_DROPBOX_TOKEN120";
    private static final String SETTINGS_BOOLEAN_ = "SETTINGS_BOOLEAN_";
    private static final String BROWSER_HISTORY = "BROWSER_HISTORY";
    private static final String LAST_TIME_ADS_NOTI = "LAST_TIME_ADS_NOTI";
    private SharedPref() {}
    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
    }

    public static long getLastTimeAdsNotification(Context context) {
        return getSharedPreferences(context).getLong(LAST_TIME_ADS_NOTI, 0);
    }

    public static void setLastTimeAdsNotification(Context context, long value) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putLong(LAST_TIME_ADS_NOTI, value);
        editor.apply();
    }

    public static int getVersionApp(Context context) {
        return getSharedPreferences(context).getInt(SETTINGS_VERSION_APP, 0);
    }

    public static void setVersionApp(Context context, int value) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(SETTINGS_VERSION_APP, value);
        editor.apply();
    }

    public static void setRatingApp(Context context, boolean value) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(SETTINGS_RATING_APP, value);
        editor.apply();
    }

    public static boolean hasRatingApp(Context context) {
        return getSharedPreferences(context).getBoolean(SETTINGS_RATING_APP, false);
    }

    public static void setProApp(Context context, boolean value) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(SETTINGS_IS_PRO, value);
        editor.apply();
    }

    public static boolean isProApp(Context context) {
        return getSharedPreferences(context).getBoolean(SETTINGS_IS_PRO, false);
    }

    public static void setFirstOpenApp(Context context, boolean value) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(SETTINGS_IS_FIRST_OPEN_APP, value);
        editor.apply();
    }

    public static boolean isFirstOpenApp(Context context) {
        return getSharedPreferences(context).getBoolean(SETTINGS_IS_FIRST_OPEN_APP, true);
    }

    public static void setOpenApp(Context context, boolean value) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(SETTINGS_IS_OPEN_APP, value);
        editor.apply();
    }

    public static boolean isOpenApp(Context context) {
        return getSharedPreferences(context).getBoolean(SETTINGS_IS_OPEN_APP, true);
    }

    public static void setSignedDrive(Context context, boolean value) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(SETTINGS_SIGNED_DRIVE, value);
        editor.apply();
    }

    public static boolean isSignedDrive(Context context) {
        return getSharedPreferences(context).getBoolean(SETTINGS_SIGNED_DRIVE, false);
    }


    public static String getDropboxAccessToken(Context context) {
        return getSharedPreferences(context).getString(SETTINGS_DROPBOX_TOKEN, null);
    }

    public static void setDropboxAccessToken(Context context, String value) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(SETTINGS_DROPBOX_TOKEN, value);
        editor.apply();
    }


    public static String getBrowserHistory(Context context) {
        return getSharedPreferences(context).getString(BROWSER_HISTORY, null);
    }

    public static void setBrowserHistory(Context context, String value) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(BROWSER_HISTORY, value);
        editor.apply();
    }
}
