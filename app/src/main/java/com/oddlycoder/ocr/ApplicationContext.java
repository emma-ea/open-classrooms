package com.oddlycoder.ocr;

import android.app.Application;

import com.oddlycoder.ocr.repo.Repository;

public class ApplicationContext extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Repository.initialize(this);
    }
}
