package com.oddlycoder.ocr.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.oddlycoder.ocr.model.BookedClassroom;
import com.oddlycoder.ocr.model.Student;
import com.oddlycoder.ocr.repo.Repository;

public class ClassroomDialogViewModel extends AndroidViewModel {

    private Repository repository;

    public ClassroomDialogViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.get();
    }

    public LiveData<Boolean> addBooked(BookedClassroom bookedClassroom) {
        return repository.addBooked(bookedClassroom);
    }

    public LiveData<Student> getUserInfo(String uuid) {
        return repository.getUserDetail(uuid);
    }

}
