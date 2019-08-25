package com.example.roman.listofnews;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.util.Log;

import com.example.roman.listofnews.data.background.NetworkUtils;

public class MyApplication extends Application {
    private static final String TAG = "myLogs";
    private static MyApplication sMyApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        sMyApplication = this;
        registerReceiver(NetworkUtils.sNetworkUtils.getReceiver(),
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        //Log.d(TAG, "Start connectivity receiver with callback");

        NetworkRequest request =
                new NetworkRequest.Builder()
                        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        .build();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
            Log.d(TAG, "Start connectivity receiver with handler");
            registerReceiver(NetworkUtils.sNetworkUtils.getReceiver(),
                    new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        } else {
            Log.d(TAG, "Start connectivity receiver with callback");
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            ConnectivityManager.NetworkCallback callback = createSimpleCallback();
            connectivityManager.registerDefaultNetworkCallback(callback);

        }

/*networkConnectionReceiver = new NetworkConnectionReceiver(this);
            connectivityManager.registerDefaultNetworkCallback(request,connectivityManager.getNetworkCapabilities(Network.fromNetworkHandle(long)) );*/

       /* ConnectivityManager.networkCallback = new CapabilityValidatedCallback();

        registerReceiver(NetworkUtils.sNetworkUtils.getReceiver(),
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
                new IntentFilter(ConnectivityManager.registerNetworkCallback(request, networkCallback));*/


    }

    private static ConnectivityManager.NetworkCallback createSimpleCallback() {
        return new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
            }

            @Override
            public void onLost(Network network) {
            }
        };
    }

    public static Context getContext() {
        return sMyApplication;
    }
}

