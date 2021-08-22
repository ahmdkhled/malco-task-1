package com.malcoo.malcotask1.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

public class Network {


    public static boolean isConnected(Context context) {
        boolean wifiAvailable = false;
        boolean mobileAvailable = false;
        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (conManager != null) {
                NetworkCapabilities capabilities = conManager.getNetworkCapabilities(conManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)||capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))return  true;

                }
            }
        }
        else {

            NetworkInfo[] networkInfo = conManager.getAllNetworkInfo();
            for (NetworkInfo netInfo : networkInfo) {
                if (netInfo.getTypeName().equalsIgnoreCase("WIFI"))
                    if (netInfo.isConnected())
                        wifiAvailable = true;
                if (netInfo.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (netInfo.isConnected())
                        mobileAvailable = true;
            }
            return wifiAvailable || mobileAvailable;
        }


        return false;
}
}
