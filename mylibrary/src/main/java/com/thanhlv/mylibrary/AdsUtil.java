package com.thanhlv.mylibrary;

import static com.google.android.gms.ads.nativead.NativeAdOptions.ADCHOICES_TOP_RIGHT;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.thanhlv.mylibrary.view.BannerAdView;
import com.thanhlv.mylibrary.view.DialogLoadingFullScreen;
import com.thanhlv.mylibrary.view.NativeAdView;
//import com.thanhlv.mylibrary.view.NativeTemplateStyle;

public class AdsUtil {
    public static final String TAG = AdsUtil.class.getSimpleName();
    public static final String AD_BANNER_ID_DEV = "ca-app-pub-3940256099942544/6300978111";
    public static final String AD_NATIVE_ID_DEV = "ca-app-pub-3940256099942544/2247696110";
    public static final String AD_INTERSTITIAL_ID_DEV = "ca-app-pub-3940256099942544/1033173712";

    public static void initialAdMod(Context context) {
        MobileAds.initialize(context, initializationStatus -> System.out.println("thanhlv - " + TAG + ": onInitializationComplete"));
    }

    // for BannerAd
    public static void createBanner(Context context, String id, BannerAdView containerAdView) {
        if (context == null || containerAdView == null) return;
        MobileAds.initialize(context, initializationStatus -> System.out.println("thanhlv - " + TAG + ": createBanner " + id));
        containerAdView.toggleLoadingView(true);
        AdView mAdView = new AdView(context);
        mAdView.setAdUnitId(id.isEmpty() ? AD_BANNER_ID_DEV : id);
        mAdView.setAdSize(getAdSize(context));
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                new Handler().postDelayed(() -> containerAdView.toggleLoadingView(false), 150);
                AdjustUtil.trackingRevenueAdjust(mAdView);
            }
        });
        if (mAdView.getParent() != null) {
            ((ViewGroup) mAdView.getParent()).removeView(mAdView);
        }
        if (mAdView.getParent() == null) containerAdView.addView(mAdView);
        //requestAd
        AdRequest adRequest = new AdRequest.Builder().build();
        runOnUI(() -> mAdView.loadAd(adRequest));
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
    public static InterstitialAd mInterAd = null;

    public static boolean interstitialAdAlready(Context context) {
        return context != null && mInterAd != null;
    }

    static boolean isLoadingInter = false;

    public interface LoadInterAdCallback {
        void onAdLoaded();

        void onAdFailedToLoad();
    }

    public static void createInterstitialAd(Context context, String id, LoadInterAdCallback loadInterAdCallback) {
        if (context == null) {
            mInterAd = null;
            return;
        }
        if (isLoadingInter) return;
        isLoadingInter = true;
        System.out.println("thanhlv - " + TAG + ": createInterstitialAd (" + id);
        AdRequest adRequest = new AdRequest.Builder().setHttpTimeoutMillis(30000).build();
        runOnUI(() -> InterstitialAd.load(context,
                id.isEmpty() ? AD_INTERSTITIAL_ID_DEV : id,
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        isLoadingInter = false;
                        mInterAd = interstitialAd;
                        if (loadInterAdCallback != null) loadInterAdCallback.onAdLoaded();
                        AdjustUtil.trackingRevenueAdjust(interstitialAd);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        isLoadingInter = false;
                        mInterAd = null;
                        if (loadInterAdCallback != null) loadInterAdCallback.onAdFailedToLoad();
                    }
                }));
    }

    public interface ShowInterAdCallback {
        void onNextActivity(boolean clickDismiss);
    }

    private static ShowInterAdCallback mShowInterAdCallback;

    private static DialogLoadingFullScreen loadingFullScreen;

    public static void showInterstitialAd(Context context, ShowInterAdCallback showInterAdCallback) {
        mShowInterAdCallback = showInterAdCallback;
        if (context instanceof Activity && mInterAd != null) {
            loadingFullScreen = new DialogLoadingFullScreen();
            loadingFullScreen.show(((FragmentActivity) context).getSupportFragmentManager(), "");
            new Handler().postDelayed(() -> mInterAd.show((Activity) context), 800);
            mInterAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    if (mShowInterAdCallback != null)
                        mShowInterAdCallback.onNextActivity(true);
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    if (mShowInterAdCallback != null)
                        mShowInterAdCallback.onNextActivity(true);
                }

                @Override
                public void onAdImpression() {
                    super.onAdImpression();
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                    new Handler().postDelayed(() -> loadingFullScreen.dismissAllowingStateLoss(), 150);
                }
            });
        }
    }

    // for NativeAd

    public static void createNativeAd(Context context, String id, NativeAdView nativeAdView) {
        if (context == null || nativeAdView == null) return;
        if (!isNetworkAvailable(context)) {
            nativeAdView.preLoadView();
            nativeAdView.setVisibility(View.GONE);
            nativeAdView.destroyNativeAd();
        } else {
            nativeAdView.setVisibility(View.VISIBLE);
            if (nativeAdView.getNativeAd() == null) {
                System.out.println("thanhlv - " + TAG + ": createNativeAd " + id);
                AdLoader adLoader = new AdLoader.Builder(context, id.isEmpty() ? AD_NATIVE_ID_DEV : id)
                        .forNativeAd(nativeAd_ -> {
                            if (context instanceof Activity && ((Activity) context).isDestroyed()) {
                                nativeAd_.destroy();
                                return;
                            }
                            nativeAdView.setNativeAd(nativeAd_);
                            AdjustUtil.trackingRevenueAdjust(nativeAd_);
                        })
                        .withAdListener(new AdListener() {
                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                nativeAdView.setVisibility(View.GONE);
                            }
                        })
                        .withNativeAdOptions(new NativeAdOptions.Builder()
                                .setAdChoicesPlacement(ADCHOICES_TOP_RIGHT)
                                .build())
                        .build();
                runOnUI(() -> adLoader.loadAd(new AdRequest.Builder().build()));
            }
        }

    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    static private Handler handler;
    public static void runOnUI(Runnable runnable) {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        handler.post(runnable);
    }
}
