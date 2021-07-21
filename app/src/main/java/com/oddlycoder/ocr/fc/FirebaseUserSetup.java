package com.oddlycoder.ocr.fc;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.oddlycoder.ocr.model.Student;

import java.util.HashMap;
import java.util.Map;

public class FirebaseUserSetup {

    private String authKey;
    private static final String USERS = "users";

    private Student student;

    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private MutableLiveData<Student> studentDetail = new MutableLiveData<>();

    boolean success = false;

    public FirebaseUserSetup() { }

    public void createNewUser(String authKey, Student student) {
        this.authKey = authKey;
        this.student = student;
        createUserDocument();
    }

    public LiveData<Student> getStudentDetail(String uuid) {
        firestore.collection("users")
                .document(uuid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            student = task.getResult().toObject(Student.class);
                            studentDetail.postValue(student);
                        }
                    }
                });
        return studentDetail;
    }

    private void updateUserDetail(String uuid, Map<String, String> data) {
        firestore.collection("users")
                .document(uuid)
                .set(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // todo: handle response
                    }
                });
    }

    private void createUserDocument() {
        Map<String, String> studentData = new HashMap<>();
        studentData.put("fullName", student.getFullName());
        studentData.put("username", student.getUsername());
        studentData.put("year", student.getYear());
        studentData.put("email", student.getEmail());
        studentData.put("courseProgram", student.getCourseProgram());
        studentData.put("profileImageUrl", student.getProfileImageUrl());

        firestore.collection(USERS)
                .document(authKey)
                .set(studentData)
                .addOnCompleteListener((listener) -> {
                    //TODO: handle response9
                    success = true;
                });
    }

}
