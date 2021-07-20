package com.oddlycoder.ocr.utils;

import com.google.firebase.firestore.FirebaseFirestore;
import com.oddlycoder.ocr.model.Student;

import java.util.HashMap;
import java.util.Map;

public class FirebaseUserSetup {

    private final String authKey;
    private static final String USERS = "users";

    private final Student student;

    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    boolean success = false;

    public FirebaseUserSetup(String authKey, Student student) {
        this.authKey = authKey;
        this.student = student;
        createUserDocument();
    }

    private void createUserDocument() {
        Map<String, String> studentData = new HashMap<>();
        studentData.put("fullName", student.getFullName());
        studentData.put("username", student.getUsername());
        studentData.put("year", student.getYear());
        studentData.put("courseProgram", student.getCourseProgram());
        studentData.put("profileImageUrl", student.getProfileImageUrl());

        firestore.collection(USERS)
                .document(authKey)
                .set(studentData)
                .addOnCompleteListener((listener) -> {
                    //TODO: handle response
                    success = true;
                });
    }

}
