package com.thanhlv.adsframework;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.thanhlv.mylibrary.AdsUtil;
import com.thanhlv.mylibrary.view.BannerAdView;
import com.thanhlv.mylibrary.view.NativeAdView;

public class AdMod extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NativeAdView banner = findViewById(R.id.banner);
        AdsUtil.initialAdMod(this);
        AdsUtil.createNativeAd(this, "", banner);
        AdsUtil.createInterstitialAd(this, "", new AdsUtil.LoadInterAdCallback() {
            @Override
            public void onAdLoaded() {
                AdsUtil.showInterstitialAd(AdMod.this,
                        clickDismiss -> startActivity(new Intent(AdMod.this, AdMod2.class)));
            }

            @Override
            public void onAdFailedToLoad() {

            }
        });
    }
}