package com.thanhlv.mylibrary;

import static com.thanhlv.mylibrary.AdsUtil.AD_OPEN_APP_ID_DEV;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback;

import java.util.Date;

/**
 * Application class that initializes, loads and show ads when activities change states.
 */
public class BaseApplication extends Application
        implements ActivityLifecycleCallbacks, LifecycleObserver {

    public AppOpenAdManager appOpenAdManager;
    private Activity currentActivity;
    private final String TAG = "BaseApplication";
    public String CHANNEL_ID = "com.thanhlv.baseapplication";
    private static Application context;

    public static Application getAppContext() {
        return context;
    }

    public AdsConfigs adsConfigs;

    @Override
    public void onCreate() {
        super.onCreate();
        this.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        context = this;
        createChannelNotification();
    }

    public void initMobileAds() {
        MobileAds.initialize(context, initializationStatus -> {
        });
    }

    private void createChannelNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "AppName", NotificationManager.IMPORTANCE_NONE);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    public static boolean ignoreOpenAd = false;

    /**
     * LifecycleObserver method that shows the app open ad when the app moves to foreground.
     */
    @OnLifecycleEvent(Event.ON_START)
    protected void onMoveToForeground() {
        // Show the ad (if available) when the app moves to foreground.

        if (SharedPref.isProApp(context)) {
            return;
        }
        if (ignoreOpenAd) {
            ignoreOpenAd = false;
            return;
        }
        appOpenAdManager.showAdIfAvailable(currentActivity);
    }


    /**
     * ActivityLifecycleCallback methods.
     */
    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        // An ad activity is started when an ad is showing, which could be AdActivity class from Google
        // SDK or another activity class implemented by a third party mediation partner. Updating the
        // currentActivity only when an ad is not showing will ensure it is not an ad activity, but the
        // one that shows the ad.
        if (!appOpenAdManager.isShowingAd) {
            currentActivity = activity;
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
    }

    /**
     * Shows an app open ad.
     *
     * @param activity                 the activity that shows the app open ad
     * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
     */
    public void showAdIfAvailable(
            @NonNull Activity activity,
            @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
        // We wrap the showAdIfAvailable to enforce that other classes only interact with MyApplication
        // class.
        if (SharedPref.isProApp(context)) {
            return;
        }
        if (ignoreOpenAd) {
            ignoreOpenAd = false;
            return;
        }
        appOpenAdManager.showAdIfAvailable(activity, onShowAdCompleteListener);
    }

    /**
     * Interface definition for a callback to be invoked when an app open ad is complete
     * (i.e. dismissed or fails to show).
     */
    public interface OnShowAdCompleteListener {
        void onShowAdComplete();
    }

    /**
     * Inner class that loads and shows app open ads.
     */
    public static class AppOpenAdManager {

        private AppOpenAd appOpenAd = null;
        private boolean isLoadingAd = false;
        private boolean isShowingAd = false;
        private final AdsConfigs mAdsConfigs;
        /**
         * Keep track of the time an app open ad is loaded to ensure you don't show an expired ad.
         */
        private long loadTime = 0;
        private final String TAG = "AppOpenAdManager";

        /**
         * Constructor.
         */
        public AppOpenAdManager(AdsConfigs adsConfigs) {
            this.mAdsConfigs = adsConfigs;
        }

        /**
         * Load an ad.
         */
        private int typeOpenAd = 0;

        private void loadAd(Context context) {
            // Do not load ad if there is an unused ad or one is already loading.
            if (isLoadingAd || isAdAvailable() || mAdsConfigs == null) {
                return;
            }
            typeOpenAd++;
            String openAdID = mAdsConfigs.getAD_OPEN_APP_ID1();
            switch (typeOpenAd) {
                case 1:
                    openAdID = mAdsConfigs.getAD_OPEN_APP_ID1();
                    break;
                case 2:
                    openAdID = mAdsConfigs.getAD_OPEN_APP_ID2();
                    break;
                case 3:
                    openAdID = mAdsConfigs.getAD_OPEN_APP_ID3();
                    break;
            }
            isLoadingAd = true;
            AdRequest request = new AdRequest.Builder().build();
            AppOpenAd.load(context, BuildConfig.DEBUG ? AD_OPEN_APP_ID_DEV : openAdID, request,
                    AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                    new AppOpenAdLoadCallback() {
                        /**
                         * Called when an app open ad has loaded.
                         *
                         * @param ad the loaded app open ad.
                         */
                        @Override
                        public void onAdLoaded(@NonNull AppOpenAd ad) {
                            appOpenAd = ad;
                            isLoadingAd = false;
                            loadTime = (new Date()).getTime();
                            typeOpenAd = 0;
                        }

                        /**
                         * Called when an app open ad has failed to load.
                         *
                         * @param loadAdError the error.
                         */
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            appOpenAd = null;
                            isLoadingAd = false;
                            if (typeOpenAd >= 3) {
                                typeOpenAd = 0;
                            } else loadAd(context);
                        }
                    });
        }

        /**
         * Check if ad was loaded more than n hours ago.
         */
        private boolean wasLoadTimeLessThanNHoursAgo() {
            long dateDifference = (new Date()).getTime() - loadTime;
            long numMilliSecondsPerHour = 3600000;
            return (dateDifference < (numMilliSecondsPerHour * 4));  //default 4h
        }

        /**
         * Check if ad exists and can be shown.
         */
        private boolean isAdAvailable() {
            // Ad references in the app open beta will time out after four hours, but this time limit
            // may change in future beta versions. For details, see:
            // https://support.google.com/admob/answer/9341964?hl=en
            return appOpenAd != null && wasLoadTimeLessThanNHoursAgo();
        }

        /**
         * Show the ad if one isn't already showing.
         *
         * @param activity the activity that shows the app open ad
         */
        private void showAdIfAvailable(@NonNull final Activity activity) {
            showAdIfAvailable(
                    activity,
                    () -> {
                        // Empty because the user will go back to the activity that shows the ad.
                    });
        }

        /**
         * Show the ad if one isn't already showing.
         *
         * @param activity                 the activity that shows the app open ad
         * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
         */
        private void showAdIfAvailable(
                @NonNull final Activity activity,
                @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
            // If the app open ad is already showing, do not show the ad again.
            if (isShowingAd) {
                System.out.println(TAG + " thanhlv >>> The app open ad is already showing");
                return;
            }

            // If the app open ad is not available yet, invoke the callback then load the ad.
            if (!isAdAvailable()) {

                System.out.println(TAG + " thanhlv >>> The app open ad is not available yet");
                onShowAdCompleteListener.onShowAdComplete();
                loadAd(activity);
                return;
            }

            appOpenAd.setFullScreenContentCallback(
                    new FullScreenContentCallback() {
                        /** Called when full screen content is dismissed. */
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            System.out.println(TAG + " thanhlv >>> onAdDismissedFullScreenContent.");
                            // Set the reference to null so isAdAvailable() returns false.
                            appOpenAd = null;
                            isShowingAd = false;
                            onShowAdCompleteListener.onShowAdComplete();
                            loadAd(activity);
                        }

                        /** Called when fullscreen content failed to show. */
                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            appOpenAd = null;
                            isShowingAd = false;
                            System.out.println(TAG + " thanhlv >>> onAdFailedToShowFullScreenContent: " + adError);
                            onShowAdCompleteListener.onShowAdComplete();
                            loadAd(activity);
                        }

                        /** Called when fullscreen content is shown. */
                        @Override
                        public void onAdShowedFullScreenContent() {
                            System.out.println(TAG + " thanhlv >>> onAdShowedFullScreenContent.");
                            appOpenAd = null;
                        }
                    });
            isShowingAd = true;
            new Handler().postDelayed(() -> appOpenAd.show(activity), 200);
        }
    }
}
