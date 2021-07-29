package com.oddlycoder.ocr.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.oddlycoder.ocr.model.BookedClassroom;
import com.oddlycoder.ocr.repo.Repository;

import java.util.List;

public class BookedListViewModel extends AndroidViewModel {

    private Repository repository;

    public BookedListViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.get();
    }

    public LiveData<List<BookedClassroom>> getBooked() {
        return repository.getBookedClassrooms();
    }

}
