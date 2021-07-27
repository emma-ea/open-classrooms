package com.oddlycoder.ocr.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.oddlycoder.ocr.model.Classroom;
import com.oddlycoder.ocr.model.WorldClock;
import com.oddlycoder.ocr.repo.Repository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private final Repository repository;
    public static final String TAG = "HomeFragmentVM";


    public HomeViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.get();
        repository.getTime();
    }

    public LiveData<WorldClock> getClock() {
        return repository.getClock();
    }

    public LiveData<List<Classroom>> getClassrooms() {
        Log.d(TAG, "service getClassroom: view Model");
        return repository.getClassroom();
    }

}
