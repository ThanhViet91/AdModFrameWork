package com.thanhlv.mylibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class FirebaseAnalyticsHelper {

    private static final String TAG = "FirebaseAnalyticsHelper";
    @SuppressLint("StaticFieldLeak")
    private static FirebaseAnalyticsHelper INSTANCE = null;
    private Context context;
    private FirebaseAnalytics firebaseAnalytics;

    public static FirebaseAnalyticsHelper getInstance(Context context_) {
        if (INSTANCE == null) {
            INSTANCE = new FirebaseAnalyticsHelper(context_);
            System.out.println(TAG + " thanhlv >>> FirebaseAnalyticsHelper init!");
        }
        return INSTANCE;
    }

    public FirebaseAnalyticsHelper(Context context_) {
        if (firebaseAnalytics == null && context_ != null) {
            firebaseAnalytics = FirebaseAnalytics.getInstance(context_.getApplicationContext());
        }
    }

    public void generateFirebaseRemoteConfig() {
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
    }

    public void logEventToFirebase(String event) {
        if (firebaseAnalytics != null) {
            Bundle bundle = new Bundle();
            bundle.putString("action", "click");
            firebaseAnalytics.logEvent(event, bundle);
            System.out.println(TAG + " thanhlv >>> logEvent onclick " + event);
        }
    }
}
