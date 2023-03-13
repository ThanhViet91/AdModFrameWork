package com.thanhlv.mylibrary;


import android.view.ViewGroup;

import com.google.android.gms.ads.AdSize;

public class AdsConfigs {
    private ViewGroup adViewBanner = null;
    private AdSize adSize = AdSize.BANNER;
    private String AD_SDK_ID = "";
    private String AD_OPEN_APP_ID1 = "";
    private String AD_OPEN_APP_ID2 = "";
    private String AD_OPEN_APP_ID3 = "";
    private String AD_BANNER_ID = "";
    private String AD_NATIVE_ID = "";
    private String AD_INTERSTITIAL_ID = "";
    private long frequencyCapping = 0;
    private int percentShow = 100;
    private boolean isDebug = true;

    public ViewGroup getAdViewBanner() {
        return adViewBanner;
    }

    public void setAdViewBanner(ViewGroup adViewBanner) {
        this.adViewBanner = adViewBanner;
    }

    public String getAD_SDK_ID() {
        return AD_SDK_ID;
    }

    public void setAD_SDK_ID(String AD_SDK_ID) {
        this.AD_SDK_ID = AD_SDK_ID;
    }

    public String getAD_OPEN_APP_ID1() {
        return AD_OPEN_APP_ID1;
    }

    public void setAD_OPEN_APP_ID1(String AD_OPEN_APP_ID1) {
        this.AD_OPEN_APP_ID1 = AD_OPEN_APP_ID1;
    }

    public String getAD_OPEN_APP_ID2() {
        return AD_OPEN_APP_ID2;
    }

    public void setAD_OPEN_APP_ID2(String AD_OPEN_APP_ID2) {
        this.AD_OPEN_APP_ID2 = AD_OPEN_APP_ID2;
    }

    public String getAD_OPEN_APP_ID3() {
        return AD_OPEN_APP_ID3;
    }

    public void setAD_OPEN_APP_ID3(String AD_OPEN_APP_ID3) {
        this.AD_OPEN_APP_ID3 = AD_OPEN_APP_ID3;
    }

    public String getAD_BANNER_ID() {
        return AD_BANNER_ID;
    }

    public void setAD_BANNER_ID(String AD_BANNER_ID) {
        this.AD_BANNER_ID = AD_BANNER_ID;
    }

    public String getAD_NATIVE_ID() {
        return AD_NATIVE_ID;
    }

    public void setAD_NATIVE_ID(String AD_NATIVE_ID) {
        this.AD_NATIVE_ID = AD_NATIVE_ID;
    }

    public String getAD_INTERSTITIAL_ID() {
        return AD_INTERSTITIAL_ID;
    }

    public void setAD_INTERSTITIAL_ID(String AD_INTERSTITIAL_ID) {
        this.AD_INTERSTITIAL_ID = AD_INTERSTITIAL_ID;
    }

    public long getFrequencyCapping() {
        return frequencyCapping;
    }

    public void setFrequencyCapping(long frequencyCapping) {
        this.frequencyCapping = frequencyCapping;
    }

    public int getPercentShow() {
        return percentShow;
    }

    public void setPercentShow(int percentShow) {
        this.percentShow = percentShow;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean debug) {
        isDebug = debug;
    }

    public AdSize getAdSize() {
        return adSize;
    }

    public void setAdSize(AdSize adSize) {
        this.adSize = adSize;
    }
}