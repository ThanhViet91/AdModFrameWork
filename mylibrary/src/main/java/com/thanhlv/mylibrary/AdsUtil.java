package com.thanhlv.mylibrary;

import static com.google.android.gms.ads.nativead.NativeAdOptions.ADCHOICES_TOP_RIGHT;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAdOptions;

public class AdsUtil {
    public static final String AD_BANNER_ID_DEV = "ca-app-pub-3940256099942544/6300978111";
    public static final String AD_NATIVE_ID_DEV = "ca-app-pub-3940256099942544/2247696110";
    public static final String AD_INTERSTITIAL_ID_DEV = "ca-app-pub-3940256099942544/1033173712";


    // for BannerAd
    public static void createBanner(Context context, String id, ViewGroup adView) {
        if (context == null) return;
        AdView mAdView;
        mAdView = new AdView(context);
        mAdView.setAdSize(getAdSize(context));
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                AdjustUtil.trackingRevenueAdjust(mAdView);
            }
        });
        mAdView.setAdUnitId(id.isEmpty() ? AD_BANNER_ID_DEV : id);
        if (mAdView.getParent() != null) {
            ((ViewGroup) mAdView.getParent()).removeView(mAdView);
        }
        if (mAdView.getParent() == null) adView.addView(mAdView);
        //requestAd
        AdRequest adRequest = new AdRequest.Builder().build();
        RunUtil.runOnUI(() -> mAdView.loadAd(adRequest));
    }

    public static AdSize getAdSize(Context context) {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        if (context instanceof Activity) {
            Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
            DisplayMetrics outMetrics = new DisplayMetrics();
            display.getMetrics(outMetrics);
            float widthPixels = outMetrics.widthPixels;
            float density = outMetrics.density;
            int adWidth = (int) (widthPixels / density);
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth);
        } else {
            return AdSize.FLUID;
        }
    }

    // for Interstitial
    public static InterstitialAd mInterstitialAdAdmob = null;

    public static boolean interstitialAdAlready(Context context) {
        return context != null && mInterstitialAdAdmob != null;
    }

    public static void createInterstitialAd(Context context, String id) {
        if (context == null) {
            mInterstitialAdAdmob = null;
            return;
        }
        AdRequest adRequest = new AdRequest.Builder().build();
        RunUtil.runOnUI(() -> InterstitialAd.load(context, id.isEmpty() ? AD_INTERSTITIAL_ID_DEV : id, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAdAdmob = interstitialAd;
                        AdjustUtil.trackingRevenueAdjust(mInterstitialAdAdmob);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAdAdmob = null;
                    }
                }));
    }

    public static void showInterstitialAd(Context context, ViewGroup loadingView, FullScreenContentCallback fullScreenContentCallback) {
        if (mInterstitialAdAdmob != null
                && context instanceof Activity) {
            loadingView.setVisibility(View.VISIBLE);
            new Handler().postDelayed(() -> mInterstitialAdAdmob.show((Activity) context), 800);
            mInterstitialAdAdmob.setFullScreenContentCallback(fullScreenContentCallback);
        }
    }

    // for NativeAd

    public static void createNativeAd(Context context, String id, TemplateView templateView) {
        if (context == null || templateView == null) return;
        if (!isNetworkAvailable(context)) {
            templateView.preLoadView();
            templateView.setVisibility(View.GONE);
            templateView.destroyNativeAd();
        } else {
            templateView.setVisibility(View.VISIBLE);
            templateView.setStyles(new NativeTemplateStyle.Builder().build());
            if (templateView.getNativeAd() == null) {
                AdLoader adLoader = new AdLoader.Builder(context, id.isEmpty() ? AD_NATIVE_ID_DEV : id)
                        .forNativeAd(nativeAd_ -> {
                            if (context instanceof Activity && ((Activity) context).isDestroyed()) {
                                nativeAd_.destroy();
                                return;
                            }
                            templateView.setNativeAd(nativeAd_);
                            AdjustUtil.trackingRevenueAdjust(nativeAd_);
                        })
                        .withAdListener(new AdListener() {
                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                templateView.setVisibility(View.GONE);
                            }
                        })
                        .withNativeAdOptions(new NativeAdOptions.Builder()
                                .setAdChoicesPlacement(ADCHOICES_TOP_RIGHT)
                                .build())
                        .build();
                RunUtil.runOnUI(() -> adLoader.loadAd(new AdRequest.Builder().build()));
            }
        }

    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
