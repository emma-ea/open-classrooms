package com.oddlycoder.ocr.utils;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Continuation;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.oddlycoder.ocr.model.Classroom_rv;
import com.oddlycoder.ocr.model.Day;
import com.oddlycoder.ocr.model.TTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class FirestoreProducer implements Runnable {

    public static final String TAG = "FirestoreProducer";

    private final BlockingQueue<CollectionReference> requestQueue;
    private final int queueRequestsSize;

    private final List<Classroom_rv> classroomList = new ArrayList<>();
    private static final MutableLiveData<List<Classroom_rv>> classroomData = new MutableLiveData<>();

    public FirestoreProducer(BlockingQueue<CollectionReference> requestQueue) {
        this.requestQueue = requestQueue;
        this.queueRequestsSize = requestQueue.size();
    }

    @Override
    public void run() {
        List<Day> wk = new ArrayList<>();
        for (int i = 0; i < queueRequestsSize; i++) {

            try {
                CollectionReference ref = requestQueue.take();

                Classroom_rv cr = new Classroom_rv();
                cr.setClassroom(ref.getParent().getId());
                classroomList.add(cr);

                ref.get()
                        .addOnCompleteListener((tasks) -> {
                            if (tasks.isSuccessful() && tasks.getResult() != null) {
                                for (QueryDocumentSnapshot snapshot : tasks.getResult()) {
                                    // Log.d(TAG, "run: parent id --> " + ref.getParent().getId() + " ref id --> " + ref.getId() + " data id --> " + snapshot.getId() + " --> data " + snapshot.getData());

                                    TTable hours = snapshot.toObject(TTable.class);
                                    Day day = new Day();
                                    day.setDay(ref.getId());
                                    day.setTtables(hours);
                                    wk.add(day);
                                }
                            }
                            if (tasks.isComplete()) {
                                classroomList.add(cr);
                            }
                        });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        classroomData.postValue(classroomList);
    }

    public static LiveData<List<Classroom_rv>> fetchClassrooms() {
        return classroomData;
    }
}
