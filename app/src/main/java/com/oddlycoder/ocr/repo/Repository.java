package com.oddlycoder.ocr.repo;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.oddlycoder.ocr.model.Classroom;
import com.oddlycoder.ocr.model.WorldClock;
import com.oddlycoder.ocr.utils.FirestoreProducer;
import com.oddlycoder.ocr.utils.FirestoreService;
import com.oddlycoder.ocr.utils.WorldTimeApiClient;

import java.util.List;

public class Repository implements IRepository {

    public static final String TAG = "com.oddlycoder.ocr";

    private static Repository instance = null;
    private final Context context;

    private final MutableLiveData<WorldClock> clock = new MutableLiveData<>();
    private FirestoreService fs;

    private Repository(Context ctx) {
        this.context = ctx;
        WorldTimeApiClient client = new WorldTimeApiClient(ctx);
        fs = new FirestoreService();
        client.start();
    }

    public static void initialize(Context ctx) {
        if (instance == null)
            instance = new Repository(ctx);
    }

    public static Repository get() {
        if (instance != null)
            return instance;
        throw new IllegalStateException("repository not initialized");
    }

    public void fetchTime(WorldClock clock) {
        Log.i(TAG, "fetchTime: ");
        this.clock.setValue(clock);
    }

    @Override
    public LiveData<WorldClock> getClock() {
        return this.clock;

    }

    public LiveData<List<Classroom>> getClassroom() {
        Log.d(TAG, "getClassroom: repository");
        return fs.getClassroom();
    }

    public LiveData<List<Classroom>> getClassroomData() {
        return FirestoreProducer.fetchClassrooms();
    }


}



