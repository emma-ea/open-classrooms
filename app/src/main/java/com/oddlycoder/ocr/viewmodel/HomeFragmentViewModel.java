package com.oddlycoder.ocr.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.oddlycoder.ocr.model.Classroom;
import com.oddlycoder.ocr.model.WorldClock;
import com.oddlycoder.ocr.repo.Repository;

import java.util.List;

public class HomeFragmentViewModel extends AndroidViewModel {

    private final Repository repository;
    public static final String TAG = "HomeFragmentVM";

    public HomeFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.get();
    }

    public LiveData<WorldClock> getClock() {
        return repository.getClock();
    }

    public LiveData<List<Classroom>> sgetClassroom() {
        Log.d(TAG, "service getClassroom: view Model");
        return repository.getClassroom();
    }

    public LiveData<List<Classroom>> getClassroomData() {
        return repository.getClassroomData();
    }

}
