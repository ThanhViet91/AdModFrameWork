package com.thanhlv.mylibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import androidx.appcompat.app.AlertDialog;

import java.net.InetAddress;

public class NetworkUtils {

    //check connected to a network
    @SuppressLint("MissingPermission")
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static void checkInternet(Context context) {
        if (!isNetworkAvailable(context)) {
            try {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Network is disconnected");
                alertDialogBuilder
                        .setMessage("Check your network, please!")
                        .setCancelable(true)
                        .setPositiveButton("OK", (dialog, id) -> dialog.cancel());
                AlertDialog alertDialog = alertDialogBuilder.create();
                if (!((Activity) context).isFinishing()) alertDialog.show();
            } catch (Exception ignored) {
            }
        }
    }

    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.toString().equals("");
        } catch (Exception e) {
            return false;
        }
    }

    public static int getWifiLevel(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int linkSpeed = wifiManager.getConnectionInfo().getRssi();
        return WifiManager.calculateSignalLevel(linkSpeed, 10);
    }

    @SuppressWarnings("deprecation")
    public static String getWifiIPAddress(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    }

    public static int DEFAULT_PORT = 8080;

    public static String getLocalURLBase(Context context) {
        return "http://" + getWifiIPAddress(context) + ":" + DEFAULT_PORT + "/";
    }
}
