package com.oddlycoder.ocr.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.oddlycoder.ocr.repo.Repository;

import java.util.Map;

public class ProfileEditDialogViewModel extends AndroidViewModel {

    private Repository repository;

    public ProfileEditDialogViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.get();
    }

    public LiveData<Boolean> updateProfile(String uuid, Map<String, String> data) {
        return repository.updateProfile(uuid, data);
    }
}
