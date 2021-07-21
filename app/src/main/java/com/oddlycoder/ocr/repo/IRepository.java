package com.oddlycoder.ocr.repo;

import androidx.lifecycle.LiveData;

import com.oddlycoder.ocr.model.WorldClock;

public interface IRepository {
    public LiveData<WorldClock> getClock();
}
