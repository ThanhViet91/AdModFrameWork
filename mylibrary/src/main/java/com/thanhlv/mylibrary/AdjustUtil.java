package com.thanhlv.mylibrary;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustAdRevenue;
import com.adjust.sdk.AdjustConfig;
import com.adjust.sdk.AdjustEvent;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdapterResponseInfo;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.nativead.NativeAd;

import java.util.Objects;

public class AdjustUtil {

    public static void trackingRevenueAdjust(AppOpenAd ad) {
        if (ad == null) return;
        ad.setOnPaidEventListener(adValue -> {
            AdapterResponseInfo loadedAdapterResponseInfo = ad.getResponseInfo().getLoadedAdapterResponseInfo();
            AdjustAdRevenue adRevenue = new AdjustAdRevenue(AdjustConfig.AD_REVENUE_ADMOB);
            adRevenue.setRevenue(adValue.getValueMicros() / 1000000.0, adValue.getCurrencyCode());
            if (loadedAdapterResponseInfo != null)
                adRevenue.setAdRevenueNetwork(loadedAdapterResponseInfo.getAdSourceName());
            Adjust.trackAdRevenue(adRevenue);
        });
    }

    public static void trackingRevenueAdjust(NativeAd ad) {
        if (ad == null) return;
        ad.setOnPaidEventListener(adValue -> {
            AdapterResponseInfo loadedAdapterResponseInfo = Objects.requireNonNull(ad.getResponseInfo()).getLoadedAdapterResponseInfo();
            AdjustAdRevenue adRevenue = new AdjustAdRevenue(AdjustConfig.AD_REVENUE_ADMOB);
            adRevenue.setRevenue(adValue.getValueMicros() / 1000000.0, adValue.getCurrencyCode());
            if (loadedAdapterResponseInfo != null)
                adRevenue.setAdRevenueNetwork(loadedAdapterResponseInfo.getAdSourceName());
            Adjust.trackAdRevenue(adRevenue);
        });
    }

    public static void trackingRevenueAdjust(InterstitialAd ad) {
        if (ad == null) return;
        ad.setOnPaidEventListener(adValue -> {
            AdapterResponseInfo loadedAdapterResponseInfo = Objects.requireNonNull(ad.getResponseInfo()).getLoadedAdapterResponseInfo();
            AdjustAdRevenue adRevenue = new AdjustAdRevenue(AdjustConfig.AD_REVENUE_ADMOB);
            adRevenue.setRevenue(adValue.getValueMicros() / 1000000.0, adValue.getCurrencyCode());
            if (loadedAdapterResponseInfo != null)
                adRevenue.setAdRevenueNetwork(loadedAdapterResponseInfo.getAdSourceName());
            Adjust.trackAdRevenue(adRevenue);
        });
    }

    public static void trackingRevenueAdjust(AdView ad) {
        if (ad == null) return;
        ad.setOnPaidEventListener(adValue -> {
            AdapterResponseInfo loadedAdapterResponseInfo = Objects.requireNonNull(ad.getResponseInfo()).getLoadedAdapterResponseInfo();
            AdjustAdRevenue adRevenue = new AdjustAdRevenue(AdjustConfig.AD_REVENUE_ADMOB);
            adRevenue.setRevenue(adValue.getValueMicros() / 1000000.0, adValue.getCurrencyCode());
            if (loadedAdapterResponseInfo != null)
                adRevenue.setAdRevenueNetwork(loadedAdapterResponseInfo.getAdSourceName());
            Adjust.trackAdRevenue(adRevenue);
        });
    }
}
