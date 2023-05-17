package com.thanhlv.adsframework;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.thanhlv.mylibrary.AdsManager;
import com.thanhlv.mylibrary.view.BannerAdView;

public class AdMod extends AppCompatActivity {

    BannerAdView banner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        banner = findViewById(R.id.banner);
        AdsManager.initialAdMod(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        AdsManager.registerReceiver(this, context -> AdsManager.createBanner(context, "", banner));
    }

    @Override
    protected void onStop() {
        super.onStop();
        AdsManager.unregisterReceiver(this);
    }
}