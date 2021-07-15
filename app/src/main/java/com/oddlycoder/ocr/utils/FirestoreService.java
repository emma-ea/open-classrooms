package com.oddlycoder.ocr.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.oddlycoder.ocr.model.Classroom_rv;
import com.oddlycoder.ocr.model.Day;
import com.oddlycoder.ocr.model.TTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class FirestoreService {

    // TODO: retrieve all classes
    // retrieve users

    public static final String CL_COLLECTION = "classroom";
    public static final String TAG = "FirestoreService";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void getClassroom() {

        List<Classroom_rv> classroom_rvs = new ArrayList<>();

        db.collection("classroom")
                .get()
                .addOnCompleteListener((task) -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                           // Log.d(TAG, "onComplete: document id(name) --> " + documentSnapshot.getId());

                            CollectionReference colRefMonday = db.collection("classroom")
                                    .document(documentSnapshot.getId())
                                    .collection("Monday");

                            CollectionReference colRefTuesday = db.collection("classroom")
                                    .document(documentSnapshot.getId())
                                    .collection("Tuesday");

                            CollectionReference colRefWednesday = db.collection("classroom")
                                    .document(documentSnapshot.getId())
                                    .collection("Wednesday");

                            CollectionReference colRefThursday = db.collection("classroom")
                                    .document(documentSnapshot.getId())
                                    .collection("Thursday");

                            CollectionReference colRefFriday = db.collection("classroom")
                                    .document(documentSnapshot.getId())
                                    .collection("Friday");


                            BlockingQueue<CollectionReference> requestQueue =
                                    new ArrayBlockingQueue<>(5);
                            requestQueue.addAll(Arrays.asList(
                                    colRefMonday,
                                    colRefTuesday,
                                    colRefWednesday,
                                    colRefThursday,
                                    colRefFriday));

                            FirestoreProducer requestHandler = new FirestoreProducer(requestQueue);
                            Thread thread = new Thread(requestHandler);
                            thread.start();


                           /*  for (int i = 0; i < task.getResult().size(); i++) {
                                List<Day> wk = new ArrayList<>();
                                Classroom_rv cr = new Classroom_rv();
                                cr.setClassroom(documentSnapshot.getId());
                                classroom_rvs.set(i, cr);
                                classroom_rvs.get(i).setWeek(wk);


                                        db.collection("classroom")
                                        .document(documentSnapshot.getId())
                                        .collection("Monday")
                                        .get()
                                        .addOnCompleteListener((m) -> {
                                            if (m.isSuccessful() && m.getResult() != null) {
                                                for (QueryDocumentSnapshot doc : m.getResult()) {
                                                    Log.d(TAG, "id: " + documentSnapshot.getId() + " monday: --> id" + doc.getId() + " 00 " + doc.getData());
                                                    TTable hours = doc.toObject(TTable.class);
                                                    Day monday = new Day();
                                                    monday.setDay("Monday");
                                                    monday.setTtables(hours);
                                                    wk.add(monday);
                                                }
                                            }
                                        });

                                db.collection("classroom")
                                        .document(documentSnapshot.getId())
                                        .collection("Tuesday")
                                        .get()
                                        .addOnCompleteListener((t) -> {
                                            if (t.isSuccessful() && t.getResult() != null) {
                                                for (QueryDocumentSnapshot doc : t.getResult()) {
                                                    Log.d(TAG, "id: " + documentSnapshot.getId() + " tuesday: --> id" + doc.getId() + " 00 " + doc.getData());
                                                    TTable hours = doc.toObject(TTable.class);
                                                    Day tuesday = new Day();
                                                    tuesday.setDay("Tuesday");
                                                    tuesday.setTtables(hours);
                                                    wk.add(tuesday);
                                                }
                                            }
                                        });

                                db.collection("classroom")
                                        .document(documentSnapshot.getId())
                                        .collection("Wednesday")
                                        .get()
                                        .addOnCompleteListener((w) -> {
                                            if (w.isSuccessful() && w.getResult() != null) {
                                                for (QueryDocumentSnapshot doc : w.getResult()) {
                                                    Log.d(TAG, "id : " + documentSnapshot.getId() + " wednesday: --> id" + doc.getId() + " 00 " + doc.getData());
                                                    TTable hours = doc.toObject(TTable.class);
                                                    Day wednesday = new Day();
                                                    wednesday.setDay("Wednesday");
                                                    wednesday.setTtables(hours);
                                                    wk.add(wednesday);
                                                }
                                            }
                                        });

                                db.collection("classroom")
                                        .document(documentSnapshot.getId())
                                        .collection("Thursday")
                                        .get()
                                        .addOnCompleteListener((th) -> {
                                            if (th.isSuccessful() && th.getResult() != null) {
                                                for (QueryDocumentSnapshot doc : th.getResult()) {
                                                    Log.d(TAG, "id: " + documentSnapshot.getId() + " thursday: --> id" + doc.getId() + " 00 " + doc.getData());
                                                    TTable hours = doc.toObject(TTable.class);
                                                    Day thursday = new Day();
                                                    thursday.setDay("Thursday");
                                                    thursday.setTtables(hours);
                                                    wk.add(thursday);
                                                }
                                            }
                                        });

                                db.collection("classroom")
                                        .document(documentSnapshot.getId())
                                        .collection("Friday")
                                        .get()
                                        .addOnCompleteListener((f) -> {
                                            if (f.isSuccessful() && f.getResult() != null) {
                                                for (QueryDocumentSnapshot doc : f.getResult()) {
                                                    Log.d(TAG, "id: " + documentSnapshot.getId() +  " Friday: --> id" + doc.getId() + " 00 " + doc.getData());
                                                    TTable hours = doc.toObject(TTable.class);
                                                    Day friday = new Day();
                                                    friday.setDay("Friday");
                                                    friday.setTtables(hours);
                                                    wk.add(friday);
                                                }
                                            }
                                        });
                                // end of
                            }
*/
                        }
                    }
                });

        //TODO: have a thread pool
        // TODO: put all request into pool
        // TODO:
    }

}
