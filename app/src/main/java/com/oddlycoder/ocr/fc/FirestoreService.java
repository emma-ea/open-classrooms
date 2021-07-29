package com.oddlycoder.ocr.fc;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.oddlycoder.ocr.model.BookedClassroom;
import com.oddlycoder.ocr.model.Classroom;
import com.oddlycoder.ocr.model.Day;
import com.oddlycoder.ocr.model.TTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class FirestoreService {

    public static final String CL_COLLECTION = "classroom";
    public static final String BOOKED = "booked";
    public static final String TAG = "FirestoreService";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final MutableLiveData<List<Classroom>> classroomLiveData = new MutableLiveData<>();

    public LiveData<Boolean> addBookedClassroom(BookedClassroom bookedClassroom) {
        MutableLiveData<Boolean> booked = new MutableLiveData<>();
        db.collection(BOOKED)
                .add(bookedClassroom)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        booked.postValue(true);
                    }
                });
        return booked;
    }

    public LiveData<List<BookedClassroom>> getBookedClassrooms() {
        MutableLiveData<List<BookedClassroom>> booked = new MutableLiveData<>();
        List<BookedClassroom> list = new ArrayList<>();
        db.collection(BOOKED)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (DocumentSnapshot snapshot : task.getResult()) {
                            list.add(snapshot.toObject(BookedClassroom.class));
                        }
                        booked.postValue(list);
                    }
                });
        return booked;
    }

    public LiveData<List<Classroom>> getClassroom() {

        List<Classroom> classrooms = new ArrayList<>();

        db.collection(CL_COLLECTION)
                .get()
                .addOnCompleteListener((task) -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (DocumentSnapshot documentSnapshot : task.getResult()) { // fetch classrooms

                            Classroom classroom = new Classroom();
                            classroom.setClassroom(documentSnapshot.getId());
                            Log.d(TAG, "getClassroom: document id: " + documentSnapshot.getId());

                            List<Day> week = new ArrayList<>();
                            for (String key : documentSnapshot.getData().keySet()) {
                                Day day = new Day();
                                day.setDay(key);
                                Map<String, String> hours = (Map) documentSnapshot.getData().get(key);
                                day.setHours(hours);
                                week.add(day);
                            }
                            classroom.setWeek(week);
                            classrooms.add(classroom);
                        }
                        classroomLiveData.postValue(classrooms);
                    }
                });
        return classroomLiveData;
    }
}
