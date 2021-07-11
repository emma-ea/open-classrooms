package com.oddlycoder.ocr.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.oddlycoder.ocr.model.WorldClock;
import com.oddlycoder.ocr.repo.IRepository;
import com.oddlycoder.ocr.repo.Repository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WorldTimeApiClient implements Callback<WorldClock> {

    public static final String TAG = "com.oddlycoder.ocr";
    public static final String BASE_URL = "http://worldclockapi.com/";

    private final IWorldTime service;
    private final Context context;

    public WorldTimeApiClient(Context context) {
        this.context = context;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();
        service = retrofit.create(IWorldTime.class);
    }


    public void start() {
        service.getTime().enqueue(this);
        Log.i(TAG, "start: getting time");
    }

    @Override
    public void onResponse(Call<WorldClock> call, Response<WorldClock> response) {
        if (response.isSuccessful() && response.body() != null) {
            Repository repo = Repository.initialize(context);
            repo.fetchTime(response.body());
            Log.i(TAG, "onResponse: clock day: " + response.body().getDayOfTheWeek());
        } else {
            Log.d(TAG, "onResponse: couldn't fetch clock");
        }
    }

    @Override
    public void onFailure(Call<WorldClock> call, Throwable throwable) {
       // call.enqueue(this);
        Log.e(TAG, "onFailure: couldn't get time", throwable);
    }
}
