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

import com.facebook.shimmer.ShimmerFrameLayout;
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
    public static void createBanner(Context context, String id, ViewGroup adView, ViewGroup loadingView) {
        if (context == null) return;
        loadingView.setVisibility(View.VISIBLE);
        ((ShimmerFrameLayout)loadingView).startShimmer();
        AdView mAdView = new AdView(context);
        mAdView.setAdSize(getAdSize(context));
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                ((ShimmerFrameLayout)loadingView).stopShimmer();
                loadingView.setVisibility(View.VISIBLE);
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
    public static InterstitialAd mInterAd2 = null;

    public static boolean interstitialAdAlready(Context context, int type) {
        if (type == 1) return context != null && mInterAd != null;
        else return context != null && mInterAd2 != null;
    }

    static boolean isLoadingInter = false;
    static boolean isLoadingInter2 = false;

    public interface LoadInterAdCallback {
        void onAdLoaded(InterstitialAd interstitialAd, int type);

        void onAdFailedToLoad(int type);
    }

    public static void createInterstitialAd(Context context, String id, int type, LoadInterAdCallback callback) {
        System.out.println("thanhlv loaddddd  createInterstitialAd >>>> " + type + " /// " + id);
        if (context == null) {
            if (type == 1) mInterAd = null;
            if (type == 2) mInterAd2 = null;
            return;
        }
        if (type == 1 && isLoadingInter) return;
        if (type == 1) isLoadingInter = true;
        if (type == 2 && isLoadingInter2) return;
        if (type == 2) isLoadingInter2 = true;
        System.out.println("thanhlv loaddddd  createInterstitialAd okkkk >>>> " + type + " /// " + id);
        AdRequest adRequest = new AdRequest.Builder().setHttpTimeoutMillis(30000).build();
        runOnUI(() -> InterstitialAd.load(context, id.isEmpty() ? AD_INTERSTITIAL_ID_DEV : id, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        if (type == 1) {
                            isLoadingInter = false;
                            mInterAd = interstitialAd;
                        }
                        if (type == 2) {
                            isLoadingInter2 = false;
                            mInterAd2 = interstitialAd;
                        }
                        if (callback != null) callback.onAdLoaded(interstitialAd, type);
                        AdjustUtil.trackingRevenueAdjust(interstitialAd);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        System.out.println("thanhlv onAdFailedToLoad(@NonNull LoadAdError loadAdError) " + type);
                        if (type == 1) {
                            isLoadingInter = false;
                            mInterAd = null;
                        }
                        if (type == 2) {
                            isLoadingInter2 = false;
                            mInterAd2 = null;
                        }
                        if (callback != null) callback.onAdFailedToLoad(type);
                    }
                }));
    }

    public static void showInterstitialAd(Context context, ViewGroup loadingView, int type, FullScreenContentCallback fullScreenContentCallback) {
        if (type == 1) {
            if (context instanceof Activity && mInterAd != null) {
                loadingView.setVisibility(View.VISIBLE);
                new Handler().postDelayed(() -> mInterAd.show((Activity) context), 600);
                mInterAd.setFullScreenContentCallback(fullScreenContentCallback);
            }
        }
        if (type == 2) {
            if (context instanceof Activity && mInterAd2 != null) {
                loadingView.setVisibility(View.VISIBLE);
                new Handler().postDelayed(() -> mInterAd2.show((Activity) context), 600);
                mInterAd2.setFullScreenContentCallback(fullScreenContentCallback);
            }
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
                            if (context instanceof Activity && ((Activity)context).isDestroyed()) {
                                nativeAd_.destroy();
                                return;
                            }
                            templateView.setNativeAd(nativeAd_);
                            AdjustUtil.trackingRevenueAdjust(nativeAd_);
                        })
                        .withAdListener(new AdListener() {
                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                // Handle the failure by logging, altering the UI, and so on.
                                templateView.setVisibility(View.GONE);
                            }
                        })
                        .withNativeAdOptions(new NativeAdOptions.Builder()
                                // Methods in the NativeAdOptions.Builder class can be
                                // used here to specify individual options settings.
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
