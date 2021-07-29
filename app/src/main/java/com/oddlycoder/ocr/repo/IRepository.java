package com.oddlycoder.ocr.repo;

import androidx.lifecycle.LiveData;

import com.oddlycoder.ocr.model.WorldClock;

public interface IRepository {
    LiveData<WorldClock> getClock();
}
