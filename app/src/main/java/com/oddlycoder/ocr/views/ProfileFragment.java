package com.oddlycoder.ocr.views;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.oddlycoder.ocr.R;
import com.oddlycoder.ocr.model.Student;
import com.oddlycoder.ocr.viewmodel.ProfileViewModel;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";

    private ProfileViewModel profileViewModel;
    private final FirebaseUser firebaseUser =
            FirebaseAuth.getInstance().getCurrentUser();

    private TextView userName, fullName, courseProgram, year, email;
    private TextView profileHeaderWelcome;
    private CircleImageView imageView;

    private Button profileUpdate, deleteAccount;

    private ProgressBar imageLoad, unameLoad, fNameLoad, courseLoad, yearLoad;

    private Student student;

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

        imageLoad = view.findViewById(R.id.image_load_progress);
        unameLoad = view.findViewById(R.id.username_load_progress);
        fNameLoad = view.findViewById(R.id.fullname_load_progress);
        courseLoad = view.findViewById(R.id.course_load_progress);
        yearLoad = view.findViewById(R.id.year_load_progress);

        profileUpdate = view.findViewById(R.id.profile_update);
        deleteAccount = view.findViewById(R.id.profile_delete);

        profileHeaderWelcome = view.findViewById(R.id.profile_user_name);

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileViewModel.getUserDetail(firebaseUser.getUid()).observe(getViewLifecycleOwner(), student -> {
            showProgressBar();
            if (student != null) {
                this.student = student;
                setUpProfile(student);
                hideProgressBar();
            }
        });

        profileUpdate();
        deleteAccount();
        
    }

    private void profileUpdate() {
        profileUpdate.setOnClickListener(listener -> {
            ProfileEditDialog dialog = ProfileEditDialog.newInstance(student);
            this.getParentFragmentManager().setFragmentResultListener(
                    ProfileEditDialog.STUDENT_DATA,
                    ProfileFragment.this,
                    resultListener
            );
            dialog.show(this.getParentFragmentManager(), "profile-update");
        });
    }

    private final FragmentResultListener resultListener = (requestKey, result) -> {
        if (requestKey.equals(ProfileEditDialog.STUDENT_DATA)) {
            setUpProfile((Student) result.getSerializable(ProfileEditDialog.STUDENT_DATA));
        }
    };

    private void deleteAccount() {
        deleteAccount.setOnClickListener(listener -> {
            callbacks.deleteProfile();
        });
    }

    private void hideProgressBar() {
        unameLoad.setVisibility(View.GONE);
        fNameLoad.setVisibility(View.GONE);
        courseLoad.setVisibility(View.GONE);
        yearLoad.setVisibility(View.GONE);
    }

    private void showProgressBar() {
        unameLoad.setVisibility(View.VISIBLE);
        fNameLoad.setVisibility(View.VISIBLE);
        courseLoad.setVisibility(View.VISIBLE);
        yearLoad.setVisibility(View.VISIBLE);
    }

    private void setUpProfile(Student student) {
        String welcome = String.format(
                "Welcome, %s",
                student.getFullName().split(" ")[0]
        );
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
                    .listener(imageRequestListener())
                    .placeholder(R.drawable.ic_account_circle)
                    .into(imageView);
        }
    }

    public RequestListener<Drawable> imageRequestListener() {
        return new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable  GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                imageLoad.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                imageLoad.setVisibility(View.GONE);
                return false;
            }
        };
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callbacks = (ProfileCallbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: profile fragment");
    }
    
    private ProfileCallbacks callbacks;
    
    interface ProfileCallbacks {
        void deleteProfile();
    }



}
