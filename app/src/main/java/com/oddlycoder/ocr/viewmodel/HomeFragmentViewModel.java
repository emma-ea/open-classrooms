package com.oddlycoder.ocr.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.oddlycoder.ocr.model.WorldClock;
import com.oddlycoder.ocr.repo.Repository;
import com.oddlycoder.ocr.utils.WorldTimeApiClient;

public class HomeFragmentViewModel extends AndroidViewModel {

    private final Repository repository;

    public HomeFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.initialize(application.getApplicationContext());
    }

    public LiveData<WorldClock> getClock() {
        return repository.getClock();
    }



}
