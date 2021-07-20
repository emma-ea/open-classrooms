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

    public AuthViewModel(@NonNull Application application) {
        super(application);
    }
}
