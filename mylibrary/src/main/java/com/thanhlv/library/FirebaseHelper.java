package com.thanhlv.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
public class FirebaseHelper {
    private static final String TAG = "FirebaseHelper";
    @SuppressLint("StaticFieldLeak")
    private static FirebaseHelper INSTANCE = null;
    private Context context;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    public static FirebaseHelper getInstance(Context context_) {
        if (INSTANCE == null) {
            INSTANCE = new FirebaseHelper(context_);
            System.out.println(TAG + " thanhlv >>> FirebaseHelper init!");
        }
        return INSTANCE;
    }
    public FirebaseHelper(Context context_) {
        if (mFirebaseAnalytics == null && context_ != null) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context_.getApplicationContext());
        }
        if (mFirebaseRemoteConfig == null) {
            mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
            FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(0)
                    .build();
            mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        }
    }
    public FirebaseRemoteConfig getFireBaseRemoteConfig() {
        return this.mFirebaseRemoteConfig;
    }
    public void logEventToFirebase(String event) {
        if (mFirebaseAnalytics != null) {
            Bundle bundle = new Bundle();
            bundle.putString("action", "click");
            mFirebaseAnalytics.logEvent(event, bundle);
        }
    }
}
