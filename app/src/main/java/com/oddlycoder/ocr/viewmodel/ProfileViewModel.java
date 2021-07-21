package com.oddlycoder.ocr.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.oddlycoder.ocr.model.Student;
import com.oddlycoder.ocr.repo.Repository;

public class ProfileViewModel extends AndroidViewModel {

    Repository repository;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.get();
    }

    public LiveData<Student> getUserDetail(String uuid) {
        return repository.getUserDetail(uuid);
    }

}
