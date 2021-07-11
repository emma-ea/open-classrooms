package com.oddlycoder.ocr.viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.oddlycoder.ocr.repo.Repository;

public class AuthViewModel extends AndroidViewModel {

    private final Repository repository;
    private final LiveData<Boolean> conState;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public AuthViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.initialize(application.getApplicationContext());
        conState = repository.getNetworkState();
    }

    public LiveData<Boolean> isNetworkConnected() {
        return conState;
    }

}
