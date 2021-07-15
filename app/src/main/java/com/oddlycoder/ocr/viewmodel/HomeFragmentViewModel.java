package com.oddlycoder.ocr.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.oddlycoder.ocr.model.Classroom_rv;
import com.oddlycoder.ocr.model.WorldClock;
import com.oddlycoder.ocr.repo.Repository;
import com.oddlycoder.ocr.utils.WorldTimeApiClient;

import java.util.List;

public class HomeFragmentViewModel extends AndroidViewModel {

    private final Repository repository;
    public static final String TAG = "HomeFragmentVM";

    public HomeFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.initialize(application.getApplicationContext());
    }

    public LiveData<WorldClock> getClock() {
        return repository.getClock();
    }

    public void getClassroom() {
        repository.getClassroom();
        Log.d(TAG, "getClassroom: view Model");
    }

    public LiveData<List<Classroom_rv>> getClassroomData() {
        return repository.getClassroomData();
    }

}
