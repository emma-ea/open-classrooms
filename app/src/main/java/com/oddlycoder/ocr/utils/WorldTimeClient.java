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

public class WorldTimeClient {

    public static final String TAG = "WorldTimeApiClient";
    public static final String BASE_URL = "http://worldclockapi.com/";

    private final IWorldTime service;

    public WorldTimeClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();
        service = retrofit.create(IWorldTime.class);
    }

    public Call<WorldClock> start() {
        return service.getTime();
    }

}
