//package com.thanhlv.mylibrary;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.net.ConnectivityManager;
//
//import com.google.android.gms.ads.MobileAds;
//
//public class AdsManager {
//
//    public static final String TAG = AdsManager.class.getSimpleName();
//
//
//    public static void initialAdMod(Context context) {
//        MobileAds.initialize(context, initializationStatus -> System.out.println("thanhlv - " + TAG + ": onInitializationComplete"));
//    }
//
//    public static void registerReceiver(Context context, ConnectivityCallBack callBack) {
//        if (context == null) return;
//        context.registerReceiver(mReceiver, mIntentFilter);
//        mConnectivityCallBack = callBack;
//    }
//
//    public static void unregisterReceiver(Context context) {
//        context.unregisterReceiver(mReceiver);
//    }
//
//    public interface ConnectivityCallBack {
//        void onReceive(Context context);
//    }
//
//    private static ConnectivityCallBack mConnectivityCallBack;
//    private static final IntentFilter mIntentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
//    private static final BroadcastReceiver mReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            if (mConnectivityCallBack != null) mConnectivityCallBack.onReceive(context);
//        }
//    };
//
//}
