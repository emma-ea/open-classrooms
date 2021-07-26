package com.oddlycoder.ocr.fc;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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

    // TODO: retrieve all classes
    // retrieve users

    public static final String CL_COLLECTION = "classroom";
    public static final String TAG = "FirestoreService";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final MutableLiveData<List<Classroom>> classroomLiveData = new MutableLiveData<>();


 /*   @RequiresApi(api = Build.VERSION_CODES.N)
    public LiveData<List<Classroom>> queryStore(String searchString) {
        List<Classroom> classrooms = classroomLiveData.getValue();
        if (classrooms != null) {
            classrooms.stream().findAny().filter(cr -> {
                return cr.getClassroom().matches(searchString);
            });
        }
        MutableLiveData<List<Classroom>> result = new MutableLiveData<>();
        result.postValue(classrooms);
        return result;
    }*/

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
                                Log.d(TAG, "getClassroom: day: " + day.getDay());
                                Map<String, String> hours = (Map) documentSnapshot.getData().get(key);
                                Log.d(TAG, "getClassroom: hours: " + documentSnapshot.getData().get(key));
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

                            /*CollectionReference colRefMonday = db.collection("classroom")
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


                            BlockingQueue<CollectionReference> requestQueue = new ArrayBlockingQueue<>(5);
                            requestQueue.addAll(Arrays.asList(
                                    colRefMonday,
                                    colRefTuesday,
                                    colRefWednesday,
                                    colRefThursday,
                                    colRefFriday));

                            FirestoreProducer producer = new FirestoreProducer(requestQueue, documentSnapshot.getId());
                            producer.Tp();*/

        // Log.d(TAG, "onComplete firestoreService: document id(name) --> " + documentSnapshot.getId());
                           /* Log.d(TAG, "getClassroom: data: --> " + documentSnapshot.getData().keySet());

                            Classroom_rv c = new Classroom_rv();
                            c.setClassroom(documentSnapshot.getId());

                            List<Day> week = new ArrayList<>();
                            for (String day : documentSnapshot.getData().keySet()) {
                                Day d = new Day();
                                d.setDay(day);
                                week.add(d);
                            }*/

        //d.setDay(documentSnapshot.getData().values());

                            /*new Thread(new FirestoreProducer(requestQueue, documentSnapshot.getId()))
                                    .start();*/


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
