package com.oddlycoder.ocr.fc;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.oddlycoder.ocr.model.BookedClassroom;
import com.oddlycoder.ocr.model.Classroom;
import com.oddlycoder.ocr.model.Day;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    @SuppressWarnings("unchecked cast")
    public LiveData<List<Classroom>> getClassrooms() {

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
                            for (String key : Objects.requireNonNull(documentSnapshot.getData()).keySet()) {
                                Day day = new Day();
                                day.setDay(key);
                                Map<String, String> hours = (Map<String, String>) documentSnapshot.getData().get(key);
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
