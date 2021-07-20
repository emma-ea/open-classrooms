package com.oddlycoder.ocr.utils;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.oddlycoder.ocr.model.Classroom;
import com.oddlycoder.ocr.model.Day;
import com.oddlycoder.ocr.model.TTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class FirestoreProducer implements Runnable {

    public static final String TAG = "FirestoreProducer";

    private final BlockingQueue<CollectionReference> requestQueue;
    private final int queueRequestsSize;

    private String classroomName;

    private final List<Classroom> classroomList = new ArrayList<>();
    private List<Day> week;

    private MutableLiveData<Boolean> loader = new MutableLiveData<>();

    private static final MutableLiveData<List<Classroom>> classroomData = new MutableLiveData<>();

    public FirestoreProducer(BlockingQueue<CollectionReference> requestQueue, String classroomName) {
        this.requestQueue = requestQueue;
        this.queueRequestsSize = requestQueue.size();
        this.classroomName = classroomName;

        Classroom cr = new Classroom();
        week = new ArrayList<>();
        cr.setClassroom(classroomName);
        cr.setWeek(week);
        classroomList.add(cr);

    }

    public void Tp() {
        Observable<BlockingQueue<CollectionReference>> observable = Observable.just(requestQueue);
        observable.subscribe(new Observer<BlockingQueue<CollectionReference>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull BlockingQueue<CollectionReference> queue) {
                for (int i = 0; i < queueRequestsSize; i++) {
                    try {
                        CollectionReference ref = queue.take();
                        ref.get()
                                .addOnCompleteListener((tasks) -> {
                                    if (tasks.isSuccessful() && tasks.getResult() != null) {
                                        // hours for every day
                                        for (QueryDocumentSnapshot snapshot : tasks.getResult()) {
                                            // Log.d(TAG, "run: parent id --> " + ref.getParent().getId() + " ref id --> " + ref.getId() + " data id --> " + snapshot.getId() + " --> data " + snapshot.getData());

                                            TTable hours = snapshot.toObject(TTable.class);
                                            Day day = new Day();
                                            day.setDay(ref.getId());
                                            day.setTtables(hours);
                                            //Log.d(TAG, "run: day: " + day.getDay() + " hours: " + day.getTtables());
                                            week.add(day);
                                        }
                                    }

                                    classroomData.postValue(classroomList);
                                });
                    } catch (InterruptedException ignored) {}
                }

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
               /* for (Classroom_rv c : classroomList) {
                    Log.d(TAG, "run onComplete obs: classroom rv: " + c.getClassroom()
                            + " day: --> " + Arrays.toString(c.getWeek().toArray()));
                }*/
                classroomData.postValue(classroomList);
            }
        });
    }

    @Override
    public void run() {

        for (int i = 0; i < queueRequestsSize; i++) {
            try {
                CollectionReference ref = requestQueue.take();
                ref.get()
                        .addOnCompleteListener((tasks) -> {
                            if (tasks.isSuccessful() && tasks.getResult() != null) {
                                // hours for every day
                                for (QueryDocumentSnapshot snapshot : tasks.getResult()) {
                                    // Log.d(TAG, "run: parent id --> " + ref.getParent().getId() + " ref id --> " + ref.getId() + " data id --> " + snapshot.getId() + " --> data " + snapshot.getData());

                                    TTable hours = snapshot.toObject(TTable.class);
                                    Day day = new Day();
                                    day.setDay(ref.getId());
                                    day.setTtables(hours);
                                    Log.d(TAG, "run: thread day: " + day.getDay() + "hours: " + day.getTtables());
                                    week.add(day);
                                }
                            }
                            for (Classroom c : classroomList) {
                                Log.d(TAG, "run: thread classroom rv: " + c.getClassroom()
                                        + "day: --> " + Arrays.toString(c.getWeek().toArray()));
                            }
                            classroomData.postValue(classroomList);
                        });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public static LiveData<List<Classroom>> fetchClassrooms() {
        return classroomData;
    }
}
