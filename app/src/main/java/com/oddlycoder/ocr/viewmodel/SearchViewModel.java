package com.oddlycoder.ocr.viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.oddlycoder.ocr.model.Classroom;
import com.oddlycoder.ocr.repo.Repository;

import java.util.List;

public class SearchViewModel extends AndroidViewModel {

    Repository repository;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.get();

    }

    //TODO: get classroom list from home frag viewmodel.

}
