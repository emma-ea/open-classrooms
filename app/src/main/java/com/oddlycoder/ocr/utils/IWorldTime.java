package com.oddlycoder.ocr.utils;

import com.oddlycoder.ocr.model.WorldClock;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IWorldTime {
    @GET("api/json/utc/now")
    Call<WorldClock> getTime();
}
