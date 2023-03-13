package com.thanhlv.mylibrary;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.nativead.NativeAd;

public class NativeAdsUtil {

    //DEV
    public static final String AD_NATIVE_ID_DEV = "ca-app-pub-3940256099942544/2247696110";

    //RELEASE
    public static final String AD_NATIVE_ID = "ca-app-pub-2210513256218688/6896644866";

    @SuppressLint("StaticFieldLeak")
    private static NativeAdsUtil instance;
    public static NativeAdsUtil getInstance(Context context) {
        if (instance == null) instance = new NativeAdsUtil(context);
        return instance;
    }

    public NativeAdsUtil(Context context) {
        this.mContext = context;
    }

    private final Context mContext;

    private NativeAd nativeAd;
    private AdLoader adLoader;
    @SuppressLint("VisibleForTests")
    public void loadNativeAdmob(boolean isDebug) {
        if (adLoader == null || nativeAd == null)
            adLoader = new AdLoader.Builder(mContext, isDebug ? AD_NATIVE_ID_DEV : AD_NATIVE_ID)
                    .forNativeAd(nativeAd_ -> {
                        nativeAd = nativeAd_;
                        if (mCallBack != null) mCallBack.loadSuccess(nativeAd_);
                    })
                    .build();
        adLoader.loadAds(new AdRequest.Builder().build(), 5);
    }

    public NativeAd getNativeAd() {
        return this.nativeAd;
    }

    private NativeAdListener mCallBack;
    public interface NativeAdListener {
        void loadSuccess(NativeAd nativeAd);
    }

    public void setCallBack(NativeAdListener callBack) {
        this.mCallBack = callBack;
    }
}
