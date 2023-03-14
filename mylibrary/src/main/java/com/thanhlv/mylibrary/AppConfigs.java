package com.thanhlv.mylibrary;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class AppConfigs {
    private static AppConfigs _instance;
    private FirebaseRemoteConfig config;

    private AppConfigs() {
    }

    public FirebaseRemoteConfig getConfig() {
        return this.config;
    }

    public void setConfig(FirebaseRemoteConfig config) {
        this.config = config;
    }

    public static AppConfigs getInstance() {
        if (_instance == null) {
            _instance = new AppConfigs();
        }
        return _instance;
    }

}
