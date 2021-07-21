package com.oddlycoder.ocr.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.oddlycoder.ocr.R;
import com.oddlycoder.ocr.model.Student;
import com.oddlycoder.ocr.viewmodel.ProfileViewModel;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";

    private ProfileViewModel profileViewModel;
    private final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    private TextView userName, fullName, courseProgram, year, email;
    private TextView profileHeaderWelcome;
    private CircleImageView imageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        userName = view.findViewById(R.id.user_profile_username_res);
        fullName = view.findViewById(R.id.user_profile_fullname_res);
        email = view.findViewById(R.id.user_profile_email);
        courseProgram = view.findViewById(R.id.user_profile_course_res);
        year = view.findViewById(R.id.user_profile_year_res);
        imageView = view.findViewById(R.id.user_profile_image);

        profileHeaderWelcome = view.findViewById(R.id.profile_user_name);

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileViewModel.getUserDetail(firebaseUser.getUid()).observe(getViewLifecycleOwner(), new Observer<Student>() {
            @Override
            public void onChanged(Student student) {
                if (student != null) {
                    setUpProfile(student);
                }
            }
        });
    }

    private void setUpProfile(Student student) {
        String fName = student.getFullName().split(" ")[0];
        String welcome = String.format("Welcome, %s", fName);
        profileHeaderWelcome.setText(welcome);
        userName.setText(student.getUsername());
        fullName.setText(student.getFullName());
        email.setText(student.getEmail());
        courseProgram.setText(student.getCourseProgram());
        year.setText(student.getYear());
        setImage(student.getProfileImageUrl());
    }

    private void setImage(String imageUrl) {
        if (getActivity() != null) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_account_circle)
                    .into(imageView);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: profile fragment");
    }
}
