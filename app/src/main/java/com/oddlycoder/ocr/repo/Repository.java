package com.oddlycoder.ocr.repo;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.oddlycoder.ocr.fc.FirebaseUserSetup;
import com.oddlycoder.ocr.model.BookedClassroom;
import com.oddlycoder.ocr.model.Classroom;
import com.oddlycoder.ocr.model.Student;
import com.oddlycoder.ocr.model.WorldClock;
import com.oddlycoder.ocr.fc.FirestoreService;
import com.oddlycoder.ocr.utils.WorldTimeClient;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository implements IRepository {

    public static final String TAG = "Repository";

    private static Repository instance = null;

    private final MutableLiveData<WorldClock> clock = new MutableLiveData<>();
    private final FirestoreService fs;
    private FirebaseUserSetup userSetup;
    private final WorldTimeClient client;

    private Repository(Context ctx) {
        client = new WorldTimeClient();
        client.start();
        fs = new FirestoreService();
        userSetup = new FirebaseUserSetup();
    }

    public static void initialize(Context ctx) {
        if (instance == null)
            instance = new Repository(ctx);
    }

    public static Repository get() {
        if (instance != null)
            return instance;
        throw new IllegalStateException("repository not initialized");
    }

    public void getTime() {
        client.start().enqueue(new Callback<WorldClock>() {
            @Override
            public void onResponse(Call<WorldClock> call, Response<WorldClock> response) {
                clock.postValue(response.body());
            }

            @Override
            public void onFailure(Call<WorldClock> call, Throwable t) { }
        });
    }

    @Override
    public LiveData<WorldClock> getClock() {
        return this.clock;
    }

    public LiveData<List<Classroom>> getClassroom() {
        return fs.getClassrooms();
    }

    public void setUpNewUser(Student student, String Uuid) {
        userSetup.createNewUser(Uuid, student);
    }

    public LiveData<Student> getUserDetail(String uuid) {
        return userSetup.getStudentDetail(uuid);
    }

    public LiveData<Boolean> updateProfile(String uuid, Map<String, String> updateData) {
        return userSetup.updateUserDetail(uuid, updateData);
    }

    public LiveData<Boolean> addBooked(BookedClassroom bookedClassroom) {
        return fs.addBookedClassroom(bookedClassroom);
    }

    public LiveData<List<BookedClassroom>> getBookedClassrooms() {
        return fs.getBookedClassrooms();
    }

}



