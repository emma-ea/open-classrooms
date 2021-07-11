package com.oddlycoder.ocr.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class NetworkConnection implements INetworkConnection {

    private static NetworkConnection networkConnection = null;

    private final Context context;

    private NetworkConnection(Context context) { this.context = context; }

    public synchronized static NetworkConnection getInstance(Context context) {
        if (networkConnection == null)
            networkConnection = new NetworkConnection(context);
        return networkConnection;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean isNetworkConnected() {
        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities hasNetworkConnection =
                manager.getNetworkCapabilities(manager.getActiveNetwork());
        return hasNetworkConnection != null &&
                hasNetworkConnection.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }
}
