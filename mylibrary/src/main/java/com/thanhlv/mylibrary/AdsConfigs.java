package com.thanhlv.mylibrary;


import android.view.ViewGroup;

import com.google.android.gms.ads.AdSize;

public class AdsConfigs {
    private ViewGroup adViewBanner = null;
    private AdSize adSize = AdSize.BANNER;
    private String AD_OPEN_APP_ID1 = "";
    private String AD_OPEN_APP_ID2 = "";
    private String AD_OPEN_APP_ID3 = "";
    private String AD_BANNER_ID = "";
    private String AD_NATIVE_ID = "";
    private String AD_INTERSTITIAL_ID = "";
    private long frequencyCapping = 0;
    private int percentShow = 100;
    private boolean isDebug = true;

    private AdsConfigs() {
    }

    public void setAdViewBanner(ViewGroup adViewBanner) {
        this.adViewBanner = adViewBanner;
    }

    public void setAdSize(AdSize adSize) {
        this.adSize = adSize;
    }

    public ViewGroup getAdViewBanner() {
        return adViewBanner;
    }

    public AdSize getAdSize() {
        return adSize;
    }

    public String getAD_OPEN_APP_ID1() {
        return AD_OPEN_APP_ID1;
    }

    public String getAD_OPEN_APP_ID2() {
        return AD_OPEN_APP_ID2;
    }

    public String getAD_OPEN_APP_ID3() {
        return AD_OPEN_APP_ID3;
    }

    public String getAD_BANNER_ID() {
        return AD_BANNER_ID;
    }

    public String getAD_NATIVE_ID() {
        return AD_NATIVE_ID;
    }

    public String getAD_INTERSTITIAL_ID() {
        return AD_INTERSTITIAL_ID;
    }

    public long getFrequencyCapping() {
        return frequencyCapping;
    }

    public int getPercentShow() {
        return percentShow;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public static class Builder {
        private ViewGroup adViewBanner = null;
        private AdSize adSize = AdSize.BANNER;
        private String AD_OPEN_APP_ID1 = "";
        private String AD_OPEN_APP_ID2 = "";
        private String AD_OPEN_APP_ID3 = "";
        private String AD_BANNER_ID = "";
        private String AD_NATIVE_ID = "";
        private String AD_INTERSTITIAL_ID = "";
        private long frequencyCapping = 0;
        private int percentShow = 100;
        private boolean isDebug = true;

        public Builder() {
        }

        public Builder setAdViewBanner(ViewGroup adViewBanner) {
            this.adViewBanner = adViewBanner;
            return this;
        }

        public Builder setAdSize(AdSize adSize) {
            this.adSize = adSize;
            return this;
        }

        public Builder setAD_OPEN_APP_ID1(String AD_OPEN_APP_ID1) {
            this.AD_OPEN_APP_ID1 = AD_OPEN_APP_ID1;
            return this;
        }

        public Builder setAD_OPEN_APP_ID2(String AD_OPEN_APP_ID2) {
            this.AD_OPEN_APP_ID2 = AD_OPEN_APP_ID2;
            return this;
        }

        public Builder setAD_OPEN_APP_ID3(String AD_OPEN_APP_ID3) {
            this.AD_OPEN_APP_ID3 = AD_OPEN_APP_ID3;
            return this;
        }

        public Builder setAD_BANNER_ID(String AD_BANNER_ID) {
            this.AD_BANNER_ID = AD_BANNER_ID;
            return this;
        }

        public Builder setAD_NATIVE_ID(String AD_NATIVE_ID) {
            this.AD_NATIVE_ID = AD_NATIVE_ID;
            return this;
        }

        public Builder setAD_INTERSTITIAL_ID(String AD_INTERSTITIAL_ID) {
            this.AD_INTERSTITIAL_ID = AD_INTERSTITIAL_ID;
            return this;
        }

        public Builder setFrequencyCapping(long frequencyCapping) {
            this.frequencyCapping = frequencyCapping;
            return this;
        }

        public Builder setPercentShow(int percentShow) {
            this.percentShow = percentShow;
            return this;
        }

        public Builder setDebug(boolean debug) {
            this.isDebug = debug;
            return this;
        }

        public AdsConfigs build() {
            AdsConfigs adsConfigs = new AdsConfigs();
            adsConfigs.adViewBanner = this.adViewBanner;
            adsConfigs.adSize = this.adSize;
            adsConfigs.AD_OPEN_APP_ID1 = this.AD_OPEN_APP_ID1;
            adsConfigs.AD_OPEN_APP_ID2 = this.AD_OPEN_APP_ID2;
            adsConfigs.AD_OPEN_APP_ID3 = this.AD_OPEN_APP_ID3;
            adsConfigs.AD_BANNER_ID = this.AD_BANNER_ID;
            adsConfigs.AD_NATIVE_ID = this.AD_NATIVE_ID;
            adsConfigs.AD_INTERSTITIAL_ID = this.AD_INTERSTITIAL_ID;
            adsConfigs.frequencyCapping = this.frequencyCapping;
            adsConfigs.percentShow = this.percentShow;
            adsConfigs.isDebug = this.isDebug;
            return adsConfigs;
        }
    }
}