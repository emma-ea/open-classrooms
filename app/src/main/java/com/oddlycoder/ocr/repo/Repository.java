package com.oddlycoder.ocr.repo;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.oddlycoder.ocr.model.WorldClock;
import com.oddlycoder.ocr.utils.NetworkConnection;
import com.oddlycoder.ocr.utils.WorldTimeApiClient;

import retrofit2.Response;

public class Repository implements IRepository {

    public static final String TAG = "com.oddlycoder.ocr";

    private static Repository instance = null;
    private final Context context;

    private final MutableLiveData<WorldClock> clock = new MutableLiveData<>();
    private final MutableLiveData<Boolean> conState = new MutableLiveData<>();

    private Repository(Context ctx) {
        this.context = ctx;
        WorldTimeApiClient client = new WorldTimeApiClient(ctx);
        client.start();
    }

    public static Repository initialize(Context ctx) {
        if (instance == null)
            instance = new Repository(ctx);
        return instance;
    }

    public void fetchTime(WorldClock clock) {
        Log.i(TAG, "fetchTime: ");
        this.clock.setValue(clock);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public LiveData<Boolean> getNetworkState() {
        // TODO: return network state;
        conState.setValue(NetworkConnection.getInstance(context).isNetworkConnected());
        return conState;
    }

    @Override
    public LiveData<WorldClock> getClock() {
        return this.clock;

    }
}
