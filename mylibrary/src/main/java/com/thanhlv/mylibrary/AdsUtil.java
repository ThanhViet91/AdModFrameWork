package com.thanhlv.mylibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;

import java.util.Random;

public class AdsUtil {

    // for DEV
    public static final String AD_OPEN_APP_ID_DEV = "ca-app-pub-3940256099942544/3419835294";
    public static final String AD_BANNER_ID_DEV = "ca-app-pub-3940256099942544/6300978111";
    public static final String AD_NATIVE_ID_DEV = "ca-app-pub-3940256099942544/2247696110";
    public static final String AD_INTERSTITIAL_ID_DEV = "ca-app-pub-3940256099942544/1033173712";

    private AdsConfigs mAdsConfig;

    public AdsConfigs getAdsConfig() {
        return this.mAdsConfig;
    }

    public void setAdsConfig(AdsConfigs mAdsConfig) {
        this.mAdsConfig = mAdsConfig;
    }

    public AdsUtil(Context context, AdsConfigs adsConfigs) {
        if (context == null || adsConfigs == null) return;
        this.mContext = context;
        this.mAdsConfig = adsConfigs;
        if (adsConfigs.getAdViewBanner() != null) initialAdViewBanner();
    }
    private Context mContext;

    public void setContext (Context context) {
        this.mContext = context;
    }

    private boolean isLoaded = false;

    @SuppressLint("StaticFieldLeak")
    private static AdsUtil instance;

    public AdView mAdView;

    public static AdsUtil getInstance(Context context, AdsConfigs adsConfigs) {
        if (instance == null) instance = new AdsUtil(context, adsConfigs);
        return instance;
    }

    // for BannerAd
    public void initialAdViewBanner() {
        if (SharedPref.isProApp(mContext) || this.mAdsConfig == null || this.mAdsConfig.getAdViewBanner() == null) return;
        mAdView = new AdView(mContext);
//        adView.setAdSize(mAdsConfig.getAdSize());
        mAdView.setAdSize(getAdSize());
        mAdView.setAdUnitId(mAdsConfig.isDebug() ? AD_BANNER_ID_DEV : mAdsConfig.getAD_BANNER_ID());
        this.mAdsConfig.getAdViewBanner().addView(mAdView);
        //requestAd
        AdRequest adRequest = new AdRequest.Builder().build();
        RunUtil.runOnUI(() -> mAdView.loadAd(adRequest));
    }

    private AdSize getAdSize() {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        if (mContext instanceof Activity)  {
            Display display = ((Activity) mContext).getWindowManager().getDefaultDisplay();
            DisplayMetrics outMetrics = new DisplayMetrics();
            display.getMetrics(outMetrics);

            float widthPixels = outMetrics.widthPixels;
            float density = outMetrics.density;

            int adWidth = (int) (widthPixels / density);

            // Step 3 - Get adaptive ad size and return for setting on the ad view.
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(mContext, adWidth);
        } else {
            return mAdsConfig.getAdSize();
        }
    }

    public void showBanner() {
        if (this.mAdsConfig == null || this.mAdsConfig.getAdViewBanner() == null) return;
        if (SharedPref.isProApp(mContext)) {
            this.mAdsConfig.getAdViewBanner().setVisibility(View.GONE);
        } else
            this.mAdsConfig.getAdViewBanner().setVisibility(View.VISIBLE);
    }

    // for Interstitial
    public InterstitialAd mInterstitialAdAdmob = null;

    public boolean interstitialAdAlready() {
        return !SharedPref.isProApp(mContext)
                && this.mInterstitialAdAdmob != null
                && checkInterstitialAlready();
    }

    public long lastTime = 0;

    private boolean checkInterstitialAlready() {
        boolean passPercent = new Random().nextInt(100) < mAdsConfig.getPercentShow();
        boolean passOvertime = (System.currentTimeMillis() - lastTime) > mAdsConfig.getFrequencyCapping() * 1000L;
        return passPercent && passOvertime;
    }

    private int tryAgainstTime = 0;

    public void createInterstitialAdmob() {
        mInterstitialAdAdmob = null;
        if (SharedPref.isProApp(mContext) || this.mAdsConfig == null) {
            mInterstitialAdAdmob = null;
            return;
        }
        String interstitialID = (mAdsConfig.isDebug() ? AD_INTERSTITIAL_ID_DEV : mAdsConfig.getAD_INTERSTITIAL_ID());
        AdRequest adRequest = new AdRequest.Builder().build();
        RunUtil.runOnUI(() -> InterstitialAd.load(mContext, interstitialID, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAdAdmob = interstitialAd;
                        tryAgainstTime = 0;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAdAdmob = null;
                        if (tryAgainstTime++ < 2) createInterstitialAdmob();
                    }
                }));
    }

    public void showInterstitialAd(FullScreenContentCallback fullScreenContentCallback) {
        if (mInterstitialAdAdmob != null) {
            mInterstitialAdAdmob.show((Activity) mContext);
            mInterstitialAdAdmob.setFullScreenContentCallback(fullScreenContentCallback);
        }
    }

    // for NativeAd
    public NativeAd nativeAd;
    private AdLoader adLoader;
    public void loadNativeAdmob() {
        if (SharedPref.isProApp(mContext) || this.mAdsConfig == null) {
            nativeAd = null;
            return;
        }
        if (adLoader == null || nativeAd == null)
            adLoader = new AdLoader.Builder(mContext, mAdsConfig.isDebug() ? AD_NATIVE_ID_DEV : mAdsConfig.getAD_NATIVE_ID())
                    .forNativeAd(nativeAd_ -> {
                        nativeAd = nativeAd_;
                        if (mCallBack != null) mCallBack.loadSuccess(nativeAd_);
                    })
                    .build();
        RunUtil.runOnUI(() -> adLoader.loadAds(new AdRequest.Builder().build(), 5));
    }
    public NativeAdListener mCallBack;

    public interface NativeAdListener {
        void loadSuccess(NativeAd nativeAd);
    }

    public void setNativeAdCallBack(NativeAdListener callBack) {
        this.mCallBack = callBack;
    }

}
